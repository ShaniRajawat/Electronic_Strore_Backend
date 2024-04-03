package com.sr.electronic.store.Electronic_Store.services;

import com.sr.electronic.store.Electronic_Store.dtos.AddItemToCartRequest;
import com.sr.electronic.store.Electronic_Store.dtos.CartDto;

public interface CartService {

    //add item to cart:
    //case:1 cart for user is not available: we will create the cart and them add the item
    //case:2 if cart available and the item to cart
    CartDto addItemToCart(String userId, AddItemToCartRequest request);

    //Remove item from Cart
    void removeItemFormCart(String userId, int CardItem);

    //Clear Cart(Remove all Items)
    void clearCart(String userId);

    //GetCartByUser
    CartDto getCartByUser(String userId);
}
