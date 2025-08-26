package com.hhd.jwelery.service;

import com.hhd.jwelery.entity.Category;

public interface CategoryService {
    Category getCategoryByName(String categoryName);
    Category save(Category category);
    void delete(Category category);
}
