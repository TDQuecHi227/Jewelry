package com.hhd.jewelry.repository;

import com.hhd.jewelry.entity.CartItem;
import com.hhd.jewelry.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartItemRepository extends JpaRepository<CartItem,Integer> {
    CartItem findByProduct(Product product);
}
