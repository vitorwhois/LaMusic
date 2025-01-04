package com.LaMusic.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.LaMusic.entity.CartItem;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {

	List<CartItem> findByCart_Id(Long cartId);
	
}
