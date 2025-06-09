package com.LaMusic.controllers;

import java.util.List;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.security.core.context.SecurityContextHolder;


import com.LaMusic.dto.AddCartDTO;
import com.LaMusic.entity.Cart;
import com.LaMusic.entity.CartItem;
import com.LaMusic.services.CartService;

import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/cart")
@AllArgsConstructor
public class CartController {

    private final CartService cartService;

    @PostMapping("/add")
    public ResponseEntity<Cart> addToCart(@RequestBody @Validated AddCartDTO dto) {
        if (dto.productId() == null || dto.quantity() == null) {
            throw new IllegalArgumentException("Campos obrigatórios não podem ser nulos.");
        }

        UUID userId = (UUID) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Cart cart = cartService.addToCart(userId, dto.productId(), dto.quantity());
        return ResponseEntity.ok(cart);
    }


    @GetMapping("/user")
    public ResponseEntity<Cart> getCartByUserId() {
        UUID userId = (UUID) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Cart cart = cartService.findOrCreateCartByUserId(userId);
        cart.setItems(cartService.getCartItemsByCartId(cart.getId()));
        return ResponseEntity.ok(cart);
    }

    @DeleteMapping("/clear")
    public ResponseEntity<Void> clearCart() {
        UUID userId = (UUID) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        cartService.clearCart(userId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{cartId}")
    public ResponseEntity<List<CartItem>> getCartItemsByCartId(@PathVariable UUID cartId) {
        return ResponseEntity.ok(cartService.getCartItemsByCartId(cartId));
    }
}
