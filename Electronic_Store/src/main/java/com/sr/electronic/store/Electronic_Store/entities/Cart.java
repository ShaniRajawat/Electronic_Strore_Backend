package com.sr.electronic.store.Electronic_Store.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
@Table(name = "cart")
public class Cart {
    @Id
    private String cartId;
    private Date createdAt;
    @OneToOne
    private User user;
    //mapping cart_Item
    @OneToMany(mappedBy = "cart",cascade = CascadeType.ALL,fetch = FetchType.EAGER,orphanRemoval = true)
    private List<CartItem> items = new ArrayList<>();

}
