package com.LaMusic.repositories;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.LaMusic.entity.ProductLog;

public interface ProductLogRepository extends JpaRepository<ProductLog, UUID>{

}
