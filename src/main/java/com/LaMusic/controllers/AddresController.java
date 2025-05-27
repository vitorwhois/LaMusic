package com.LaMusic.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.LaMusic.dto.AddressCreateDto;
import com.LaMusic.dto.AddressDto;
import com.LaMusic.services.AddressService;

@RestController
@RequestMapping("/address")
public class AddresController {

	@Autowired
	private AddressService addressService; 
	
	@PostMapping
	public ResponseEntity<AddressDto> createAddress(@RequestBody AddressCreateDto address) {
		return ResponseEntity.status(HttpStatus.CREATED).body(
				addressService.createAddress(address));		
	}
	
	
}
