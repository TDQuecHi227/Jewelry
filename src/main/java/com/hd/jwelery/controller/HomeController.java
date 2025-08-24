package com.hd.jwelery.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;
import java.util.Map;

@Controller
public class HomeController {

    @GetMapping("/")
    public String home(Model model) {
        List<Map<String, Object>> products = List.of(
                Map.of("name", "Nhẫn Kim Cương", "price", 15000000, "imageUrl", "/images/category/Nhan.jpg"),
                Map.of("name", "Vòng Tay Vàng", "price", 12000000, "imageUrl", "/images/category/TrangSucBac.jpg"),
                Map.of("name", "Dây Chuyền Bạc", "price", 2500000, "imageUrl", "/images/category/DayChuyen.jpg"),
                Map.of("name", "Bông Tai Ngọc Trai", "price", 3500000, "imageUrl", "/images/category/BongTai.jpg")
        );

        model.addAttribute("products", products);
        return "client/homepage/Home";
    }
}
