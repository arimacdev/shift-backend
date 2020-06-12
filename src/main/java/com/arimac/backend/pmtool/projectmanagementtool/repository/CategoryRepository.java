package com.arimac.backend.pmtool.projectmanagementtool.repository;

import com.arimac.backend.pmtool.projectmanagementtool.dtos.Category.CategoryDto;
import com.arimac.backend.pmtool.projectmanagementtool.model.Category;

import java.util.List;

public interface CategoryRepository {
    void createCategory(Category category);
    Category getCategoryByName(String categoryName);
    List<Category> getAllCategory();
    Category getCategoryById(String categoryId);
    void updateCategory(String categoryId, Category category);
    void flagCategory(String categoryId);
}
