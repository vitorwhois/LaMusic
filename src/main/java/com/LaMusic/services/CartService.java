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

        Cart cart = findOrCreateCartByUserId(userId); // Este método agora deve retornar o carrinho com a coleção gerenciada
        // Se cart.getUser() puder ser nulo, adicione uma verificação aqui ou garanta que nunca seja nulo.
        logger.debug("addToCart - Carrinho obtido/criado: cartId={}, userId={}, itemsCount={}", 
            cart.getId(), (cart.getUser() != null ? cart.getUser().getId() : "null"), cart.getItems() != null ? cart.getItems().size() : "null");

        Product product = productRepository.findById(productId)
            .orElseThrow(() -> {
                logger.error("addToCart - Produto não encontrado: productId={}", productId);
                return new RuntimeException("Produto não encontrado: " + productId);
            });
        logger.debug("addToCart - Produto encontrado: productId={}, productName='{}'", product.getId(), product.getName());

        // Trabalhe diretamente com a coleção 'cart.getItems()'
        CartItem item = cart.getItems().stream()
            .filter(ci -> ci.getProduct() != null && ci.getProduct().getId().equals(productId))
            .findFirst()
            .orElseGet(() -> {
                logger.info("addToCart - Criando novo CartItem para productId={} no cartId={}", productId, cart.getId());
                CartItem newItem = new CartItem();
                newItem.setCart(cart); // Importante para a relação bidirecional
                newItem.setProduct(product);
                newItem.setPrice(product.getPrice());
                // Adiciona o novo item à coleção gerenciada do carrinho
                // Se a relação Cart -> CartItem for bidirecional e o Cart for o "owner" com CascadeType.ALL ou PERSIST,
                // esta adição será refletida quando o 'cart' for salvo.
                cart.getItems().add(newItem); 
                return newItem;
            });

        // Log antes de definir a quantidade
        logger.debug("addToCart - CartItem (itemId={}) antes da atualização da quantidade: currentQuantity={}",
            item.getId(), item.getQuantity());
        
        item.setQuantity(quantity);

        // Log depois de definir a quantidade
        logger.info("addToCart - CartItem (itemId={}) atualizado: newQuantity={}", 
            item.getId(), item.getQuantity());

        if (item.getQuantity() <= 0) {
            logger.info("addToCart - Quantidade <= 0, removendo CartItem: itemId={}, productId={}", item.getId(), productId);
            // Remove o item da coleção gerenciada do carrinho
            cart.getItems().remove(item);
            // Se orphanRemoval=true estiver no Cart para a coleção items,
            // o Hibernate deletará o CartItem quando o 'cart' for salvo e o item não estiver mais na coleção.
            // Se não, você pode precisar deletar explicitamente:
            if (item.getId() != null) { // Apenas se já foi persistido
                 cartItemRepository.delete(item); // Pode ser redundante com orphanRemoval=true
            }
            logger.debug("addToCart - CartItem removido da coleção: itemId={}", item.getId());
        } else {
            // Se o item é novo (item.getId() == null), ele será persistido quando o 'cart' for salvo
            // devido ao cascade (se configurado como CascadeType.PERSIST ou ALL).
            // Se o item já existe, suas alterações (quantidade) serão salvas.
            // Salvar o item explicitamente aqui é seguro e garante a persistência se o cascade não estiver configurado para cobrir.
            CartItem savedItem = cartItemRepository.save(item);
            logger.debug("addToCart - CartItem salvo/atualizado: itemId={}, productId={}, quantity={}", 
                savedItem.getId(), savedItem.getProduct().getId(), savedItem.getQuantity());
        }
        
        // Salvar o 'cart' persistirá as mudanças na sua coleção 'items' (novos itens, itens removidos se orphanRemoval=true)
        Cart savedCart = cartRepository.save(cart);
        logger.debug("addToCart - Carrinho salvo: cartId={}", savedCart.getId());
        
        // NÃO substitua a coleção de itens aqui. A instância 'savedCart' já tem a coleção correta.
        // List<CartItem> finalItems = getCartItemsByCartId(savedCart.getId());
        // savedCart.setItems(finalItems); 

        if (logger.isDebugEnabled()) {
            // Use a coleção diretamente de savedCart.getItems()
            String itemsDetails = savedCart.getItems().stream()
                .map(ci -> String.format("Item[id=%s, prodId=%s, qty=%d]", ci.getId(), (ci.getProduct() != null ? ci.getProduct().getId() : "null"), ci.getQuantity()))
                .collect(Collectors.joining(", "));
            logger.debug("addToCart - Itens finais no carrinho: cartId={}, items=[{}]", savedCart.getId(), itemsDetails);
        }
        logger.info("addToCart - Fim: cartId={}, userId={}", savedCart.getId(), userId);
        return savedCart; // Retorna o carrinho com sua coleção gerenciada e atualizada
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
        logger.debug("getCartItemsByCartId - Encontrados {} itens para cartId={}", (items != null ? items.size() : "null"), cartId);
        return items;
    }

    public Cart findOrCreateCartByUserId(UUID userId) {
        logger.debug("findOrCreateCartByUserId - Início: userId={}", userId);
        Cart cart = cartRepository.findByUserId(userId)
            .orElseGet(() -> { 
                logger.info("findOrCreateCartByUserId - Carrinho não encontrado para userId={}, criando novo.", userId);
                User user = userService.findById(userId);
                if (user == null) { // Adicionar verificação para usuário nulo
                    logger.error("findOrCreateCartByUserId - Usuário não encontrado com ID: {}", userId);
                    throw new RuntimeException("Usuário não encontrado para criar carrinho: " + userId);
                }
                Cart newCart = new Cart();
                newCart.setUser(user);
                newCart.setStatus("active");
                newCart.setItems(new ArrayList<>()); // Inicializa com uma nova lista gerenciável
                Cart savedNewCart = cartRepository.save(newCart);
                logger.info("findOrCreateCartByUserId - Novo carrinho criado e salvo: cartId={}, userId={}", 
                    savedNewCart.getId(), userId);
                return savedNewCart; // Retorna o carrinho persistido com sua coleção gerenciada
            });

        // Se o carrinho foi encontrado, sua coleção 'items' é gerenciada pelo Hibernate.
        // Se for LAZY, será carregada ao ser acessada dentro de uma transação.
        // Não substitua a coleção. Apenas garanta que não seja nula se a entidade permitir.
        if (cart.getItems() == null) {
            // Isso não deveria acontecer se a entidade Cart sempre inicializa 'items'
            // (ex: private List<CartItem> items = new ArrayList<>(); na declaração do campo)
            // ou se o construtor o faz.
            logger.warn("findOrCreateCartByUserId - Coleção de itens era nula para cartId={}, inicializando com nova lista (pode causar problemas com orphanRemoval se esta instância de cart já era gerenciada).", cart.getId());
            cart.setItems(new ArrayList<>());
        }
        
        // Forçar o carregamento se for LAZY e você precisar dos itens aqui (opcional)
        // O simples acesso dentro de uma transação já deve carregar.
        // Ex: int itemCount = cart.getItems().size();
        
        logger.debug("findOrCreateCartByUserId - Fim: cartId={}, userId={}, itemsCount={}", 
            cart.getId(), (cart.getUser() != null ? cart.getUser().getId() : "null"), (cart.getItems() != null ? cart.getItems().size() : "null"));
        return cart; // Retorna o carrinho com sua coleção gerenciada
    }

    public Cart findCartByUserId(UUID userId) {
        logger.debug("findCartByUserId - Início: userId={}", userId);
        Cart cart = cartRepository.findByUserId(userId)
            .orElseThrow(() -> {
                logger.error("findCartByUserId - Carrinho não encontrado para userId={}", userId);
                return new RuntimeException("Carrinho não encontrado para o usuário: " + userId);
            });
        
        // Similar a findOrCreateCartByUserId, não substitua a coleção.
        // Apenas garanta que não seja nula se a entidade permitir.
        if (cart.getItems() == null) {
            logger.warn("findCartByUserId - Coleção de itens era nula para cartId={}, inicializando.", cart.getId());
            cart.setItems(new ArrayList<>());
        }
        // Forçar o carregamento se for LAZY (opcional)
        // int itemCount = cart.getItems().size();

        logger.debug("findCartByUserId - Fim: cartId={}, userId={}, itemsCount={}", 
            cart.getId(), (cart.getUser() != null ? cart.getUser().getId() : "null"), (cart.getItems() != null ? cart.getItems().size() : "null"));
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