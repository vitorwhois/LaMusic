package com.LaMusic.controllers;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.LaMusic.dto.AddCartDto;
import com.LaMusic.entity.Cart;
import com.LaMusic.entity.CartItem;
import com.LaMusic.services.CartService;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@RestController
@RequestMapping("/cart")
public class CartController {

	@Autowired
	private CartService cartService;

	@PostMapping("/add")
	public ResponseEntity<Cart> addToCart(@RequestBody AddCartDto dto) {
		 if (dto.productId() == null) {
		        throw new IllegalArgumentException("O ID do produto não pode ser nulo.");
		    }
		return ResponseEntity.ok(cartService.addToCart(dto.userId(), dto.productId(), dto.quantity()));
	}

	@DeleteMapping("/clear/{userId}")
	public ResponseEntity<Void> clearCart(@PathVariable UUID userId) {
		cartService.clearCart(userId);
		return ResponseEntity.noContent().build();
	}	
	    
	@GetMapping("/{cartId}")
	public ResponseEntity<List<CartItem>> getCartItemsByCartId(@PathVariable Long cartId){
		List<CartItem> items = cartService.getCartItemsByCartId(cartId);
		return ResponseEntity.ok(items);
	}
	
	
}
