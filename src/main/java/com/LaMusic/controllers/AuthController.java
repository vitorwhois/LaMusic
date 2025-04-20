package com.LaMusic.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.LaMusic.controllers.dto.LoginRequest;
import com.LaMusic.controllers.dto.*;
import com.LaMusic.security.JwtUtil;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

	@Autowired
	private AuthenticationManager authenticationManager;
	
	@Autowired
	private UserDetailsService userDetailsService;
	
	@Autowired
	private JwtUtil jwtUtil;
	
	
	
	@PostMapping
	public LoginResponse login (@RequestBody LoginRequest request) {
	
		authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(request.email(),
				request.password()));
	
		final UserDetails userDetails = userDetailsService.loadUserByUsername(request.email());	
		final String jwt = jwtUtil.generateToken(userDetails);	
		return new LoginResponse(jwt);	
	
	}
	
	
	
}
