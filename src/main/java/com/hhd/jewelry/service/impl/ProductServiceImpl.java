package com.hhd.jewelry.service.impl;

import com.hhd.jewelry.entity.Product;
import com.hhd.jewelry.repository.ProductRepository;
import com.hhd.jewelry.service.ProductService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

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
        return productRepository.findByName(productName).orElse(null);
    }

    @Override
    public Product getProductBySerialNumber(String serialNumber) {
        return productRepository.findBySerialNumber(serialNumber).orElse(null);
    }

    @Override
    public List<Product> getProductsByCategoryName(String categoryName) {
        return productRepository.findAllByCategory_Name(categoryName);
    }

    @Override
    public List<Product> getProductsByCollectionName(String collectionName) {
        return productRepository.findAllByCollection_Name(collectionName);
    }

    @Override
    public List<Product> getProductsByCategoryNameAndCollectionName(String categoryName, String collectionName) {
        return productRepository.findAllByCategory_NameAndCollection_Name(categoryName, collectionName);
    }

    @Override
    public List<Product> getProductsByPriceBetween(Integer minPrice, Integer maxPrice) {
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

    @Override
    public void save(Product product) {
        Optional<Product> existingProduct = productRepository.findBySerialNumber(product.getSerialNumber());

        if (existingProduct.isPresent()) {
            Product existing = existingProduct.get();

            existing.setName(product.getName());
            existing.setGemstone(product.getGemstone());
            existing.setMaterial(product.getMaterial());
            existing.setBrand(product.getBrand());
            existing.setPrice(product.getPrice());
            existing.setDiscount(product.getDiscount());
            existing.setOrder(product.getOrder());
            existing.setGender(product.getGender());
            existing.setCategory(product.getCategory());
            existing.setCollection(product.getCollection());
            existing.setStockQuantity(product.getStockQuantity());
            existing.setImageUrls(product.getImageUrls());

            productRepository.save(existing);
        }
        else {
            productRepository.save(product);
        }
    }

    @Override
    public void delete(Product product) {
        productRepository.delete(product);
    }

    @Override
    public void deleteAll() {
        productRepository.deleteAll();
    }

    @Override
    public void resetAutoIncrement() {
        productRepository.resetAutoIncrement();
    }
}
