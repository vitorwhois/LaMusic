package com.LaMusic.repositories;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.LaMusic.entity.Address;

public interface AddressRepository extends JpaRepository<Address,UUID> {
	List<Address> findByUserId(UUID userId);
}
