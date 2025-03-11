package org.example.githubrepositoryapi.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BranchResponse {

	private String branchName;
	private String lastCommitSha;
}
