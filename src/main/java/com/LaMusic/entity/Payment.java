package com.LaMusic.entity;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

import com.LaMusic.util.Auditable;
import com.fasterxml.jackson.databind.JsonNode;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table( name = "tb_payments")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Payment extends Auditable{

	@Id
	@GeneratedValue
	private UUID id;
	
	@OneToOne
	@JoinColumn(name = "order_id", unique = true)
	private Order order;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal amount;

    @Column(nullable = false)
    private String method;

    @Column(nullable = false)
    private String status;

    @Column(name = "transaction_id")
    private String transactionId;

    @Column(name = "gateway_response", columnDefinition = "jsonb")
    private JsonNode gatewayResponse;

    @Column(name = "error_code")
    private String errorCode;

    @Column(name = "error_message")
    private String errorMessage;

    @Column(name = "attempt_number")
    private Integer attemptNumber;

    @Column(name = "processed_at", columnDefinition = "TIMESTAMPTZ")
    private OffsetDateTime processedAt;

    @Column(name = "created_at", columnDefinition = "TIMESTAMPTZ")
    private OffsetDateTime createdAt;
}   