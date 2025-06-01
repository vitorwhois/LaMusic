package com.LaMusic.repositories;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.LaMusic.entity.Order;

public interface OrderRepository extends JpaRepository<Order, UUID> {
	List<Order> findByUserId(UUID userId);
}
