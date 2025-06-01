package com.LaMusic.repositories;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.LaMusic.entity.ProductImage;

public interface ProductImageRepository extends JpaRepository<ProductImage, UUID>{

}
