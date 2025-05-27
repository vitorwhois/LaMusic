package com.LaMusic.repositories;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.LaMusic.entity.Payment;

public interface PaymentRepository extends JpaRepository<Payment, UUID>{
	
}
