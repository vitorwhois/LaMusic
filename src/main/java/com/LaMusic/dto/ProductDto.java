package com.LaMusic.dto;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import com.LaMusic.entity.Category;
import com.LaMusic.entity.Product;

public record ProductDto(
		String name, 
		String description,
		BigDecimal price,
		Integer stock,
		String imageUrl,
		List<UUID> categoryIds ) {

	public Product toProduct(List<Category> categories) {
		return new Product();
	}
	
}
