package com.LaMusic.services;
import java.util.List;
import java.util.UUID;
import java.util.ArrayList;
import java.util.stream.Collectors; // Logger

import org.slf4j.Logger; // Logger
import org.slf4j.LoggerFactory; // Logger
import org.springframework.stereotype.Service;

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
// @AllArgsConstructor // Remova se for adicionar o construtor manualmente para o logger ou use @RequiredArgsConstructor
public class CartService {

    private static final Logger logger = LoggerFactory.getLogger(CartService.class); // Adicionar logger

    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final ProductRepository productRepository;
    private final UserService userService;

    // Adicionar construtor se removeu @AllArgsConstructor ou se @RequiredArgsConstructor não cobre todos os campos
    public CartService(CartRepository cartRepository, CartItemRepository cartItemRepository,
                       ProductRepository productRepository, UserService userService) {
        this.cartRepository = cartRepository;
        this.cartItemRepository = cartItemRepository;
        this.productRepository = productRepository;
        this.userService = userService;
    }

    @Transactional
    public Cart addToCart(UUID userId, UUID productId, Integer quantity) {
        logger.info("addToCart - Início: userId={}, productId={}, quantity={}", userId, productId, quantity);

        Cart cart = findOrCreateCartByUserId(userId);
        logger.debug("addToCart - Carrinho obtido/criado: cartId={}, userId={}, itemsCount={}", 
            cart.getId(), cart.getUser().getId(), cart.getItems() != null ? cart.getItems().size() : "null");

        Product product = productRepository.findById(productId)
            .orElseThrow(() -> {
                logger.error("addToCart - Produto não encontrado: productId={}", productId);
                return new RuntimeException("Produto não encontrado: " + productId);
            });
        logger.debug("addToCart - Produto encontrado: productId={}, productName='{}'", product.getId(), product.getName());

        CartItem item = cart.getItems().stream()
            .filter(ci -> ci.getProduct() != null && ci.getProduct().getId().equals(productId))
            .findFirst()
            .orElseGet(() -> {
                logger.info("addToCart - Criando novo CartItem para productId={} no cartId={}", productId, cart.getId());
                CartItem newItem = new CartItem();
                newItem.setCart(cart);
                newItem.setProduct(product);
                newItem.setPrice(product.getPrice());
                cart.getItems().add(newItem);
                return newItem;
            });

        logger.debug("addToCart - CartItem antes da atualização da quantidade: itemId={}, productId={}, currentQuantity={}",
            item.getId(), item.getProduct().getId(), item.getQuantity());
        
        item.setQuantity(quantity);
        logger.info("addToCart - CartItem atualizado: itemId={}, productId={}, newQuantity={}", 
            item.getId(), item.getProduct().getId(), item.getQuantity());

        if (item.getQuantity() <= 0) {
            logger.info("addToCart - Quantidade <= 0, removendo CartItem: itemId={}, productId={}", item.getId(), productId);
            cart.getItems().remove(item);
            if (item.getId() != null) {
                cartItemRepository.delete(item);
                logger.debug("addToCart - CartItem deletado do repositório: itemId={}", item.getId());
            }
        } else {
            CartItem savedItem = cartItemRepository.save(item);
            logger.debug("addToCart - CartItem salvo: itemId={}, productId={}, quantity={}", 
                savedItem.getId(), savedItem.getProduct().getId(), savedItem.getQuantity());
        }
        
        Cart savedCart = cartRepository.save(cart);
        logger.debug("addToCart - Carrinho salvo: cartId={}", savedCart.getId());
        
        List<CartItem> finalItems = getCartItemsByCartId(savedCart.getId());
        savedCart.setItems(finalItems);
        if (logger.isDebugEnabled()) { // Log detalhado dos itens apenas se o debug estiver habilitado
            String itemsDetails = finalItems.stream()
                .map(ci -> String.format("Item[id=%s, prodId=%s, qty=%d]", ci.getId(), ci.getProduct().getId(), ci.getQuantity()))
                .collect(Collectors.joining(", "));
            logger.debug("addToCart - Itens finais no carrinho: cartId={}, items=[{}]", savedCart.getId(), itemsDetails);
        }
        logger.info("addToCart - Fim: cartId={}, userId={}", savedCart.getId(), userId);
        return savedCart;
    }

    @Transactional
    public Cart removeItemFromCart(UUID userId, UUID productId) {
        logger.info("removeItemFromCart - Início: userId={}, productId={}", userId, productId);
        Cart cart = findCartByUserId(userId);
        logger.debug("removeItemFromCart - Carrinho encontrado: cartId={}", cart.getId());

        CartItem itemToRemove = cart.getItems().stream()
            .filter(item -> item.getProduct() != null && item.getProduct().getId().equals(productId))
            .findFirst()
            .orElse(null); 

        if (itemToRemove != null) {
            logger.info("removeItemFromCart - Removendo CartItem: itemId={}, productId={} do cartId={}", 
                itemToRemove.getId(), productId, cart.getId());
            cart.getItems().remove(itemToRemove); 
            cartItemRepository.delete(itemToRemove);
            logger.debug("removeItemFromCart - CartItem deletado do repositório: itemId={}", itemToRemove.getId());
        } else {
            logger.warn("removeItemFromCart - Item não encontrado no carrinho para remoção: productId={}, cartId={}", 
                productId, cart.getId());
        }
        
        List<CartItem> finalItems = getCartItemsByCartId(cart.getId());
        cart.setItems(finalItems);
        logger.info("removeItemFromCart - Fim: cartId={}, userId={}", cart.getId(), userId);
        return cart;
    }

