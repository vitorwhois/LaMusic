package com.LaMusic.dto;

import com.LaMusic.enums.Role;

import lombok.Data;

@Data
public class CreateUserRequest {
    private String name;
    private String email;
    private String password;
    private String phone;
    private Role role;
}
