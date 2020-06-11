package com.arimac.backend.pmtool.projectmanagementtool.repository;

import com.arimac.backend.pmtool.projectmanagementtool.dtos.Category.CategoryAddDto;
import com.arimac.backend.pmtool.projectmanagementtool.model.Category;

public interface CategoryRepository {
    void createCategory(Category category);
    Category findCategoryByName(String categoryName);
}
