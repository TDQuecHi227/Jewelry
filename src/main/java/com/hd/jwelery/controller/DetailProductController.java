package com.hd.jwelery.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;
import java.util.Map;

@Controller
public class DetailProductController {
    // Hiển thị trang chi tiết sản phẩm (UI-only)
    @GetMapping("/details")
    public String productDetail(Model model) {
        Map<String, Object> product = Map.of(
                "name", "Nhẫn Cưới nam Vàng Trắng 10K Đính đá ECZ PNJ",
                "sku", "XMXMW006385",
                "price", 5_356_000, // số nguyên; Thymeleaf sẽ format thành "5.356.000 ₫"
                "images", List.of(
                        "/images/product/p1/1.png",
                        "/images/product/p1/2.jpg",
                        "/images/product/p1/3.jpg",
                        "/images/product/p1/4.png",
                        "/images/product/p1/5.png"
                ),
                "sold", 6,
                "material", "Vàng trắng 10K",
                "stone", "ECZ (Xoàn mỹ)",
                "brand", "PNJ",
                "gender", "Nam",
                // template sẽ cộng thêm " ₫/tháng"
                "monthlyPayment", "447.584"
        );

        model.addAttribute("product", product);
        return "client/product/detail"; // -> src/main/resources/templates/product/detail.html
    }
}
