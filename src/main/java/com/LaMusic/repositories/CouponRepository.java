package com.LaMusic.repositories;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.LaMusic.entity.Coupon;

public interface CouponRepository extends JpaRepository<Coupon, UUID>{

}
