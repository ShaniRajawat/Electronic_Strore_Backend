package com.sr.electronic.store.Electronic_Store.repositories;

import com.sr.electronic.store.Electronic_Store.entities.Order;
import com.sr.electronic.store.Electronic_Store.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, String> {
    List<Order> findByUser(User user);
}
