package com.LaMusic.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public record OrderResponseDto(
    UUID orderId,
    LocalDateTime orderDate,
    BigDecimal totalAmount,
    OrderAddressDto shippingAddress,
    OrderAddressDto billingAddress,
    List<OrderItemResponseDto> items
) {}
