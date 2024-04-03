package com.sr.electronic.store.Electronic_Store.dtos;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class OrderItemDto {
    private int orderItemId;
    private int quantity;
    private int totalPrice;
    private ProductDto product;
}
