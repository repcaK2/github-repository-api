package org.example.githubrepositoryapi.exception;

public class GitHubUserNotFoundException extends RuntimeException{
	public GitHubUserNotFoundException(String message) {
		super(message);
	}
}
