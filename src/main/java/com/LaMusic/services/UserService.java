package com.LaMusic.services;

import org.springframework.stereotype.Service;

import com.LaMusic.controllers.dto.CreateUserDto;
import com.LaMusic.entity.LoginResponse;
import com.LaMusic.entity.User;
import com.LaMusic.repositories.UserRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class UserService {

	private UserRepository userRepository;
	
	public User createUser (CreateUserDto dto) {
		return userRepository.save(dto.toEntity());
	}

	public LoginResponse authenticateUser(String email, String password) {
		var user = userRepository.findByEmail(email);
		
		if (user.isEmpty()) {
			return new LoginResponse(false, "User not found.");
		}
		
//		if(!user.get().getPassword().equals(password)) {
//			throw new RuntimeException ("Password incorrect.");
//		}
		
		return new LoginResponse(true, user.get());
	}
	
}
