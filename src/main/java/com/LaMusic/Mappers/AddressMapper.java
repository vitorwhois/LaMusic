package com.LaMusic.Mappers;

import org.springframework.stereotype.Component;

import com.LaMusic.dto.AddressDTO;
import com.LaMusic.entity.Address;
import com.LaMusic.entity.User;

@Component
public class AddressMapper {

    public AddressDTO toDTO(Address address) {
        return AddressDTO.builder()
            .id(address.getId())
            .userId(address.getUser().getId())
            .type(address.getType())
            .label(address.getLabel())
            .recipientName(address.getRecipientName())
            .street(address.getStreet())
            .number(address.getNumber())
            .complement(address.getComplement())
            .neighborhood(address.getNeighborhood())
            .city(address.getCity())
            .state(address.getState())
            .zipCode(address.getZipCode())
            .country(address.getCountry())
            .isDefault(address.isDefault())
            .build();
    }

    public Address toEntity(AddressDTO dto, User user) {
        return Address.builder()
            .id(dto.getId())
            .user(user)
            .type(dto.getType())
            .label(dto.getLabel())
            .recipientName(dto.getRecipientName())
            .street(dto.getStreet())
            .number(dto.getNumber())
            .complement(dto.getComplement())
            .neighborhood(dto.getNeighborhood())
            .city(dto.getCity())
            .state(dto.getState())
            .zipCode(dto.getZipCode())
            .country(dto.getCountry())
            .isDefault(dto.isDefault())
            .build();
    }
}
