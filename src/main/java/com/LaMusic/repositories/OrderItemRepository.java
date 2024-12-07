package com.LaMusic.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.LaMusic.entity.OrderItem;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
	
}
