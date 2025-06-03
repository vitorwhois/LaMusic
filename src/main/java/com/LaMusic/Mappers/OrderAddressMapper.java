package com.LaMusic.Mappers;

import java.time.OffsetDateTime;

import com.LaMusic.entity.Address;
import com.LaMusic.entity.OrderAddress;

public class OrderAddressMapper {

	private OrderAddress buildOrderAddressFromAddress(Address address) {
	    return OrderAddress.builder()
	        .recipientName(address.getRecipientName())
	        .street(address.getStreet())
	        .number(address.getNumber())
	        .complement(address.getComplement())
	        .neighborhood(address.getNeighborhood())
	        .city(address.getCity())
	        .state(address.getState())
	        .zipCode(address.getZipCode())
	        .country(address.getCountry())
	        .createdAt(OffsetDateTime.now())
	        .build();
	}

	
}
