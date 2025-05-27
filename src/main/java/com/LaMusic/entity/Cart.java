package com.LaMusic.entity;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID; // Mudança

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.fasterxml.jackson.annotation.JsonIgnore;

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
import jakarta.persistence.ManyToOne; // Mudança de OneToOne para ManyToOne se User pode ter N carts
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

enum CartStatus {
    active,
    abandoned,
    converted
}

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "carts") // Nome da tabela conforme ADR
public class Cart {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID) // Mudança
    private UUID id;

    // Um usuário pode ter múltiplos carrinhos (ex: um ativo, outros abandonados)
    // Se for sempre 1:1 user-cart ativo, a lógica de serviço garante, mas o BD permite 1:N.
    @ManyToOne(fetch = FetchType.LAZY) // Mudança para ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "TEXT DEFAULT 'active'")
    private CartStatus status; // Conforme ADR

    @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CartItem> itens = new ArrayList<>();
    
    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private OffsetDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private OffsetDateTime updatedAt;

    // O construtor Cart(Long userId) não faz mais sentido e pode ser removido.
    // public Cart(Long userId) { id = userId; } // REMOVER
}