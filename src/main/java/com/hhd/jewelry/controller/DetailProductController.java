package com.hhd.jewelry.controller;

import com.hhd.jewelry.entity.Product;
import com.hhd.jewelry.service.ProductService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import java.util.Map;

@Controller
public class DetailProductController {
    private final ProductService productService;
    public DetailProductController(ProductService productService) {
        this.productService = productService;
    }
    // Hiển thị trang chi tiết sản phẩm (UI-only)
    @GetMapping("/details/{serialnumber}")
    public String productDetail(Model model, @PathVariable("serialnumber") String serialnumber) {
        Product product = productService.getProductBySerialNumber(serialnumber);
        model.addAttribute("product", product);
        return "client/product/detail"; // -> src/main/resources/templates/product/detail.html
    }
}
