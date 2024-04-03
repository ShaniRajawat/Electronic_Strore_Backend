package com.sr.electronic.store.Electronic_Store.dtos;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CartItemDto {
    private int cartItemId;
    private ProductDto product;
    private int quantity;
    private int totalPrice;
}
