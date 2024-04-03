package com.sr.electronic.store.Electronic_Store.services.implementaions;

import com.sr.electronic.store.Electronic_Store.dtos.AddItemToCartRequest;
import com.sr.electronic.store.Electronic_Store.dtos.CartDto;
import com.sr.electronic.store.Electronic_Store.entities.Cart;
import com.sr.electronic.store.Electronic_Store.entities.CartItem;
import com.sr.electronic.store.Electronic_Store.entities.Product;
import com.sr.electronic.store.Electronic_Store.entities.User;
import com.sr.electronic.store.Electronic_Store.exceptions.BadApiRequestException;
import com.sr.electronic.store.Electronic_Store.exceptions.ResourceNOtFoundException;
import com.sr.electronic.store.Electronic_Store.repositories.CartItemRepository;
import com.sr.electronic.store.Electronic_Store.repositories.CartRepository;
import com.sr.electronic.store.Electronic_Store.repositories.ProductRepository;
import com.sr.electronic.store.Electronic_Store.repositories.UserRepository;
import com.sr.electronic.store.Electronic_Store.services.CartService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

@SuppressWarnings("ALL")
@Service
public class CartServiceImple implements CartService {

    @Autowired
    private CartRepository cartRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private CartItemRepository cartItemRepository;
    @Autowired
    private ModelMapper modelMapper;
    @Override
    public CartDto addItemToCart(String userId, AddItemToCartRequest request) {
        String productId = request.getProductId();
        int quantity = request.getQuantity();

        if(quantity <=0){
            throw new BadApiRequestException("Request quantity is not valid");
        }
        //fetch the Product
        Product product = productRepository.findById(productId).orElseThrow(() -> new ResourceNOtFoundException("No Product found with given Id !!"));
        //fetch the user from DB
        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNOtFoundException("No User found with given Id !!"));

        Cart cart = null;
        try {
            cart = cartRepository.findByUser(user).get();
        }catch (NoSuchElementException ex){
            cart = new Cart();
            cart.setCartId(UUID.randomUUID().toString());
            cart.setCreatedAt(new Date());
        }

        //perform Cart Operation
        //if cart already Present then update
        AtomicBoolean updated = new AtomicBoolean(false);
        List<CartItem> items = cart.getItems();
        items = items.stream().map(item -> {
            if (item.getProduct().getProductId().equals(productId)) {
                //item already present in cart
                item.setQuantity(quantity);
                item.setTotalPrice(quantity*product.getDiscountedPrice());
                updated.set(true);
            }

            return item;
        }).collect(Collectors.toList());

//        cart.setItems(updatedItems);

        //create item to Add
        if (!updated.get()){
            CartItem cartItem = CartItem.builder()
                    .quantity(quantity)
                    .totalPrice(quantity * product.getDiscountedPrice())
                    .cart(cart).product(product)
                    .build();
            cart.getItems().add(cartItem);
        }

        cart.setUser(user);
        Cart updatedCart = cartRepository.save(cart);

        return modelMapper.map(updatedCart, CartDto.class);
    }

    @Override
    public void removeItemFormCart(String userId, int cardItem) {
        //condition
        CartItem cartItem = cartItemRepository.findById(cardItem).orElseThrow(() -> new ResourceNOtFoundException("No Item Found with the Given Id"));
        cartItemRepository.delete(cartItem);
    }

    @Override
    public void clearCart(String userId) {
        //fetch the user from DB
        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNOtFoundException("No User found with given Id !!"));
        //fetch the Cart from User
        Cart cart = cartRepository.findByUser(user).orElseThrow(() -> new ResourceNOtFoundException("Cart is not found in the given User!!"));
        cart.getItems().clear();
        cartRepository.save(cart);

    }

    @Override
    public CartDto getCartByUser(String userId) {
        //fetch the user from DB
        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNOtFoundException("No User found with given Id !!"));
        //fetch the Cart from User
        Cart cart = cartRepository.findByUser(user).orElseThrow(() -> new ResourceNOtFoundException("Cart is not found in the given User!!"));

        return modelMapper.map(cart, CartDto.class);
    }
}
