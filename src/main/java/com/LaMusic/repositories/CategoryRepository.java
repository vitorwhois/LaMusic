package com.LaMusic.repositories;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.LaMusic.entity.Category;

public interface CategoryRepository extends JpaRepository <Category, UUID>{
}
