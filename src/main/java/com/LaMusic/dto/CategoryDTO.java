package com.LaMusic.dto;

import lombok.Data;

import java.util.UUID;

@Data
public class CategoryDTO {
    private UUID id;
    private String name;
    private String slug;
    private String description;
    private UUID parentId;
    private Integer sortOrder;
    private boolean isActive;
}
