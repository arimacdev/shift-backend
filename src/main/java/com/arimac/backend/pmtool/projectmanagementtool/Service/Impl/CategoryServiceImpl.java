package com.arimac.backend.pmtool.projectmanagementtool.Service.Impl;

import com.arimac.backend.pmtool.projectmanagementtool.Response.Response;
import com.arimac.backend.pmtool.projectmanagementtool.Service.CategoryService;
import com.arimac.backend.pmtool.projectmanagementtool.dtos.Category.CategoryAddDto;
import com.arimac.backend.pmtool.projectmanagementtool.enumz.ResponseMessage;
import com.arimac.backend.pmtool.projectmanagementtool.exception.ErrorMessage;
import com.arimac.backend.pmtool.projectmanagementtool.model.Category;
import com.arimac.backend.pmtool.projectmanagementtool.model.User;
import com.arimac.backend.pmtool.projectmanagementtool.repository.CategoryRepository;
import com.arimac.backend.pmtool.projectmanagementtool.repository.UserRepository;
import com.arimac.backend.pmtool.projectmanagementtool.utils.UtilsService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;
    private final UtilsService utilsService;

    public CategoryServiceImpl(CategoryRepository categoryRepository, UserRepository userRepository, UtilsService utilsService) {
        this.categoryRepository = categoryRepository;
        this.userRepository = userRepository;
        this.utilsService = utilsService;
    }

    @Override
    public Object createCategory(String userId, CategoryAddDto categoryAddDto) {
        //Check if Admin
        User admin = userRepository.getUserByUserId(userId);
        if (admin == null)
            return new ErrorMessage(ResponseMessage.USER_NOT_FOUND, HttpStatus.NOT_FOUND);
        Category checkCategory = categoryRepository.findCategoryByName(categoryAddDto.getCategoryName());
        if (checkCategory != null)
            return new ErrorMessage(ResponseMessage.CATEGORY_NAME_EXIST, HttpStatus.CONFLICT);
        Category category = new Category();
        category.setCategoryId(utilsService.getUUId());
        category.setCategoryName(categoryAddDto.getCategoryName());
        category.setCategoryCreator(userId);
        category.setCategoryCreatedAt(utilsService.getCurrentTimestamp());
        categoryRepository.createCategory(category);
        return new Response(ResponseMessage.SUCCESS, HttpStatus.OK);
    }
}
