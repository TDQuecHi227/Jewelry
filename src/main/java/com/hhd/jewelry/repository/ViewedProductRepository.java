package com.hhd.jewelry.repository;

import com.hhd.jewelry.entity.Product;
import com.hhd.jewelry.entity.User;
import com.hhd.jewelry.entity.ViewedProduct;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ViewedProductRepository extends JpaRepository<ViewedProduct, Long> {
    List<ViewedProduct> findTop10ByUserOrderByIdDesc(User user);
    Optional<ViewedProduct> findByUserAndProduct(User user, Product product);
}
