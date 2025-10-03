package com.hhd.jewelry.repository;

import com.hhd.jewelry.entity.Cart;
import com.hhd.jewelry.entity.CartItem;
import com.hhd.jewelry.entity.Product;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CartItemRepository extends JpaRepository<CartItem,Integer> {
    Optional<CartItem> findCartItemByCartAndProduct(Cart cart, Product product);
    @Transactional
    void deleteByCart_CartIdAndProduct_Id(Integer cart_id,  Integer product_id);
}
