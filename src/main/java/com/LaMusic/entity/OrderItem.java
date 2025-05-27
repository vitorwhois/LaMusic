package com.LaMusic.entity;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID; // Mudança

import org.hibernate.annotations.CreationTimestamp;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "order_items") // Nome da tabela conforme ADR
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID) // Mudança
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    @Column(nullable = false)
    private Integer quantity;

    @Column(name = "unit_price", nullable = false) // Conforme ADR
    private BigDecimal unitPrice; // Preço unitário no momento da compra

    @Column(name = "total_price", nullable = false) // Conforme ADR
    private BigDecimal totalPrice; // quantity * unit_price
    
    @Column(name = "product_name_snapshot") // Conforme ADR revisado
    private String productNameSnapshot;

    @Column(name = "product_sku_snapshot") // Conforme ADR revisado
    private String productSkuSnapshot;
    
    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private OffsetDateTime createdAt;

    // Construtor original não mais necessário se usando Lombok @AllArgsConstructor
    // ou se os snapshots são setados após a criação.
    // Removi o construtor que recebia (null, product, order, quantity, price)
    // pois o ID é gerado e snapshots são adicionados.
}