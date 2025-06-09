package com.LaMusic.services;
import java.util.List;
import java.util.UUID;

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

@Service
@AllArgsConstructor
public class CartService {

    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final ProductRepository productRepository;
    private final UserService userService;

    @Transactional
    public Cart addToCart(UUID userId, UUID productId, Integer quantity) {
        Cart cart = findOrCreateCartByUserId(userId);

        Product product = productRepository.findById(productId)
            .orElseThrow(() -> new RuntimeException("Produto não encontrado"));

        CartItem item = cart.getItems().stream()
            .filter(ci -> ci.getProduct().getId().equals(productId))
            .findFirst()
            .orElseGet(() -> {
                CartItem newItem = new CartItem();
                newItem.setCart(cart);
                newItem.setProduct(product);
                newItem.setQuantity(0);
                newItem.setPrice(product.getPrice());
                cart.getItems().add(newItem);
                return newItem;
            });

        item.setQuantity(item.getQuantity() + quantity);

        cartItemRepository.save(item);
        Cart savedCart = cartRepository.save(cart);
        savedCart.setItems(getCartItemsByCartId(savedCart.getId()));
        return savedCart;
    }

    public void clearCart(UUID userId) {
        Cart cart = cartRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Carrinho não encontrado"));
        cartItemRepository.deleteAll(cart.getItems());
    }

    public List<CartItem> getCartItemsByCartId(UUID cartId) {
        return cartItemRepository.findByCart_Id(cartId);
    }

    public Cart findOrCreateCartByUserId(UUID userId) {
        return cartRepository.findByUserId(userId)
            .orElseGet(() -> {
                User user = userService.findById(userId);
                Cart newCart = new Cart();
                newCart.setUser(user);
                newCart.setStatus("active");
                return cartRepository.save(newCart);
            });
    }

    public Cart findCartByUserId(UUID userId) {
        return cartRepository.findByUserId(userId)
            .orElseThrow(() -> new RuntimeException("Carrinho não encontrado"));
    }

    public void deleteCart(Cart cart) {
        cartRepository.delete(cart);
    }
}
