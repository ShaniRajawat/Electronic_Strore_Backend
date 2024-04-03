package com.sr.electronic.store.Electronic_Store.repositories;

import com.sr.electronic.store.Electronic_Store.entities.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderItemRepository extends JpaRepository<OrderItem, Integer> {

}
