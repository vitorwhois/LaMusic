package com.LaMusic.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public record OrderResponseDTO(
    UUID orderId,
    LocalDateTime orderDate,
    BigDecimal totalAmount,
    OrderAddressDTO shippingAddress,
    OrderAddressDTO billingAddress,
    List<OrderItemResponseDTO> items
) {}
