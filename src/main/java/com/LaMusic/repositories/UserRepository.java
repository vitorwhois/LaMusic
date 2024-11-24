package com.LaMusic.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.LaMusic.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {
	
}
