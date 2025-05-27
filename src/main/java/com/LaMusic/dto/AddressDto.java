package com.LaMusic.dto;

import java.util.UUID;

import lombok.Data;

@Data
public class AddressDto {
    private UUID id;
    private String cep;
    private String country;
    private String state;
    private String city;
    private String street;
    private String number;
    private String complement;
    private UUID userId;
}