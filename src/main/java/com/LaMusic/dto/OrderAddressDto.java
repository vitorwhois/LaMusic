package com.LaMusic.dto;

public record OrderAddressDto(
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
