package com.sr.electronic.store.Electronic_Store.repositories;

import com.sr.electronic.store.Electronic_Store.entities.Category;
import com.sr.electronic.store.Electronic_Store.entities.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, String> {
    //search
    Page<Product> findByTitleContaining(String subtitle,Pageable pageable);
    Page<Product> findByLiveTrue(Pageable pageable);
    Page<Product> findByCategory(Category category,Pageable pageable);
    //other Methods
    //Query Method
}
