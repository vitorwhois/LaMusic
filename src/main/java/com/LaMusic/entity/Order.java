package com.LaMusic.entity;

import java.math.BigDecimal;
import java.time.OffsetDateTime; // Mudança
import java.util.ArrayList;
import java.util.List;
import java.util.UUID; // Mudança

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

enum OrderStatus {
    pending,
    confirmed,
    processing,
    shipped,
    delivered,
    cancelled,
    refund_pending,
    refunded
}

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "orders") // Nome da tabela conforme ADR
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID) // Mudança
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "order_number", unique = true, nullable = false)
    private String orderNumber; // Gerado pela aplicação

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "TEXT DEFAULT 'pending'")
    private OrderStatus status;

    @Column(nullable = false)
    private BigDecimal subtotal;

    @Column(name = "shipping_cost", columnDefinition = "DECIMAL(10,2) DEFAULT 0")
    private BigDecimal shippingCost;

    @Column(name = "tax_amount", columnDefinition = "DECIMAL(10,2) DEFAULT 0")
    private BigDecimal taxAmount;

    @Column(name = "discount_amount", columnDefinition = "DECIMAL(10,2) DEFAULT 0")
    private BigDecimal discountAmount;

    @Column(name = "total_amount", nullable = false)
    private BigDecimal totalAmount;

    @Column(columnDefinition = "TEXT DEFAULT 'BRL'")
    private String currency;

    // Removido orderDate, usar createdAt
    // private LocalDateTime orderDate;

    @OneToOne(fetch = FetchType.LAZY) // Ou ManyToOne se um snapshot puder ser reusado (improvável)
    @JoinColumn(name = "shipping_address_snapshot_id") // Conforme ADR revisado
    private OrderAddress shippingAddressSnapshot;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "billing_address_snapshot_id") // Conforme ADR revisado
    private OrderAddress billingAddressSnapshot;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "coupon_id") // Conforme ADR revisado (FK para Coupon)
    private Coupon coupon;
    // Se preferir manter coupon_code como TEXT:
    // @Column(name = "coupon_code")
    // private String couponCode;


    @Column(columnDefinition = "TEXT")
    private String notes;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderItem> items = new ArrayList<>();

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true) // Novo relacionamento
    private List<Payment> payments = new ArrayList<>();
    
    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private OffsetDateTime createdAt; // Usar este como data do pedido

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private OffsetDateTime updatedAt;
}