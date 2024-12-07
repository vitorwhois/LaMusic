package com.LaMusic.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.LaMusic.entity.Order;

public interface OrderRepository extends JpaRepository<Order, Long> {
	
}
