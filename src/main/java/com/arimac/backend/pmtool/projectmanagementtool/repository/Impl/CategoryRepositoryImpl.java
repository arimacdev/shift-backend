package com.arimac.backend.pmtool.projectmanagementtool.repository.Impl;

import com.arimac.backend.pmtool.projectmanagementtool.dtos.Category.CategoryDto;
import com.arimac.backend.pmtool.projectmanagementtool.model.Category;
import com.arimac.backend.pmtool.projectmanagementtool.repository.CategoryRepository;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.sql.PreparedStatement;
import java.util.List;

@Service
public class CategoryRepositoryImpl implements CategoryRepository {

    private final JdbcTemplate jdbcTemplate;

    public CategoryRepositoryImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void createCategory(Category category) {
        jdbcTemplate.update(connection -> {
            PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO Category (categoryId, categoryName, categoryCreator, categoryCreatedAt, isDeleted) VALUES(?,?,?,?,?)");
            preparedStatement.setString(1, category.getCategoryId());
            preparedStatement.setString(2, category.getCategoryName());
            preparedStatement.setString(3, category.getCategoryCreator());
            preparedStatement.setTimestamp(4, category.getCategoryCreatedAt());
            preparedStatement.setBoolean(5, category.getIsDeleted());

            return preparedStatement;
        });
    }

    @Override
    public List<Category> getAllCategory() {
        String sql = "SELECT * FROM Category WHERE isDeleted=?";
        return jdbcTemplate.query(sql, new Category(), false);
    }

    @Override
    public Category getCategoryById(String categoryId) {
        String sql = "SELECT * FROM Category WHERE categoryId=? AND isDeleted=false";
        try {
            return jdbcTemplate.queryForObject(sql, new Category(), categoryId);
        } catch (EmptyResultDataAccessException e){
            return null;
        }
    }

    @Override
    public void updateCategory(String categoryId, CategoryDto categoryDto) {
        String sql = "UPDATE Category SET categoryName=? WHERE categoryId=?";
        jdbcTemplate.update(sql, categoryDto.getCategoryName(), categoryId);
    }

    @Override
    public void flagCategory(String categoryId) {
        String sql = "UPDATE Category SET isDeleted=true WHERE categoryId=?";
        jdbcTemplate.update(sql, categoryId);
    }

    @Override
    public Category getCategoryByName(String categoryName) {
        String sql = "SELECT * FROM Category WHERE categoryName=? AND isDeleted=false";
        try {
            return jdbcTemplate.queryForObject(sql, new Category(), categoryName);
        } catch (EmptyResultDataAccessException e){
            return null;
        }
    }
}
