package com.LaMusic.services;

import org.springframework.stereotype.Service;

import com.LaMusic.controllers.dto.CreateUserDto;
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

	public User authenticateUser(String email, String password) {
		var user = userRepository.findByEmail(email);
		
		if (user.isEmpty()) {
			throw new RuntimeException("User not found.");
		}
		
		if(!user.get().getPassword().equals(password)) {
			throw new RuntimeException ("Password incorrect.");
		}
		return user.get();
	}
	
}
