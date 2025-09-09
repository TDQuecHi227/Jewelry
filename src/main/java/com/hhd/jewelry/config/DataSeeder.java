package com.hhd.jewelry.config;

import com.hhd.jewelry.entity.Category;
import com.hhd.jewelry.service.CategoryService;
import com.hhd.jewelry.service.ProductService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataSeeder implements CommandLineRunner {
    private final CategoryService categoryService;
    private final ProductService productService;

    public DataSeeder(CategoryService categoryService, ProductService productService){
        this.categoryService = categoryService;
        this.productService = productService;
    }

    @Override
    public void run(String... args) {
        insertOrUpdate("Dây chuyền", "/images/categories/necklaces/category/DayChuyen.jpg");
        insertOrUpdate("Lắc", "/images/categories/bracelets/category/Lac.jpg");
        insertOrUpdate("Bông tai", "/images/categories/earrings/category/BongTai.jpg");
        insertOrUpdate("Nhẫn", "/images/categories/rings/category/Nhan.jpg");
        insertOrUpdate("Đồng hồ", "/images/categories/watch/category/DongHo.jpg");

        Category necklace = categoryService.getCategoryByName("Dây chuyền");
        Category bracelet = categoryService.getCategoryByName("Lắc");
        Category earring  = categoryService.getCategoryByName("Bông tai");
        Category ring     = categoryService.getCategoryByName("Nhẫn");
        Category watch    = categoryService.getCategoryByName("Đồng hồ");
    }

    private void insertOrUpdate(String categoryName, String imageUrl) {
        Category existing = categoryService.getCategoryByName(categoryName);
        if (existing == null) {
            Category c = new Category();          // dùng no-args constructor
            c.setCategoryName(categoryName);      // set các field cần thiết
            c.setImageUrl(imageUrl);
            categoryService.save(c);
            System.out.println("➕ Inserted: " + categoryName);
        } else {
            existing.setImageUrl(imageUrl);       // cập nhật nếu đã tồn tại
            categoryService.save(existing);
            System.out.println("✏️ Updated: " + categoryName);
        }
    }
}
