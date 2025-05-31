package com.LaMusic.services;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.LaMusic.entity.Cart;
import com.LaMusic.entity.Order;
import com.LaMusic.entity.OrderItem;
import com.LaMusic.entity.Product;
import com.LaMusic.repositories.OrderRepository;

@Service
public class OrderService {

	@Autowired
	private OrderRepository orderRepository;

	@Autowired
	private CartService cartService;
	
	@Autowired
	private ProductService productService;
	
	public Order placeOrder(UUID userId) {
		Cart cart = cartService.FindCartByUserId(userId);
		
		Order order = new Order();
		order.setUser(cart.getUser());
		order.setOrderDate(LocalDateTime.now());
		
		List<OrderItem> orderItems = cart
				.getItems()
				.stream()
				.map(cartItem -> {
					Product product = cartItem.getProduct();
								product.setStock(product.getStock() - cartItem.getQuantity());
								productService.updateProductStock(product);								
						
						return new OrderItem(null,
						cartItem.getProduct(),
						order,
						cartItem.getQuantity(),
						cartItem.getPrice());
				})
				.collect(
						Collectors.toList());
		
		order.setItems(orderItems);
        order.setTotalAmount(orderItems.stream()
                .map(item -> item.getPrice().multiply(BigDecimal.valueOf(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add));
        		
        orderRepository.save(order);
        cartService.deleteCart(cart);

        return order;			
	}

	
	public Order placeOrder2(UUID userId) {
		var cart = cartService.FindCartByUserId(userId);
		
		Order order = new Order();
		order.setUser(cart.getUser());
		order.setOrderDate(LocalDateTime.now());
		
		List<OrderItem> items = cart
				.getItems()
				.stream()
				.map(item -> new OrderItem(
						null, 
						item.getProduct(),
						order,
						item.getQuantity(),
						item.getPrice()
				)).collect(Collectors.toList());
		
		order.setItems(items);
		order.setTotalAmount(items.
				stream()
				.map(item -> item.getPrice().multiply(BigDecimal.valueOf(item.getQuantity())))
				.reduce(BigDecimal.ZERO, BigDecimal::add));
		
		orderRepository.save(order);
		cartService.deleteCart(cart);		
		return order;
	}
	
//	public Order placeOrder3 (UUID userId) {
//		var cart = cartService.FindCartByUserId(userId);
//		
//		Order order = new Order();
//		order.setUser(cart.getUser());
//		order.setOrderDate(LocalDateTime.now());
		
//		List<OrderItem> items = cart
//				.getItems()
//				.stream()
//				.map(item -> 
//				{Product product = item.getProduct();
				
//				product.setStock(product.getStock() - item.getQuantity());
				
//				item.getProduct().setStock(item.getProduct().getStock() - item.getQuantity());
				
//			return new OrderItem(
//						null,
//						item.getProduct(),
//						order,
//						item.getQuantity(),
//						item.getPrice()
//						);
//				}).collect(Collectors.toList());
//		
//		orderRepository.save(order);
//		cartService.deleteCart(cart);
//		return order;
		
//	}
	
	public Order placeOrder4(UUID userId) {
		var cart = cartService.FindCartByUserId(userId);
		
		Order order = new Order();
		order.setUser(cart.getUser());
		order.setOrderDate(LocalDateTime.now());
		
		List <OrderItem> itemCart = cart.getItems()
				.stream()
				.map(items -> new OrderItem(
						null,
						items.getProduct(),
						order,
						items.getQuantity(),						
						items.getPrice()
						)
					).collect(Collectors.toList());
						
		
		order.setItems(itemCart);
		return order;
		
	}
	
	
	
	public List<Order> findOrdersByUserId(Long userId) {
		var orders = orderRepository.findByUserId(userId);
		return orders;
	}
	
	
}
