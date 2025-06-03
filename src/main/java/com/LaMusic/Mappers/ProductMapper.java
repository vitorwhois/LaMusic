package com.LaMusic.Mappers;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.LaMusic.dto.ProductDTO;
import com.LaMusic.dto.ProductImageDTO;
import com.LaMusic.entity.Category;
import com.LaMusic.entity.Product;
import com.LaMusic.entity.ProductImage;

@Component
public class ProductMapper {

	public static Product toEntity(ProductDTO dto, List<Category> categories, List<ProductImageDTO> imageDTOs) {
	    Product product = Product.builder()
	            .id(dto.getId())
	            .name(dto.getName())
	            .slug(dto.getSlug())
	            .description(dto.getDescription())
	            .shortDescription(dto.getShortDescription())
	            .price(dto.getPrice())
	            .comparePrice(dto.getComparePrice())
	            .costPrice(dto.getCostPrice())
	            .sku(dto.getSku())
	            .barcode(dto.getBarcode())
	            .stockQuantity(dto.getStockQuantity())
	            .minStockAlert(dto.getMinStockAlert())
	            .weight(dto.getWeight())
	            .dimensionsLength(dto.getDimensionsLength())
	            .dimensionsWidth(dto.getDimensionsWidth())
	            .dimensionsHeight(dto.getDimensionsHeight())
	            .status(dto.getStatus())
	            .featured(dto.isFeatured())
	            .metaTitle(dto.getMetaTitle())
	            .metaDescription(dto.getMetaDescription())
	            .createdAt(dto.getCreatedAt())
	            .updatedAt(dto.getUpdatedAt())
	            .deletedAt(dto.getDeletedAt())
	            .categories(categories)
	            .build();

	    if (imageDTOs != null) {
	        List<ProductImage> images = imageDTOs.stream().map(imgDto -> ProductImage.builder()
	                .url(imgDto.getUrl())
	                .altText(imgDto.getAltText())
	                .sortOrder(imgDto.getSortOrder())
	                .isPrimary(imgDto.getIsPrimary())
	                .product(product) 
	                .build()).collect(Collectors.toList());

	        product.setImages(images);
	    }

	    return product;
	}

    public static ProductDTO toDTO(Product product) {
        ProductDTO dto = new ProductDTO();
        dto.setId(product.getId());
        dto.setName(product.getName());
        dto.setSlug(product.getSlug());
        dto.setDescription(product.getDescription());
        dto.setShortDescription(product.getShortDescription());
        dto.setPrice(product.getPrice());
        dto.setComparePrice(product.getComparePrice());
        dto.setCostPrice(product.getCostPrice());
        dto.setSku(product.getSku());
        dto.setBarcode(product.getBarcode());
        dto.setStockQuantity(product.getStockQuantity());
        dto.setMinStockAlert(product.getMinStockAlert());
        dto.setWeight(product.getWeight());
        dto.setDimensionsLength(product.getDimensionsLength());
        dto.setDimensionsWidth(product.getDimensionsWidth());
        dto.setDimensionsHeight(product.getDimensionsHeight());
        dto.setStatus(product.getStatus());
        dto.setFeatured(product.isFeatured());
        dto.setMetaTitle(product.getMetaTitle());
        dto.setMetaDescription(product.getMetaDescription());
        dto.setCreatedAt(product.getCreatedAt());
        dto.setUpdatedAt(product.getUpdatedAt());
        dto.setDeletedAt(product.getDeletedAt());
      
        List<ProductImageDTO> imgs = product.getImages()
        	    .stream()
        	    .map(img -> {
        	        ProductImageDTO imgDto = new ProductImageDTO();
//        	        imgDto.setId(img.getId());
        	        imgDto.setUrl(img.getUrl());
        	        imgDto.setAltText(img.getAltText());
        	        imgDto.setSortOrder(img.getSortOrder());
        	        imgDto.setIsPrimary(img.getIsPrimary());
        	        return imgDto;
        	    }).collect(Collectors.toList());
        	    
	    dto.setImages(imgs);
	    
        if (product.getCategories() != null) {
            dto.setCategoryIds(
                product.getCategories().stream().map(Category::getId).collect(Collectors.toList())
            );
        }

        return dto;
    }
}
