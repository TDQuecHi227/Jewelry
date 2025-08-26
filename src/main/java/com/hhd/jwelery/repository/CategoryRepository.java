package com.hhd.jwelery.repository;

import com.hhd.jwelery.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    Category findByCategoryName(String categoryName);
    Category save(Category category);
    void delete(Category category);
}
