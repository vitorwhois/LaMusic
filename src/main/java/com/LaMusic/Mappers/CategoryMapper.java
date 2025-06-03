package com.LaMusic.Mappers;

import com.LaMusic.dto.CategoryDTO;
import com.LaMusic.entity.Category;

public class CategoryMapper {

	public static CategoryDTO toDTO(Category category) {
        CategoryDTO dto = new CategoryDTO();
        dto.setId(category.getId());
        dto.setName(category.getName());
        dto.setSlug(category.getSlug());
        dto.setDescription(category.getDescription());
        dto.setParentId(category.getParent() != null ? category.getParent().getId() : null);
        dto.setSortOrder(category.getSortOrder());
        dto.setActive(category.isActive());
        return dto;
    }

    public static Category toEntity(CategoryDTO dto, Category parentCategory) {
        Category category = new Category();
        category.setId(dto.getId());
        category.setName(dto.getName());
        category.setSlug(dto.getSlug());
        category.setDescription(dto.getDescription());
        category.setParent(parentCategory);
        category.setSortOrder(dto.getSortOrder());
        category.setActive(dto.isActive());
        return category;
    }
	
}
