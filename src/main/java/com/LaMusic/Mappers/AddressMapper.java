package com.LaMusic.Mappers;

import org.springframework.stereotype.Component;

import com.LaMusic.dto.AddressCreateDto;
import com.LaMusic.dto.AddressDto;
import com.LaMusic.entity.Address;

@Component
public class AddressMapper {

	public Address toEntity (AddressCreateDto dto) {
		
		Address address = new Address();
		address.setUserId(dto.userId());
		address.setCep(dto.cep());
		address.setCountry(dto.country());
		address.setState(dto.state());
		address.setCity(dto.city());
		address.setStreet(dto.street());
		address.setNumber(dto.number());
		address.setComplement(dto.complement());
		
		return address;
	}
	
	public AddressDto toDto(Address entity){
		
		AddressDto dto = new AddressDto();
		dto.setCep(entity.getCep());
		dto.setCountry(entity.getCountry());
		dto.setState(entity.getState());
		dto.setCity(entity.getCity());
		dto.setStreet(entity.getStreet());
		dto.setNumber(entity.getNumber());
		dto.setComplement(entity.getComplement());
		
		return dto;
	}
}
