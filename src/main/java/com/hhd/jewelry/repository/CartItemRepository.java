package com.hhd.jewelry.repository;

import com.hhd.jewelry.entity.CartItem;
import com.hhd.jewelry.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CartItemRepository extends JpaRepository<CartItem,Integer> {
    Optional<CartItem> findByProduct(Product product);
}
