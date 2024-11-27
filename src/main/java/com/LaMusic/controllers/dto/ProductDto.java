package com.LaMusic.controllers.dto;

import java.math.BigDecimal;

import com.LaMusic.entity.Product;

public record ProductDto(String name, String description , BigDecimal price, Integer stock, String imageUrl) {

	public Product toProduct() {
		return new Product(null, name, description, price, stock, imageUrl, null);
	}
	
}
