package com.hhd.jewelry.controller;

import com.hhd.jewelry.entity.Product;
import com.hhd.jewelry.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;
import java.util.Map;

@Controller
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;
  
    @GetMapping("/products")
    public String getProductPage(Model model) {
        List<Product> products = productService.getAllProducts();
        model.addAttribute("products", products);
        return "client/product/show";
    }
}
