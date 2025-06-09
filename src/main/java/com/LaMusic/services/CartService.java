package com.LaMusic.services;
import java.util.List;
import java.util.UUID;
import java.util.ArrayList;

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

    @Transactional
    public Cart removeItemFromCart(UUID userId, UUID productId) {
        Cart cart = findCartByUserId(userId); // Usamos findCartByUserId para não criar um novo se não existir
                                            // ou se o usuário não tiver um carrinho, um erro será lançado.

        if (cart.getItems() == null) {
            // Se não há itens, não há nada a remover.
            // Retorna o carrinho como está (vazio de itens).
            cart.setItems(new ArrayList<>()); // Garante que não seja nulo no retorno
            return cart;
        }

        CartItem itemToRemove = cart.getItems().stream()
            .filter(item -> item.getProduct() != null && item.getProduct().getId().equals(productId))
            .findFirst()
            .orElse(null); // Não lança exceção se o item não estiver no carrinho

        if (itemToRemove != null) {
            cart.getItems().remove(itemToRemove); // Remove da coleção em memória do carrinho
            cartItemRepository.delete(itemToRemove); // Remove o item do banco de dados
            // Salvar o carrinho pode ser necessário se a remoção do item da coleção
            // não for automaticamente persistida pela remoção do CartItem (depende do mapeamento e cascade)
            // cartRepository.save(cart); // Descomente se necessário
        }
        // Mesmo que o item não tenha sido encontrado, retornamos o estado atual do carrinho.
        // Recarrega os itens para garantir que o estado retornado esteja completo e atualizado
        cart.setItems(getCartItemsByCartId(cart.getId()));
        return cart;
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
