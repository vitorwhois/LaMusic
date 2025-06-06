package com.LaMusic.dto;

import java.util.ArrayList;

import com.LaMusic.entity.User;

public record CreateUserDTO(String name, String email, String password , String phone) {
	
	public User toEntity() {
		return new User();
	}
}
