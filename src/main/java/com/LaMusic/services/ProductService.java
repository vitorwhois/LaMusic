package com.LaMusic.services;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.LaMusic.Mappers.ProductMapper;
import com.LaMusic.dto.ProductDTO;
import com.LaMusic.entity.Category;
import com.LaMusic.entity.Product;
import com.LaMusic.repositories.CategoryRepository;
import com.LaMusic.repositories.ProductRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

    public ProductDTO create(ProductDTO dto) {
        List<Category> categories = categoryRepository.findAllById(dto.getCategoryIds());
        Product product = ProductMapper.toEntity(dto, categories, dto.getImages());
        return ProductMapper.toDTO(productRepository.save(product));
    }

    public List<ProductDTO> findAll() {
        return productRepository.findAll()
                .stream()
                .map(ProductMapper::toDTO)
                .toList();
    }

    public ProductDTO findById(UUID id) {
        return productRepository.findById(id)
                .map(ProductMapper::toDTO)
                .orElseThrow(() -> new RuntimeException("Product not found"));
    }

    public ProductDTO update(UUID id, ProductDTO dto) {
        Product existing = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        List<Category> categories = categoryRepository.findAllById(dto.getCategoryIds());
        Product updated = ProductMapper.toEntity(dto, categories, dto.getImages());
        updated.setId(existing.getId());

        return ProductMapper.toDTO(productRepository.save(updated));
    }

    public void delete(UUID id) {
        productRepository.deleteById(id);
    }

	public void updateProductStock(Product product) {
		// TODO Auto-generated method stub
		
	}
}