    @Transactional
    public void clearCart(UUID userId) {
        logger.info("clearCart - Início: userId={}", userId);
        Cart cart = findCartByUserId(userId);
        logger.debug("clearCart - Carrinho encontrado: cartId={}", cart.getId());
        
        if (cart.getItems() != null && !cart.getItems().isEmpty()) {
            logger.info("clearCart - Removendo {} itens do cartId={}", cart.getItems().size(), cart.getId());
            cartItemRepository.deleteAll(cart.getItems()); 
            cart.getItems().clear(); 
            logger.debug("clearCart - Itens deletados e coleção em memória limpa.");
        } else {
            logger.info("clearCart - Carrinho já estava vazio ou sem itens: cartId={}", cart.getId());
        }
        // cartRepository.save(cart); // Considerar se é necessário salvar o carrinho após limpar os itens
        logger.info("clearCart - Fim: userId={}", userId);
    }

    public List<CartItem> getCartItemsByCartId(UUID cartId) {
        logger.debug("getCartItemsByCartId - Buscando itens para cartId={}", cartId);
        List<CartItem> items = cartItemRepository.findByCart_Id(cartId);
        logger.debug("getCartItemsByCartId - Encontrados {} itens para cartId={}", items.size(), cartId);
        return items;
    }

    public Cart findOrCreateCartByUserId(UUID userId) {
        logger.debug("findOrCreateCartByUserId - Início: userId={}", userId);
        Cart cart = cartRepository.findByUserId(userId)
            .orElseGet(() -> { 
                logger.info("findOrCreateCartByUserId - Carrinho não encontrado para userId={}, criando novo.", userId);
                User user = userService.findById(userId); // Pode lançar exceção se usuário não existir
                Cart newCart = new Cart();
                newCart.setUser(user);
                newCart.setStatus("active");
                newCart.setItems(new ArrayList<>()); 
                Cart savedNewCart = cartRepository.save(newCart);
                logger.info("findOrCreateCartByUserId - Novo carrinho criado e salvo: cartId={}, userId={}", 
                    savedNewCart.getId(), userId);
                return savedNewCart;
            });

        if (cart.getItems() == null) {
            logger.warn("findOrCreateCartByUserId - Coleção de itens era nula para cartId={}, inicializando.", cart.getId());
            cart.setItems(new ArrayList<>());
        }
        
        // Carrega/recarrega os itens.
        logger.debug("findOrCreateCartByUserId - Carregando itens para cartId={}", cart.getId());
        List<CartItem> items = getCartItemsByCartId(cart.getId());
        cart.setItems(items != null ? items : new ArrayList<>()); 
        logger.debug("findOrCreateCartByUserId - Fim: cartId={}, userId={}, itemsCount={}", 
            cart.getId(), userId, cart.getItems().size());
        return cart;
    }

    public Cart findCartByUserId(UUID userId) {
        logger.debug("findCartByUserId - Início: userId={}", userId);
        Cart cart = cartRepository.findByUserId(userId)
            .orElseThrow(() -> {
                logger.error("findCartByUserId - Carrinho não encontrado para userId={}", userId);
                return new RuntimeException("Carrinho não encontrado para o usuário: " + userId);
            });
        
        if (cart.getItems() == null) {
            logger.warn("findCartByUserId - Coleção de itens era nula para cartId={}, inicializando.", cart.getId());
            cart.setItems(new ArrayList<>());
        }
        logger.debug("findCartByUserId - Carregando itens para cartId={}", cart.getId());
        List<CartItem> items = getCartItemsByCartId(cart.getId());
        cart.setItems(items != null ? items : new ArrayList<>());
        logger.debug("findCartByUserId - Fim: cartId={}, userId={}, itemsCount={}", 
            cart.getId(), userId, cart.getItems().size());
        return cart;
    }

     @Transactional
    public void deleteCart(Cart cart) {
        logger.info("deleteCart - Início: cartId={}", cart.getId());
        // É crucial deletar os CartItems associados antes de deletar o Cart
        // para evitar erros de constraint de chave estrangeira, a menos que
        // o cascade esteja configurado como CascadeType.ALL ou CascadeType.REMOVE
        // na relação Cart -> CartItem.
        List<CartItem> items = getCartItemsByCartId(cart.getId());
        if (items != null && !items.isEmpty()) {
            logger.debug("deleteCart - Deletando {} itens do cartId={}", items.size(), cart.getId());
            cartItemRepository.deleteAll(items);
        } else {
            logger.debug("deleteCart - Nenhum item encontrado para deletar no cartId={}", cart.getId());
        }
        
        cartRepository.delete(cart);
        logger.info("deleteCart - Fim: Carrinho deletado com sucesso: cartId={}", cart.getId());
    }
}