package com.arimac.backend.pmtool.projectmanagementtool.Service;

import com.arimac.backend.pmtool.projectmanagementtool.dtos.Category.CategoryDto;

public interface CategoryService {
    Object createCategory(String userId, CategoryDto categoryDto);
    Object getAllCategories(String userId);
    Object getCategoryById(String userId, String categoryId);
    Object updateCategory(String userId, String categoryId, CategoryDto categoryDto);
    Object deleteCategory(String userId, String categoryId);
}
