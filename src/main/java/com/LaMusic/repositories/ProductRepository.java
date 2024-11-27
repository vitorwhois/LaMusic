package com.LaMusic.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.LaMusic.entity.Product;

public interface ProductRepository extends JpaRepository <Product, Long>{

}
