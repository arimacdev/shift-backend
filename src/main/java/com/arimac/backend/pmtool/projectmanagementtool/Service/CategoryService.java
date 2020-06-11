package com.arimac.backend.pmtool.projectmanagementtool.Service;

import com.arimac.backend.pmtool.projectmanagementtool.dtos.Category.CategoryAddDto;

public interface CategoryService {
    Object createCategory(String userId, CategoryAddDto categoryAddDto);
    Object getAllCategories(String userId);
}
