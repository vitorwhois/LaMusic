package com.LaMusic.controllers;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.LaMusic.controllers.dto.ProductDto;
import com.LaMusic.entity.Product;
import com.LaMusic.services.ProductService;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@RestController
@RequestMapping("/categories")
public class ProductController {

	private final ProductService productService;

	@GetMapping
	public ResponseEntity<List<Product>> listCategories() {
		return ResponseEntity.ok(productService.listCategories());
	}

	@GetMapping("/{id}")
	public ResponseEntity<Product> findProductById(@PathVariable Long id){
		return ResponseEntity.ok(productService.findProductById(id));
	}
	
	@PostMapping
	public ResponseEntity<Product> createProduct(@RequestBody ProductDto productDto){
		return ResponseEntity.ok(productService.createProduct(productDto));
	}
	
	@PutMapping("/{id}")
    public ResponseEntity<Product> updateProduct(@PathVariable Long id, @RequestBody Product product) {
        return ResponseEntity.ok(productService.updateProduct(id, product));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluirCategoria(@PathVariable Long id) {
        productService.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }
	
}
