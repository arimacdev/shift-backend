package com.arimac.backend.pmtool.projectmanagementtool.repository;

import com.arimac.backend.pmtool.projectmanagementtool.dtos.Category.CategoryAddDto;
import com.arimac.backend.pmtool.projectmanagementtool.model.Category;

import java.util.List;

public interface CategoryRepository {
    void createCategory(Category category);
    Category getCategoryByName(String categoryName);
    List<Category> getAllCategory();
}
