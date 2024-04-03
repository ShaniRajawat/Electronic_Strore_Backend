package com.sr.electronic.store.Electronic_Store.entities;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "cart_item")
public class CartItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int cartItemId;
    @OneToOne
    @JoinColumn(name = "product_id")
    private Product product;
    private int quantity;
    private int totalPrice;

    //mapping cart
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cart_Id")
    private Cart cart;

}
