package com.LaMusic.Mappers;

import org.springframework.stereotype.Component;

import com.LaMusic.dto.PaymentDTO;
import com.LaMusic.entity.Order;
import com.LaMusic.entity.Payment;
import com.LaMusic.repositories.OrderRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class PaymentMapper {

    private final OrderRepository orderRepository;

    public PaymentDTO toDTO(Payment payment) {
        return PaymentDTO.builder()
                .id(payment.getId())
                .orderId(payment.getOrder().getId())
                .amount(payment.getAmount())
                .method(payment.getMethod())
                .status(payment.getStatus())
                .transactionId(payment.getTransactionId())
                .errorCode(payment.getErrorCode())
                .errorMessage(payment.getErrorMessage())
                .attemptNumber(payment.getAttemptNumber())
                .processedAt(payment.getProcessedAt())
                .createdAt(payment.getCreatedAt())
                .build();
    }

    public Payment toEntity(PaymentDTO dto) {
        Order order = orderRepository.findById(dto.getOrderId())
                .orElseThrow(() -> new RuntimeException("Pedido não encontrado"));

        return Payment.builder()
                .id(dto.getId())
                .order(order)
                .amount(dto.getAmount())
                .method(dto.getMethod())
                .status(dto.getStatus())
                .transactionId(dto.getTransactionId())
                .errorCode(dto.getErrorCode())
                .errorMessage(dto.getErrorMessage())
                .attemptNumber(dto.getAttemptNumber())
                .processedAt(dto.getProcessedAt())
                .createdAt(dto.getCreatedAt())
                .build();
    }
}
