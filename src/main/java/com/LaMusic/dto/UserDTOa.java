package com.LaMusic.dto;

import java.util.UUID;

import com.LaMusic.enums.Role;

import lombok.Data;

@Data
public class UserDTO {
    private UUID id;
    private String name;
    private String email;
    private String phone;
    private Role role;
    private Boolean emailVerified;
}