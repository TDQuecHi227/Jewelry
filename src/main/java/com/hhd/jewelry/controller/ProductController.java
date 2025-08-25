package com.hhd.jewelry.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.*;

@Controller
public class ProductController {

    @GetMapping("/products")
    public String getProductPage(Model model) {
        // Giá trong list gốc là GIÁ GỐC (oldPrice)
        List<Map<String, Object>> products = List.of(
                Map.of(
                        "name", "Nhẫn Kim Cương",
                        "price", 15000000,
                        "imageUrl", "/images/category/Nhan.jpg",
                        "category", "nhẫn",
                        "description", "Nhẫn kim cương sang trọng, tôn vinh vẻ đẹp tinh tế."
                ),
                Map.of(
                        "name", "Vòng Tay Vàng",
                        "price", 12000000,
                        "imageUrl", "/images/category/TrangSucBac.jpg",
                        "category", "vòng",
                        "description", "Vòng tay vàng tinh xảo, phù hợp quà tặng ý nghĩa."
                ),
                Map.of(
                        "name", "Dây Chuyền Bạc",
                        "price", 2500000,
                        "imageUrl", "/images/category/DayChuyen.jpg",
                        "category", "dây chuyền",
                        "description", "Dây chuyền bạc trẻ trung, dễ phối đồ hằng ngày."
                ),
                Map.of(
                        "name", "Bông Tai Ngọc Trai",
                        "price", 3500000,
                        "imageUrl", "/images/category/BongTai.jpg",
                        "category", "bông tai",
                        "description", "Bông tai ngọc trai thanh lịch, tỏa sáng mọi khoảnh khắc."
                )
        );

        // Tạo danh sách có thêm salePrice/discountPercent
        Random random = new Random();
        List<Map<String, Object>> productsWithSale = new ArrayList<>();
        for (Map<String, Object> p : products) {
            int oldPrice = (int) p.get("price");
            int discountPercent = 5 + random.nextInt(6); // 5 → 10 %
            int salePrice = oldPrice - (oldPrice * discountPercent / 100);

            Map<String, Object> np = new HashMap<>(p);
            np.put("oldPrice", oldPrice);
            np.put("salePrice", salePrice);
            np.put("discountPercent", discountPercent);
            productsWithSale.add(np);
        }

        model.addAttribute("products", productsWithSale);
        return "client/product/show";
    }
}
