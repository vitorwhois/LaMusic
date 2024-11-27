package com.LaMusic.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.LaMusic.entity.Category;

public interface CategoryRepository extends JpaRepository <Category, Long>{

}
