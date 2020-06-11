package com.arimac.backend.pmtool.projectmanagementtool.controller;

import com.arimac.backend.pmtool.projectmanagementtool.Response.Response;
import com.arimac.backend.pmtool.projectmanagementtool.Response.ResponseController;
import com.arimac.backend.pmtool.projectmanagementtool.Service.CategoryService;
import com.arimac.backend.pmtool.projectmanagementtool.dtos.Category.CategoryAddDto;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/skill/category")
public class CategoryController extends ResponseController {

    private static final Logger logger = LoggerFactory.getLogger(CategoryController.class);

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @ApiOperation(value = "Category Create", notes = "Create Category for Skill Matrix")
    @ApiResponse(code = 200, message = "Success", response = Response.class)
    @PostMapping
    public ResponseEntity<Object> createCategory(@Valid @RequestBody CategoryAddDto categoryAddDto, @RequestHeader("userId") String userId){
        logger.info("HIT - POST /skill/category | userId: {} | dto: {}", userId, categoryAddDto);
        return sendResponse(categoryService.createCategory(userId, categoryAddDto));
    }

    @ApiOperation(value = "Get All Categories", notes = "Get All Categories")
    @ApiResponse(code = 200, message = "Success", response = Response.class)
    @PostMapping
    public ResponseEntity<Object> getAll(@Valid @RequestBody CategoryAddDto categoryAddDto, @RequestHeader("userId") String userId){
        logger.info("HIT - POST /skill/category | userId: {} | dto: {}", userId, categoryAddDto);
        return sendResponse(categoryService.createCategory(userId, categoryAddDto));
    }

}
