package com.LaMusic.controllers;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.LaMusic.dto.OrderResponseDTO;
import com.LaMusic.entity.Order;
import com.LaMusic.services.OrderService;

@RestController
@RequestMapping("/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;
    
    
    
    @PostMapping("/place")
    public ResponseEntity<OrderResponseDTO> placeOrder(@RequestParam UUID userId,@RequestParam UUID shippingAddressId,
    		@RequestParam UUID billingAddressId) {
        return ResponseEntity.ok(orderService.placeOrder(userId, shippingAddressId, billingAddressId));
    }

    // Endpoint para listar todos os pedidos de um usuário
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Order>> getOrdersByUser(@PathVariable UUID userId) {
        List<Order> orders = orderService.findOrdersByUserId(userId);
        return ResponseEntity.ok(orders);
    }
}
