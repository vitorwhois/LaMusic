package com.LaMusic.repositories;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.LaMusic.entity.CartItem;

public interface CartItemRepository extends JpaRepository<CartItem, UUID> {

	List<CartItem> findByCart_Id(UUID cartId);
	
}
