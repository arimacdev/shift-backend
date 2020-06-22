package com.arimac.backend.pmtool.projectmanagementtool.Service;

import com.arimac.backend.pmtool.projectmanagementtool.dtos.Category.CategoryDto;
import com.arimac.backend.pmtool.projectmanagementtool.dtos.Category.CategoryEditDto;

public interface CategoryService {
    Object createCategory(String userId, CategoryDto categoryDto);
    Object getAllCategories(String userId);
    Object getCategoryById(String userId, String categoryId);
    Object updateCategory(String userId, String categoryId, CategoryEditDto categoryDto);
    Object deleteCategory(String userId, String categoryId);
}
