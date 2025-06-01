package com.LaMusic.repositories;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.LaMusic.entity.OrderAddress;

public interface OrderAddressRepository extends JpaRepository<OrderAddress, UUID> {

}
