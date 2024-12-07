package com.LaMusic.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class LoginResponse {

	private boolean success;
	private String message;
	private User user;

	public LoginResponse(boolean success, User user) {
		this.success = success;
		this.user = user;
	}

	// Construtor de falha
	public LoginResponse(boolean success, String message) {
		this.success = success;
		this.message = message;
	}

	public boolean isSuccess() {
		return success;
	}

}
