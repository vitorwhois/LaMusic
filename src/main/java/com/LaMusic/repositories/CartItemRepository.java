package com.LaMusic.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.LaMusic.entity.CartItem;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {
	
}
