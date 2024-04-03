package com.sr.electronic.store.Electronic_Store.entities;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "order_item")
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int orderItemId;
    private int quantity;
    private int totalPrice;
    @OneToOne
    @JoinColumn(name = "product_id")
    private  Product product;
    @ManyToOne
    @JoinColumn(name = "order_id")
    private Order order;
}
