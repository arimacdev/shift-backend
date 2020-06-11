package com.arimac.backend.pmtool.projectmanagementtool.model;

import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

public class Category implements RowMapper<Category> {
    private String categoryId;
    private String categoryName;
    private String categoryCreator;
    private Timestamp categoryCreatedAt;
    private boolean isDeleted;

    public Category() {
    }

    public Category(String categoryId, String categoryName, String categoryCreator, Timestamp categoryCreatedAt, boolean isDeleted) {
        this.categoryId = categoryId;
        this.categoryName = categoryName;
        this.categoryCreator = categoryCreator;
        this.categoryCreatedAt = categoryCreatedAt;
        this.isDeleted = isDeleted;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getCategoryCreator() {
        return categoryCreator;
    }

    public void setCategoryCreator(String categoryCreator) {
        this.categoryCreator = categoryCreator;
    }

    public Timestamp getCategoryCreatedAt() {
        return categoryCreatedAt;
    }

    public void setCategoryCreatedAt(Timestamp categoryCreatedAt) {
        this.categoryCreatedAt = categoryCreatedAt;
    }

    public boolean getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(boolean deleted) {
        isDeleted = deleted;
    }

    @Override
    public Category mapRow(ResultSet resultSet, int i) throws SQLException {
        return new Category(
                resultSet.getString("categoryId"),
                resultSet.getString("categoryName"),
                resultSet.getString("categoryCreator"),
                resultSet.getTimestamp("categoryCreatedAt"),
                resultSet.getBoolean("isDeleted")
        );
    }
}
