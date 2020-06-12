package com.arimac.backend.pmtool.projectmanagementtool.Service.Impl;

import com.arimac.backend.pmtool.projectmanagementtool.Response.Response;
import com.arimac.backend.pmtool.projectmanagementtool.Service.CategoryService;
import com.arimac.backend.pmtool.projectmanagementtool.dtos.Category.CategoryDto;
import com.arimac.backend.pmtool.projectmanagementtool.dtos.Category.CategoryEditDto;
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
    public Object createCategory(String userId, CategoryDto categoryDto) {
        //Check if Admin
        User admin = userRepository.getUserByUserId(userId);
        if (admin == null)
            return new ErrorMessage(ResponseMessage.USER_NOT_FOUND, HttpStatus.NOT_FOUND);
        Category checkCategory = categoryRepository.getCategoryByName(categoryDto.getCategoryName());
        if (checkCategory != null)
            return new ErrorMessage(ResponseMessage.CATEGORY_NAME_EXIST, HttpStatus.CONFLICT);
        Category category = new Category();
        category.setCategoryId(utilsService.getUUId());
        category.setCategoryName(categoryDto.getCategoryName());
        category.setCategoryCreator(userId);
        category.setCategoryCreatedAt(utilsService.getCurrentTimestamp());
        category.setCategoryColorCode(categoryDto.getCategoryColorCode());
        categoryRepository.createCategory(category);
        return new Response(ResponseMessage.SUCCESS, HttpStatus.OK);
    }

    @Override
    public Object getAllCategories(String userId) {
        User admin = userRepository.getUserByUserId(userId);
        if (admin == null)
            return new ErrorMessage(ResponseMessage.USER_NOT_FOUND, HttpStatus.NOT_FOUND);
        return new Response(ResponseMessage.SUCCESS, HttpStatus.OK, categoryRepository.getAllCategory());
    }

    @Override
    public Object getCategoryById(String userId, String categoryId) {
        User user = userRepository.getUserByUserId(userId);
        if (user == null)
            return new ErrorMessage(ResponseMessage.USER_NOT_FOUND, HttpStatus.NOT_FOUND);
        Category category = categoryRepository.getCategoryById(categoryId);
        if (category == null)
            return new ErrorMessage(ResponseMessage.CATEGORY_NOT_FOUND, HttpStatus.NOT_FOUND);
        return new Response(ResponseMessage.SUCCESS, HttpStatus.OK, category);
    }

    @Override
    public Object updateCategory(String userId, String categoryId, CategoryEditDto categoryDto) {
        if ((categoryDto.getCategoryName() == null || categoryDto.getCategoryName().isEmpty()) && (categoryDto.getCategoryColorCode() == null || categoryDto.getCategoryColorCode().isEmpty()))
            return new ErrorMessage(ResponseMessage.INVALID_REQUEST_BODY, HttpStatus.BAD_REQUEST);
        User user = userRepository.getUserByUserId(userId);
        if (user == null)
            return new ErrorMessage(ResponseMessage.USER_NOT_FOUND, HttpStatus.NOT_FOUND);
        Category category = categoryRepository.getCategoryById(categoryId);
        if (category == null)
            return  new ErrorMessage(ResponseMessage.CATEGORY_NOT_FOUND, HttpStatus.NOT_FOUND);
        if (categoryDto.getCategoryName()!= null && !categoryDto.getCategoryName().isEmpty())
            category.setCategoryName(categoryDto.getCategoryName());
        if (categoryDto.getCategoryColorCode()!= null && !categoryDto.getCategoryColorCode().isEmpty())
            category.setCategoryColorCode(categoryDto.getCategoryColorCode());
        categoryRepository.updateCategory(categoryId, category);
        return new Response(ResponseMessage.SUCCESS, HttpStatus.OK);
    }

    @Override
    public Object deleteCategory(String userId, String categoryId) {
        User user = userRepository.getUserByUserId(userId);
        if (user == null)
            return new ErrorMessage(ResponseMessage.USER_NOT_FOUND, HttpStatus.NOT_FOUND);
        Category category = categoryRepository.getCategoryById(categoryId);
        if (category == null)
            return  new ErrorMessage(ResponseMessage.CATEGORY_NOT_FOUND, HttpStatus.NOT_FOUND);
        categoryRepository.flagCategory(categoryId);
        return new Response(ResponseMessage.SUCCESS, HttpStatus.OK);
    }
}