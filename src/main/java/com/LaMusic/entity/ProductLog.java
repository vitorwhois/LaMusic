package com.LaMusic.entity;

import java.time.OffsetDateTime;
import java.util.UUID;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.Type;

import com.fasterxml.jackson.annotation.JsonIgnore;

import io.hypersistence.utils.hibernate.type.json.JsonType;
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
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

enum ProductAction {
    created,
    updated,
    deleted, // Soft delete
    stock_changed
}

@Entity
@Table(name = "product_logs")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ProductLog {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ProductAction action;

    @Type(JsonType.class)
    @Column(name = "old_values", columnDefinition = "jsonb")
    private String oldValues; // JSON String

    @Type(JsonType.class)
    @Column(name = "new_values", columnDefinition = "jsonb")
    private String newValues; // JSON String

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id") // Usuário que realizou a ação (pode ser nulo para ações do sistema)
    private User responsibleUser;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private OffsetDateTime createdAt;
}