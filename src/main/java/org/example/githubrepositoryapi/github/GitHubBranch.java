package org.example.githubrepositoryapi.github;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GitHubBranch {

	private String name;
	private GitHubCommit commit;
}
