package com.LaMusic.services;

import java.util.List;

import org.springframework.stereotype.Service;

import com.LaMusic.controllers.dto.ProductDto;
import com.LaMusic.controllers.dto.updatedProductDtoProduct;
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
	
	public Product updateProduct(updatedProductDtoProduct updatedProductDto) {
        Product existingProduct = findProductById(updatedProductDto.id());
        existingProduct.setName(updatedProductDto.name());
        existingProduct.setStock(existingProduct.getStock() - updatedProductDto.stock());
        return productRepository.save(existingProduct);
    }

    public void deleteProduct(Long id) {
        productRepository.deleteById(id);
    }

	public void updateProductStock(Product product) {
		productRepository.save(product);		
	}

	
}
