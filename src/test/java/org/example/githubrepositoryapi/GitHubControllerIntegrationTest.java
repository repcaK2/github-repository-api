package org.example.githubrepositoryapi;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.reactive.server.WebTestClient;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class GitHubControllerIntegrationTest {

	@Autowired
	private WebTestClient webTestClient;

	@Test
	public void testGetRepositoriesHappyPath() {
		webTestClient.get()
				.uri("/github/octocat/repositories")
				.exchange()
				.expectStatus().isOk()
				.expectBody()
				.jsonPath("$").isArray()
				.jsonPath("$[0].repositoryName").exists()
				.jsonPath("$[0].ownerLogin").exists()
				.jsonPath("$[0].branches").isArray()
				.jsonPath("$[0].branches[0].branchName").exists()
				.jsonPath("$[0].branches[0].lastCommitSha").exists();
	}
}
