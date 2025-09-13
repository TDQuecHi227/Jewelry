package com.hhd.jewelry.service;

import com.hhd.jewelry.entity.Product;
import com.hhd.jewelry.entity.User;
import com.hhd.jewelry.entity.ViewedProduct;

import java.util.List;

public interface ViewedProductService {
    void addViewedProduct(User user, Product product);
    List<ViewedProduct> getViewedProductsByUser(User user);
}
