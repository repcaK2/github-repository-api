package org.example.githubrepositoryapi.github;

import org.example.githubrepositoryapi.dto.RepositoryResponse;
import io.smallrye.mutiny.Uni;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/github")
public class GitHubController {

	private final GitHubService gitHubService;

	@Autowired
	public GitHubController(GitHubService gitHubService) {
		this.gitHubService = gitHubService;
	}

	@GetMapping("/{username}/repositories")
	public Uni<List<RepositoryResponse>> getRepositories(@PathVariable String username) {
		// For testing, we override the provided username with a sample username.
		String sampleUsername = "octocat";
		return gitHubService.getUserRepositories(sampleUsername);
	}
}
