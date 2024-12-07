package com.LaMusic.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.LaMusic.controllers.dto.CreateUserDto;
import com.LaMusic.controllers.dto.LoginDto;
import com.LaMusic.entity.LoginResponse;
import com.LaMusic.entity.User;
import com.LaMusic.services.UserService;

import lombok.AllArgsConstructor;

@CrossOrigin(origins = "http://localhost:19002")
@RestController
@RequestMapping("/usuarios")
@AllArgsConstructor
public class UserController {

	private static final Logger logger = LoggerFactory.getLogger(UserController.class);
	private UserService userService;

	@PostMapping
	public ResponseEntity<User> createUser(@RequestBody CreateUserDto createUserDto) {
		User newUser = userService.createUser(createUserDto);
		return ResponseEntity.ok(newUser);
	}
	@PostMapping("/login")
	public ResponseEntity<LoginResponse> login(@RequestBody LoginDto loginDto) {
	    try {
	        LoginResponse response = userService.authenticateUser(loginDto.email(), loginDto.password());
	        
	        if (response.isSuccess()) {
	            return ResponseEntity.ok(response);
	        } else {
	            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
	        }
	    } catch (RuntimeException e) {
	        logger.error("Erro ao autenticar usuário: " + e.getMessage(), e);
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new LoginResponse(false, "Server Error"));
	    }
	}

}
