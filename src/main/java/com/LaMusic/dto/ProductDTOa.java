package com.LaMusic.dto;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

import lombok.Builder;
import lombok.Data;

@Data
public class ProductDTO {
    private UUID id;
    private String name;
    private String slug;
    private String description;
    private String shortDescription;
    private BigDecimal price;
    private BigDecimal comparePrice;
    private BigDecimal costPrice;
    private String sku;
    private String barcode;
    private Integer stockQuantity;
    private Integer minStockAlert;
    private BigDecimal weight;
    private BigDecimal dimensionsLength;
    private BigDecimal dimensionsWidth;
    private BigDecimal dimensionsHeight;
    private String status;
    private boolean featured;
    private String metaTitle;
    private String metaDescription;
    private OffsetDateTime createdAt;
    private OffsetDateTime updatedAt;
    private OffsetDateTime deletedAt;
    private List<UUID> categoryIds;
    private List<ProductImageDTO> images;
}
