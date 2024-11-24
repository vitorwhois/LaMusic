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
}
