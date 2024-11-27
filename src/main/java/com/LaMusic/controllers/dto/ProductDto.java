package com.LaMusic.controllers.dto;

import java.math.BigDecimal;
import java.util.List;

import com.LaMusic.entity.Category;
import com.LaMusic.entity.Product;

public record ProductDto(
		String name, 
		String description,
		BigDecimal price,
		Integer stock,
		String imageUrl,
		List<Long> categoryIds ) {

	public Product toProduct(List<Category> categories) {
		return new Product(null,
				name,
				description,
				price,
				stock,
				imageUrl,
				categories);
	}
	
}
