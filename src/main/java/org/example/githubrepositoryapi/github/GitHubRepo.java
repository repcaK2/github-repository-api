package org.example.githubrepositoryapi.github;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GitHubRepo {

	private String name;
	private boolean fork;
	private GitHubOwner owner;
}
