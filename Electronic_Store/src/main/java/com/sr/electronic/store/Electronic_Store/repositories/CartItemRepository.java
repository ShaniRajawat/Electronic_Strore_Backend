package com.sr.electronic.store.Electronic_Store.repositories;

import com.sr.electronic.store.Electronic_Store.entities.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartItemRepository extends JpaRepository<CartItem,Integer> {

}
