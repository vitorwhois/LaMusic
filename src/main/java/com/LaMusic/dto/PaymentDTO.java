package com.LaMusic.dto;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaymentDTO {
    private UUID id;
    private UUID orderId;
    private BigDecimal amount;
    private String method;
    private String status;
    private String transactionId;
    private String errorCode;
    private String errorMessage;
    private Integer attemptNumber;
    private OffsetDateTime processedAt;
    private OffsetDateTime createdAt;
}

