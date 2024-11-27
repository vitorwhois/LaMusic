package com.LaMusic.services;

import java.util.List;

import org.springframework.stereotype.Service;

import com.LaMusic.controllers.dto.ProductDto;
import com.LaMusic.entity.Category;
import com.LaMusic.entity.Product;
import com.LaMusic.repositories.ProductRepository;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@Service
public class ProductService {

	private final ProductRepository productRepository;
	private final CategoryService categoryService;
	
	
	public List<Product> listProducts(){
		return productRepository.findAll();
	}
	
	public Product findProductById(Long id) {
		return productRepository.findById(id)
				.orElseThrow(()-> new RuntimeException("Product not found!")) ;	
	}
	
	public Product createProduct(ProductDto productDto) {
		 List<Category> categories = categoryService.getCategoriesByIds(productDto.categoryIds());
		 Product product = productDto.toProduct(categories);
		 return productRepository.save(product);
	}
	
	public Product updateProduct(Long id, Product updatedProduct) {
        Product existingProduct = findProductById(id);
        existingProduct.setName(updatedProduct.getName());
        return productRepository.save(existingProduct);
    }

    public void deleteProduct(Long id) {
        productRepository.deleteById(id);
    }

	
}
