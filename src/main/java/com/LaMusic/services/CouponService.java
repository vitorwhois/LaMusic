package com.LaMusic.services;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.LaMusic.Mappers.CouponMapper;
import com.LaMusic.dto.CouponDTO;
import com.LaMusic.entity.Coupon;
import com.LaMusic.repositories.CouponRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CouponService {

    private final CouponRepository repository;
    private final CouponMapper mapper;

    public List<CouponDTO> findAll() {
        return repository.findAll()
                .stream()
                .map(mapper::toDTO)
                .toList();
    }

    public CouponDTO findById(UUID id) {
        Coupon coupon = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cupom não encontrado"));
        return mapper.toDTO(coupon);
    }

    public CouponDTO create(CouponDTO dto) {
        Coupon coupon = mapper.toEntity(dto);
        coupon.setCreatedAt(OffsetDateTime.now());
        coupon.setUpdatedAt(OffsetDateTime.now());
        return mapper.toDTO(repository.save(coupon));
    }

    public CouponDTO update(UUID id, CouponDTO dto) {
        Coupon existing = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cupom não encontrado"));

        existing.setCode(dto.getCode());
        existing.setType(dto.getType());
        existing.setValue(dto.getValue());
        existing.setMinOrderAmount(dto.getMinOrderAmount());
        existing.setMaxDiscountAmount(dto.getMaxDiscountAmount());
        existing.setUsageLimit(dto.getUsageLimit());
        existing.setUsedCount(dto.getUsedCount());
        existing.setActive(dto.isActive());
        existing.setValidFrom(dto.getValidFrom());
        existing.setValidUntil(dto.getValidUntil());
        existing.setUpdatedAt(OffsetDateTime.now());

        return mapper.toDTO(repository.save(existing));
    }

    public void delete(UUID id) {
        repository.deleteById(id);
    }
}
