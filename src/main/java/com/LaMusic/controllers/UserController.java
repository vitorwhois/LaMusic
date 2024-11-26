package com.LaMusic.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.LaMusic.controllers.dto.CreateUserDto;
import com.LaMusic.controllers.dto.LoginDto;
import com.LaMusic.entity.User;
import com.LaMusic.services.UserService;

import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/usuarios")
@AllArgsConstructor
public class UserController {

	private UserService userService;
	
	@PostMapping
	public ResponseEntity<User> createUser(@RequestBody CreateUserDto createUserDto){
		 User newUser = userService.createUser(createUserDto);
		 return ResponseEntity.ok(newUser);
	}
	
	@PostMapping("/login")
	public ResponseEntity<User> login(@RequestBody LoginDto loginDto){
		try {
			var user = userService.authenticateUser(loginDto.email(), loginDto.password());
			return ResponseEntity.ok(user);
		} catch (RuntimeException e) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
		}
	}
	
}
