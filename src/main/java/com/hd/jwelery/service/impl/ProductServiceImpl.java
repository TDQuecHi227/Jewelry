package com.hd.jwelery.service.impl;

import com.hd.jwelery.entity.Product;
import com.hd.jwelery.repository.ProductRepository;
import com.hd.jwelery.service.ProductService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;

    public ProductServiceImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }


    @Override
    public List<Product> getAllProducts() {
        return productRepository.findAllBy();
    }

    @Override
    public Product getProductByName(String productName) {
        return productRepository.findByName(productName);
    }

    @Override
    public List<Product> getProductsByCategoryName(String categoryName) {
        return productRepository.findAllByCategory_CategoryName(categoryName);
    }

    @Override
    public List<Product> getProductsByCollectionName(String collectionName) {
        return productRepository.findAllByCollection_Name(collectionName);
    }

    @Override
    public List<Product> getProductsByCategoryNameAndCollectionName(String categoryName, String collectionName) {
        return productRepository.findAllByCategory_CategoryNameAndCollection_Name(categoryName, collectionName);
    }

    @Override
    public List<Product> getProductsByPriceBetween(Long minPrice, Long maxPrice) {
        return productRepository.findAllByPriceBetween(minPrice, maxPrice);
    }

    @Override
    public List<Product> getProductsByStockQuantityGreaterThan(int stock) {
        return productRepository.findAllByStockQuantityGreaterThan(stock);
    }

    @Override
    public List<Product> getProductsByMaterial(String material) {
        return productRepository.findAllByMaterial(material);
    }
}
