package com.hhd.jewelry.service;

import com.hhd.jewelry.entity.Product;

import java.util.List;

public interface ProductService {
    List<Product> getAllProducts();
    Product getProductByName(String productName);
    Product getProductBySerialNumber(String serialNumber);
    List<Product> getProductsByCategoryName(String categoryName);
    List<Product> getProductsByCollectionName(String collectionName);
    List<Product> getProductsByCategoryNameAndCollectionName(String categoryName, String collectionName);
    List<Product> getProductsByPriceBetween(Integer minPrice, Integer maxPrice);
    List<Product> getProductsByStockQuantityGreaterThan(int stock);
    List<Product> getProductsByMaterial(String material);
    void save(Product product);
    void delete(Product product);
    void deleteAll();
    void resetAutoIncrement();
    void AddProductToCart(String email, String serialNumber);
}
