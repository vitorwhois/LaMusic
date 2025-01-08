package com.LaMusic.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.LaMusic.entity.Order;

public interface OrderRepository extends JpaRepository<Order, Long> {
	List<Order> findByUserId(Long userId);
}
