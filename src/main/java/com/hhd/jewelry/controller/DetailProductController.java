package com.hhd.jewelry.controller;

import com.hhd.jewelry.entity.Product;
import com.hhd.jewelry.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
@RequiredArgsConstructor
public class DetailProductController {
    private final ProductService productService;
  
    @GetMapping("/details/{serialNumber}")
    public String productDetail(@PathVariable String serialNumber, Model model) {
        Product product = productService.getProductBySerialNumber(serialNumber);
        model.addAttribute("product", product);
        return "client/product/detail";
    }
}
