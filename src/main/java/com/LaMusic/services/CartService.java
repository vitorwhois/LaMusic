package com.LaMusic.services;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.LaMusic.entity.Cart;
import com.LaMusic.entity.CartItem;
import com.LaMusic.entity.Product;
import com.LaMusic.entity.User;
import com.LaMusic.repositories.CartItemRepository;
import com.LaMusic.repositories.CartRepository;
import com.LaMusic.repositories.ProductRepository;

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
	public Cart addToCart (UUID userId, UUID productId, Integer quantity) {
    	Cart cart = FindCartByUserIdOrCreateCart(userId);

		Product product = productRepository.findById(productId)
				.orElseThrow(() -> new RuntimeException("Product not Found"));
		
		CartItem item = cart.getItems().stream()
				.filter(cartItem -> cartItem.getProduct().getId().equals(productId))
				.findFirst()
				.orElseGet(() ->{  CartItem newItem = new CartItem();
                newItem.setCart(cart);
                newItem.setProduct(product);
                newItem.setQuantity(0);
                newItem.setPrice(product.getPrice());
                cart.getItems().add(newItem);
                return newItem; // Adiciona o item ao carrinho
				});
		
		 item.setQuantity(item.getQuantity() + quantity);	        
	        cartItemRepository.save(item);
	        return cartRepository.save(cart);	    
	}
	
	public void clearCart(UUID userId) {
        Cart cart = cartRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Carrinho não encontrado"));
        cartItemRepository.deleteAll(cart.getItems());
    }

	public List<CartItem> getCartItemsByCartId(UUID cartId) {
		
		return cartItemRepository.findByCart_Id(cartId);
	}
	
	public Cart FindCartByUserIdOrCreateCart(UUID userId) {
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

	public Cart FindCartByUserId(UUID userId) {
		return cartRepository.findByUserId(userId)
		.orElseThrow(() -> new RuntimeException("User not Found"));
	}

	public void deleteCart(Cart cart) {
		cartRepository.delete(cart);		
	}
	
		
	
}
