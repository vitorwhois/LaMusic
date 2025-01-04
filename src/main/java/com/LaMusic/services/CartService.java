package com.LaMusic.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.LaMusic.controllers.dto.LoginDto;
import com.LaMusic.entity.Cart;
import com.LaMusic.entity.CartItem;
import com.LaMusic.entity.Product;
import com.LaMusic.entity.User;
import com.LaMusic.repositories.CartItemRepository;
import com.LaMusic.repositories.CartRepository;
import com.LaMusic.repositories.ProductRepository;
import com.LaMusic.repositories.UserRepository;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;

@AllArgsConstructor
@Service
public class CartService {
	
	@Autowired
    private CartRepository cartRepository;

    @Autowired
    private CartItemRepository cartItemRepository;

    @Autowired
    private  ProductRepository productRepository;
    
    @Autowired
    private UserService userService;
	

    @Transactional
	public Cart addToCart (Long userId, Long productId, Integer quantity) {
    	Cart cart = FindCartByUserIdOrCreateCart(userId);

		Product product = productRepository.findById(productId)
				.orElseThrow(() -> new RuntimeException("Product not Found"));
		
		CartItem item = cart.getItens().stream()
				.filter(cartItem -> cartItem.getProduct().getId().equals(productId))
				.findFirst()
				.orElseGet(() ->{  CartItem newItem = new CartItem();
                newItem.setCart(cart);
                newItem.setProduct(product);
                newItem.setQuantity(0);
                newItem.setPrice(product.getPrice());
                cart.getItens().add(newItem);
                return newItem; // Adiciona o item ao carrinho
				});
		
		 item.setQuantity(item.getQuantity() + quantity);	        
	        cartItemRepository.save(item);
	        return cartRepository.save(cart);	    
	}
	
	public void clearCart(Long userId) {
        Cart cart = cartRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Carrinho não encontrado"));
        cartItemRepository.deleteAll(cart.getItens());
    }

	public List<CartItem> getCartItemsByCartId(Long cartId) {
		
		return cartItemRepository.findByCart_Id(cartId);
	}
	
	public Cart FindCartByUserIdOrCreateCart(Long userId) {
		User user = userService.findById(userId);		
		Cart cart = cartRepository.findByUserId(userId)
				.orElseGet(() -> {
				Cart newCart = new Cart();
				newCart.setUser(user);
				return cartRepository.save(newCart);
				});		
		return cart;
	}
	
	public Cart createCartForUser(User user) {
		 Cart cart =new Cart();
		 cart.setUser(user);
		 cartRepository.save(cart);
		 return cart;
	}
	
		
	
}
