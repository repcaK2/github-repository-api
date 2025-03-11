package org.example.githubrepositoryapi.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class RepositoryResponse {
	
	private String repositoryName;
	private String ownerLogin;
	private List<BranchResponse> branches;
}
