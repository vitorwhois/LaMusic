package com.LaMusic.dto;

public record OrderAddressDTO(
    String recipientName,
    String street,
    String number,
    String complement,
    String neighborhood,
    String city,
    String state,
    String zipCode,
    String country
) {}
