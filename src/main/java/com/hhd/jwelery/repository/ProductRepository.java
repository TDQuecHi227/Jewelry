package com.hhd.jwelery.repository;

import com.hhd.jwelery.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findAllBy();
    Product findByName(String productName);
    List<Product> findAllByCategory_CategoryName(String categoryName);
    List<Product> findAllByCollection_Name(String collectionName);
    List<Product> findAllByCategory_CategoryNameAndCollection_Name(String categoryName, String collectionName);
    List<Product> findAllByPriceBetween(Long minPrice, Long maxPrice);
    List<Product> findAllByStockQuantityGreaterThan(int stock);
    List<Product> findAllByMaterial(String material);
}

