package com.hhd.jewelry.service.impl;

import com.hhd.jewelry.entity.Product;
import com.hhd.jewelry.entity.User;
import com.hhd.jewelry.entity.ViewedProduct;
import com.hhd.jewelry.repository.ViewedProductRepository;
import com.hhd.jewelry.service.ViewedProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ViewedProductServiceImpl implements ViewedProductService {

    private final ViewedProductRepository viewedProductRepository;

    @Override
    @Transactional
    public void addViewedProduct(User user, Product product) {
        ViewedProduct viewedProduct = viewedProductRepository
                .findByUserAndProduct(user, product)
                .orElse(ViewedProduct.builder()
                        .user(user)
                        .product(product)
                        .build());

        viewedProductRepository.save(viewedProduct);
    }

    @Override
    public List<ViewedProduct> getViewedProductsByUser(User user) {
        return viewedProductRepository.findTop10ByUserOrderByIdDesc(user);
    }
}
