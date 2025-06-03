package com.LaMusic.dto;

import com.LaMusic.enums.Role;
import lombok.Data;

@Data
public class UpdateUserRequest {
    private String name;
    private String phone;
    private Role role;
}