package com.LaMusic.repositories;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.LaMusic.entity.OrderItem;

public interface OrderItemRepository extends JpaRepository<OrderItem, UUID> {
	
}
