package com.LaMusic.dto;

import java.util.UUID;

public record AddressCreateDTO(
	 UUID id,
     String cep,
     String country,
     String state,
     String city,
     String street,
     String number,
     String complement,
     UUID userId
   ) {}


