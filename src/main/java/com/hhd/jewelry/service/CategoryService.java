package com.hhd.jewelry.service;

import com.hhd.jewelry.entity.Category;

public interface CategoryService {
    Category getCategoryByName(String categoryName);
    Category save(Category category);
    void delete(Category category);
}
