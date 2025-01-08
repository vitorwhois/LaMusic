package com.LaMusic.services;

import java.util.List;

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
	
	
	public List<User> findAll() {
		return userRepository.findAll();		
	}
	
	public User createUser (CreateUserDto dto) {
		return userRepository.save(dto.toEntity());
	}

	public LoginResponse authenticateUser(String email, String password) {
		var user = userRepository.findByEmail(email);
		
		if (user.isEmpty()) {
			return new LoginResponse(false, "User not found.");
		}
		
		return new LoginResponse(true, user.get());
	}

	public User findById(Long userId) {
		 User user = userRepository.findById(userId)
				 .orElseThrow(() -> new RuntimeException("User not found!"));
		 return user;
	}

	public void findByUserId(User userId) {
		// TODO Auto-generated method stub
		
	}


	
}
