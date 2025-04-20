package com.LaMusic.services;

import java.util.List;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.LaMusic.controllers.dto.CreateUserDto;
import com.LaMusic.entity.LoginResponse;
import com.LaMusic.entity.User;
import com.LaMusic.repositories.UserRepository;
import com.LaMusic.security.JwtUtil;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class UserService {

	private UserRepository userRepository;
	private PasswordEncoder passwordEncoder;
	private AuthenticationManager authenticationManager;
	private UserDetailsService userDetailsService;
	private JwtUtil jwtUtil;

	
	public List<User> findAll() {
		return userRepository.findAll();		
	}
	
	public User createUser (CreateUserDto dto) {
		User user = dto.toEntity();
		user.setPassword(passwordEncoder.encode(user.getPassword()));		
		return userRepository.save(user);
	}

	public LoginResponse authenticateUser(String email, String password) {
		var userOptional = userRepository.findByEmail(email);

		if (userOptional.isEmpty()) {
			return new LoginResponse(false, "User not found.");
		}

		// Tenta autenticar com Spring Security
		authenticationManager.authenticate(
			new UsernamePasswordAuthenticationToken(email, password)
		);

		// Gera o token JWT
		UserDetails userDetails = userDetailsService.loadUserByUsername(email);
		String token = jwtUtil.generateToken(userDetails);

		return new LoginResponse(true, "Login successful", token, userOptional.get());
	}

//	public LoginResponse authenticateUser(String email, String password) {
//		var user = userRepository.findByEmail(email);
//		
//		if (user.isEmpty()) {
//			return new LoginResponse(false, "User not found.");
//		}
//		
//		return new LoginResponse(true, user.get());
//	}

	public User findById(Long userId) {
		 User user = userRepository.findById(userId)
				 .orElseThrow(() -> new RuntimeException("User not found!"));
		 return user;
	}

	public void findByUserId(User userId) {
		// TODO Auto-generated method stub
		
	}


	
}
