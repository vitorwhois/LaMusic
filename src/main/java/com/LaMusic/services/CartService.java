package com.LaMusic.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.LaMusic.entity.Cart;
import com.LaMusic.entity.CartItem;
import com.LaMusic.entity.Product;
import com.LaMusic.entity.User;
import com.LaMusic.repositories.CartItemRepository;
import com.LaMusic.repositories.CartRepository;
import com.LaMusic.repositories.ProductRepository;
import com.LaMusic.repositories.UserRepository;

import jakarta.transaction.Transactional;

@Service
public class CartService {
	
	@Autowired
    private CartRepository cartRepository;

    @Autowired
    private CartItemRepository cartItemRepository;

    @Autowired
    private  ProductRepository productRepository;
    
    @Autowired
    private UserRepository userRepository;
	

    @Transactional
	public Cart addToCart (Long userId, Long productId, Integer quantity) {

    	 User user = userRepository.findById(userId)
    	            .orElseThrow(() -> new RuntimeException("User not found"));
    	
    	 Cart cart = cartRepository.findByUserId(userId)
    	            .orElseGet(() -> {
    	                Cart newCart = new Cart();
    	                newCart.setUser(user); // Define o usuário no carrinho
    	                return cartRepository.save(newCart); // Salva o carrinho novo
    	            });
		
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
//		
//		item.setQuantity(item.getQuantity() + quantity);
//		cartItemRepository.save(item);
//		return cartRepository.save(cart);	
		
		 item.setQuantity(item.getQuantity() + quantity);

	        // Salva o item no repositório
	        cartItemRepository.save(item);

	        // Salva o carrinho atualizado
	        return cartRepository.save(cart);
	    
	}
	
	public void clearCart(Long userId) {
        Cart cart = cartRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Carrinho não encontrado"));
        cartItemRepository.deleteAll(cart.getItens());
    }
	
	
	
	
	
	
	
}
