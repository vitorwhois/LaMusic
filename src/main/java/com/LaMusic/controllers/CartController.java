package com.LaMusic.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.LaMusic.controllers.dto.AddCartDto;
import com.LaMusic.entity.Cart;
import com.LaMusic.services.CartService;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@RestController
@RequestMapping("/cart")
public class CartController {

	private CartService cartService;

	@PostMapping("/add")
	public ResponseEntity<Cart> addToCart(@RequestBody AddCartDto dto) {
		return ResponseEntity.ok(cartService.addToCart(dto.userId(), dto.productId(), dto.quantity()));
	}

	@DeleteMapping("/clear/{userId}")
	public ResponseEntity<Void> clearCart(@PathVariable Long userId) {
		cartService.clearCart(userId);
		return ResponseEntity.noContent().build();
	}

}
