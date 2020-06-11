package com.arimac.backend.pmtool.projectmanagementtool.repository.Impl;

import com.arimac.backend.pmtool.projectmanagementtool.dtos.Category.CategoryAddDto;
import com.arimac.backend.pmtool.projectmanagementtool.model.Category;
import com.arimac.backend.pmtool.projectmanagementtool.repository.CategoryRepository;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.sql.PreparedStatement;

@Service
public class CategoryRepositoryImpl implements CategoryRepository {

    private final JdbcTemplate jdbcTemplate;

    public CategoryRepositoryImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void createCategory(Category category) {
        jdbcTemplate.update(connection -> {
            PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO Category (categoryId, categoryName, categoryCreator, categoryCreatedAt) VALUES(?,?,?,?)");
            preparedStatement.setString(1, category.getCategoryId());
            preparedStatement.setString(2, category.getCategoryName());
            preparedStatement.setString(3, category.getCategoryCreator());
            preparedStatement.setTimestamp(4, category.getCategoryCreatedAt());

            return preparedStatement;
        });
    }

    @Override
    public Category findCategoryByName(String categoryName) {
        String sql = "SELECT * FROM Category WHERE categoryName=?";
        try {
            return jdbcTemplate.queryForObject(sql, new Category(), categoryName);
        } catch (EmptyResultDataAccessException e){
            return null;
        }
    }
}
