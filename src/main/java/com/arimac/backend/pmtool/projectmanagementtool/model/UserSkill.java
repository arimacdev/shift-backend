package com.arimac.backend.pmtool.projectmanagementtool.model;

import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class UserSkill implements RowMapper<UserSkill> {
    private String userId;
    private String categoryId;
    private String skillId;

    public UserSkill() {
    }

    public UserSkill(String userId, String categoryId, String skillId) {
        this.userId = userId;
        this.categoryId = categoryId;
        this.skillId = skillId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public String getSkillId() {
        return skillId;
    }

    public void setSkillId(String skillId) {
        this.skillId = skillId;
    }

    @Override
    public UserSkill mapRow(ResultSet resultSet, int i) throws SQLException {
        return new UserSkill(
                resultSet.getString("userId"),
                resultSet.getString("categoryId"),
                resultSet.getString("skillId")
        );
    }
}
