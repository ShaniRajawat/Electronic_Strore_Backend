package com.sr.electronic.store.Electronic_Store.controllers;

import com.sr.electronic.store.Electronic_Store.dtos.AddItemToCartRequest;
import com.sr.electronic.store.Electronic_Store.dtos.ApiResponseMessage;
import com.sr.electronic.store.Electronic_Store.dtos.CartDto;
import com.sr.electronic.store.Electronic_Store.services.CartService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/carts")
@Tag(name = "Cart Controller",description = "This is Cart API for cart Operation!!")
public class CartController {

    @Autowired
    private CartService cartService;

    //add items to cart
    @PostMapping("/{userId}")
    public ResponseEntity<CartDto> addItemToCart(@PathVariable String userId, @RequestBody AddItemToCartRequest request){
        CartDto cartDto = cartService.addItemToCart(userId, request);
        return new ResponseEntity<>(cartDto, HttpStatus.OK);
    }

    //remove items from the cart
    @DeleteMapping("/{userId}/items/{itemId}")
    public ResponseEntity<ApiResponseMessage> removeItemFromCart(@PathVariable int itemId, @PathVariable String userId){
        cartService.removeItemFormCart(userId, itemId);
        ApiResponseMessage response = ApiResponseMessage.builder()
                .message("Item is removed")
                .status(HttpStatus.OK)
                .success(true).build();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    //clear Cart
    @DeleteMapping("/{userId}")
    public ResponseEntity<ApiResponseMessage> clearCart(@PathVariable String userId){
        cartService.clearCart(userId);
        ApiResponseMessage response = ApiResponseMessage.builder()
                .message("Cart is Cleared")
                .status(HttpStatus.OK)
                .success(true).build();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    //Get Cart
    @GetMapping("/{userId}")
    public ResponseEntity<CartDto> addItemToCart(@PathVariable String userId){
        CartDto cartDto = cartService.getCartByUser(userId);
        return new ResponseEntity<>(cartDto, HttpStatus.OK);
    }

}
