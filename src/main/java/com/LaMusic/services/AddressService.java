package com.LaMusic.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.LaMusic.Mappers.AddressMapper;
import com.LaMusic.dto.AddressCreateDto;
import com.LaMusic.dto.AddressDto;
import com.LaMusic.repositories.AddressRepository;

@Service
public class AddressService {
	
	@Autowired
	private AddressRepository addressRepository;
	
	@Autowired
	private AddressMapper addressMapper;
	
	public AddressDto createAddress(AddressCreateDto addressDto){
		var addressSaved = addressRepository.save(addressMapper.toEntity(addressDto));
		return addressMapper.toDto(addressSaved);
	}
	
}
