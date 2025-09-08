package com.hhd.jewelry.repository;

import com.hhd.jewelry.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findAllBy();
    Product findByName(String productName);
    List<Product> findAllByCategory_Name(String categoryName);
    List<Product> findAllByCollection_Name(String collectionName);
    List<Product> findAllByCategory_NameAndCollection_Name(String categoryName, String collectionName);
    List<Product> findAllByPriceBetween(Integer minPrice, Integer maxPrice);
    List<Product> findAllByStockQuantityGreaterThan(int stock);
    List<Product> findAllByMaterial(String material);
}

