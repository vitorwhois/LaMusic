package com.LaMusic.entity;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID; // Mudança

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.annotations.Where;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

enum ProductStatus {
    active,
    inactive,
    draft
}

@Entity
@Table(name = "products") // Nome da tabela conforme ADR
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@SQLDelete(sql = "UPDATE products SET deleted_at = NOW() WHERE id = ?")
@Where(clause = "deleted_at IS NULL")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID) // Mudança
    private UUID id;

    @Column(nullable = false)
    private String name;

    @Column(unique = true, nullable = false)
    private String slug;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(name = "short_description", columnDefinition = "TEXT")
    private String shortDescription;

    @Column(nullable = false)
    private BigDecimal price;

    @Column(name = "compare_price")
    private BigDecimal comparePrice;

    @Column(name = "cost_price")
    private BigDecimal costPrice;

    @Column(unique = true)
    private String sku;

    private String barcode;

    @Column(name = "stock_quantity", columnDefinition = "INTEGER DEFAULT 0")
    private Integer stockQuantity; // Renomeado de 'stock' para clareza com ADR

    @Column(name = "min_stock_alert", columnDefinition = "INTEGER DEFAULT 5")
    private Integer minStockAlert;

    private BigDecimal weight; // وزن em kg, ex: 8.3 (precision 8, scale 3)

    @Column(name = "dimensions_length")
    private BigDecimal dimensionsLength; // em cm

    @Column(name = "dimensions_width")
    private BigDecimal dimensionsWidth;

    @Column(name = "dimensions_height")
    private BigDecimal dimensionsHeight;

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "TEXT DEFAULT 'active'")
    private ProductStatus status;

    @Column(columnDefinition = "BOOLEAN DEFAULT false")
    private boolean featured;
    
    // Removido imageUrl, será tratado por ProductImage
    // private String imageUrl;

    @Column(name = "meta_title", columnDefinition = "TEXT")
    private String metaTitle;

    @Column(name = "meta_description", columnDefinition = "TEXT")
    private String metaDescription;

    @ManyToMany
    @JoinTable(
            name = "product_categories", // Nome da tabela de junção conforme ADR
            joinColumns = @JoinColumn(name = "product_id"),
            inverseJoinColumns = @JoinColumn(name = "category_id")
    )
    private List<Category> categories = new ArrayList<>();

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true) // Novo relacionamento
    private List<ProductImage> images = new ArrayList<>();
    
    // Relacionamento com ProductLog (se necessário carregar explicitamente)
    // @OneToMany(mappedBy = "product")
    // private List<ProductLog> logs;


    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private OffsetDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private OffsetDateTime updatedAt;

    @Column(name = "deleted_at")
    private OffsetDateTime deletedAt;
}