package com.arimac.backend.pmtool.projectmanagementtool.controller;

import com.arimac.backend.pmtool.projectmanagementtool.Response.Response;
import com.arimac.backend.pmtool.projectmanagementtool.Response.ResponseController;
import com.arimac.backend.pmtool.projectmanagementtool.Service.CategoryService;
import com.arimac.backend.pmtool.projectmanagementtool.dtos.Category.CategoryDto;
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
    public ResponseEntity<Object> createCategory(@Valid @RequestBody CategoryDto categoryDto, @RequestHeader("userId") String userId){
        logger.info("HIT - POST | createCategory /skill/category | userId: {} | dto: {}", userId, categoryDto);
        return sendResponse(categoryService.createCategory(userId, categoryDto));
    }

    @ApiOperation(value = "Get All Categories", notes = "Get All Categories")
    @ApiResponse(code = 200, message = "Success", response = Response.class)
    @GetMapping
    public ResponseEntity<Object> getAllCategories(@RequestHeader("userId") String userId){
        logger.info("HIT - GET | getAllCategories /skill/category | userId: {}", userId);
        return sendResponse(categoryService.getAllCategories(userId));
    }

    @ApiOperation(value = "Get Category By Id", notes = "Get All Categories")
    @ApiResponse(code = 200, message = "Success", response = Response.class)
    @GetMapping("/{categoryId}")
    public ResponseEntity<Object> getCategoryById(@RequestHeader("userId") String userId, @PathVariable("categoryId") String categoryId){
        logger.info("HIT - GET | getCategoryById /skill/category/<categoryId> | userId: {} | categoryId: {}", userId,categoryId);
        return sendResponse(categoryService.getCategoryById(userId, categoryId));
    }

    @ApiOperation(value = "Update Category By Id", notes = "Get All Categories")
    @ApiResponse(code = 200, message = "Success", response = Response.class)
    @PutMapping("/{categoryId}")
    public ResponseEntity<Object> updateCategory(@RequestHeader("userId") String userId, @PathVariable("categoryId") String categoryId, @RequestBody CategoryDto categoryDto){
        logger.info("HIT - PUT | updateCategory /skill/category/<categoryId> | userId: {} | categoryId: {}", userId,categoryId);
        return sendResponse(categoryService.updateCategory(userId, categoryId, categoryDto));
    }

    @ApiOperation(value = "Delete Category", notes = "Get All Categories")
    @ApiResponse(code = 200, message = "Success", response = Response.class)
    @DeleteMapping("/{categoryId}")
    public ResponseEntity<Object> deleteCategory(@RequestHeader("userId") String userId, @PathVariable("categoryId") String categoryId){
        logger.info("HIT - DELETE | deleteCategory /skill/category/<categoryId> | userId: {} | categoryId: {}", userId,categoryId);
        return sendResponse(categoryService.deleteCategory(userId, categoryId));
    }

}
