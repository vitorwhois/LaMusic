package com.LaMusic.controllers.dto;

import com.LaMusic.entity.User;

public record CreateUserDto(String name, String email, String password , String phone) {

	
	public User toEntity() {
		return new User(null, name, email, password, phone);
	}
}
