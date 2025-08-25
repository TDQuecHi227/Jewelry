package com.hd.jwelery.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

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

        // Tạo list mới có thêm salePrice và discount
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
        // Danh sách ngày flash sale
        LocalDate start = LocalDate.now();
        List<Map<String, Object>> days = new ArrayList<>();
        for (int i = 0; i < 7; i++) {
            LocalDate date = start.plusDays(i);
            LocalDateTime time = date.atTime(12, 0);

            Map<String, Object> item = new HashMap<>();
            item.put("label", time.format(DateTimeFormatter.ofPattern("dd/MM")));
            item.put("datetime", time.toString());
            item.put("today", i == 0);
            days.add(item);
        }
        List<Map<String,Object>> flashDays = days.subList(0, Math.min(5, days.size()));
        model.addAttribute("flashDays", flashDays);
        model.addAttribute("products", productsWithSale);
        model.addAttribute("days", days);

        return "client/homepage/Home"; // chỉ 1 file
    }
    }


