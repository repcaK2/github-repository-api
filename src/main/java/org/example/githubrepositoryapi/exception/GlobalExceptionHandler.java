package org.example.githubrepositoryapi.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(GitHubUserNotFoundException.class)
	public ResponseEntity<Map<String, Object>> handleGitHubUserNotFoundException(GitHubUserNotFoundException exception) {
		Map<String, Object> errorResponse = new HashMap<>();
		HttpStatus status = HttpStatus.NOT_FOUND;
		errorResponse.put("status", status.value());
		errorResponse.put("message", exception.getMessage());

		return ResponseEntity.status(status).body(errorResponse);
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<Map<String, Object>> handleAllExceptions(Exception ex) {
		HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
		Map<String, Object> errorResponse = new HashMap<>();
		errorResponse.put("status", status.name());
		errorResponse.put("message", "An unexpected error occurred: " + ex.getMessage());
		return ResponseEntity.status(status).body(errorResponse);
	}
}
