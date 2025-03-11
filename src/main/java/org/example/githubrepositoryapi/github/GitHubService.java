package org.example.githubrepositoryapi.github;

import io.smallrye.mutiny.Uni;
import org.example.githubrepositoryapi.dto.BranchResponse;
import org.example.githubrepositoryapi.dto.RepositoryResponse;
import org.example.githubrepositoryapi.exception.GitHubUserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class GitHubService {

	private final WebClient webClient;

	@Autowired
	public GitHubService(WebClient.Builder webClientBuilder,
						 @Value("${github.api.base-url}") String baseUrl) {
		// Initialize the WebClient with the base URL injected from application.yml
		this.webClient = webClientBuilder.baseUrl(baseUrl).build();
	}

	/**
	 * Retrieves a list of repositories for a given GitHub user that are not forks.
	 * For each repository, it also retrieves a list of its branches along with the SHA of the last commit.
	 */
	public Uni<List<RepositoryResponse>> getUserRepositories(String username) {
		return Uni.createFrom().publisher(
				webClient.get()
						// Build the URI using the provided username
						.uri("/users/{username}/repos", username)
						.retrieve()
						// Handle client errors (HTTP 4xx)
						.onStatus(status -> status.is4xxClientError(), clientResponse -> {
							// If the status is 404 Not Found, throw a specific exception
							if (clientResponse.statusCode().value() == HttpStatus.NOT_FOUND.value()) {
								return Mono.error(new GitHubUserNotFoundException("GitHub user not found"));
							}
							// For other 4xx errors, return a generic RuntimeException with the error body
							return clientResponse.bodyToMono(String.class)
									.flatMap(errorBody -> Mono.error(new RuntimeException(errorBody)));
						})
						// Convert the response body into a stream of GitHubRepo objects
						.bodyToFlux(GitHubRepo.class)
						// Filter out repositories that are forks
						.filter(repo -> !repo.isFork())
						// For each repository, retrieve its branches and map to a RepositoryResponse DTO
						.flatMap(repo ->
								webClient.get()
										// Build the URI for retrieving branches for the repository
										.uri("/repos/{owner}/{repo}/branches", repo.getOwner().getLogin(), repo.getName())
										.retrieve()
										// Convert the response into a stream of GitHubBranch objects and collect them as a list
										.bodyToFlux(GitHubBranch.class)
										.collectList()
										// Map the repository and its branches into a RepositoryResponse
										.map(branches -> {
											RepositoryResponse response = new RepositoryResponse();
											response.setRepositoryName(repo.getName());
											response.setOwnerLogin(repo.getOwner().getLogin());
											// Map each branch to a BranchResponse DTO
											List<BranchResponse> branchResponses = branches.stream().map(branch -> {
												BranchResponse br = new BranchResponse();
												br.setBranchName(branch.getName());
												br.setLastCommitSha(branch.getCommit().getSha());
												return br;
											}).collect(Collectors.toList());
											response.setBranches(branchResponses);
											return response;
										})
						)
						// Collect all RepositoryResponse objects into a list
						.collectList()
		);
	}
}