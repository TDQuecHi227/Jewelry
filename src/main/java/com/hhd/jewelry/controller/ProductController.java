package com.hhd.jewelry.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.*;

@Controller
public class ProductController {
    @GetMapping("/products")
    public String getProductPage(Model model) {
        List<Map<String, Object>> products = List.of(
                Map.of("name", "Nhẫn Kim Cương", "price", 15000000, "imageUrl", "/images/category/Nhan.jpg"),
                Map.of("name", "Vòng Tay Vàng", "price", 12000000, "imageUrl", "/images/category/TrangSucBac.jpg"),
                Map.of("name", "Dây Chuyền Bạc", "price", 2500000, "imageUrl", "/images/category/DayChuyen.jpg"),
                Map.of("name", "Bông Tai Ngọc Trai", "price", 3500000, "imageUrl", "/images/category/BongTai.jpg")
        );
        Random random = new Random();
        List<Map<String, Object>> productsWithSale = new ArrayList<>();
        for (Map<String, Object> p : products) {
            int oldPrice = (int) p.get("price");
            int discountPercent = 5 + random.nextInt(6); // 5 → 10
            int newPrice = oldPrice - (oldPrice * discountPercent / 100);

            Map<String, Object> newP = new HashMap<>(p);
            newP.put("oldPrice", oldPrice);
            newP.put("salePrice", newPrice);
            newP.put("discountPercent", discountPercent);
            productsWithSale.add(newP);
        }
        model.addAttribute("products", products);
        model.addAttribute("products", productsWithSale);
return "client/product/show";
    }
}
