package com.hhd.jwelery.service;

import com.hhd.jwelery.entity.Product;

import java.util.List;

public interface ProductService {
    List<Product> getAllProducts();
    Product getProductByName(String productName);
    List<Product> getProductsByCategoryName(String categoryName);
    List<Product> getProductsByCollectionName(String collectionName);
    List<Product> getProductsByCategoryNameAndCollectionName(String categoryName, String collectionName);
    List<Product> getProductsByPriceBetween(Long minPrice, Long maxPrice);
    List<Product> getProductsByStockQuantityGreaterThan(int stock);
    List<Product> getProductsByMaterial(String material);
    Product save(Product product);
    void delete(Product product);
}
