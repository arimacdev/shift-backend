package com.arimac.backend.pmtool.projectmanagementtool.dtos.Skill;

import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class SkillUserResponseDto implements RowMapper<SkillUserResponseDto> {
    private String skillId;
    private String userId;
    private String categoryId;
    private String skillName;
    private String categoryName;
    private String categoryColorCode;


    public SkillUserResponseDto() {
    }

    public SkillUserResponseDto(String skillId, String userId, String categoryId, String skillName, String categoryName, String categoryColorCode) {
        this.skillId = skillId;
        this.userId = userId;
        this.categoryId = categoryId;
        this.skillName = skillName;
        this.categoryName = categoryName;
        this.categoryColorCode = categoryColorCode;
    }


    public String getSkillId() {
        return skillId;
    }

    public void setSkillId(String skillId) {
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

    public String getSkillName() {
        return skillName;
    }

    public void setSkillName(String skillName) {
        this.skillName = skillName;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getCategoryColorCode() {
        return categoryColorCode;
    }

    public void setCategoryColorCode(String categoryColorCode) {
        this.categoryColorCode = categoryColorCode;
    }


    @Override
    public SkillUserResponseDto mapRow(ResultSet resultSet, int i) throws SQLException {
        return new SkillUserResponseDto(
                resultSet.getString("skillId"),
                resultSet.getString("userId"),
                resultSet.getString("categoryId"),
                resultSet.getString("skillName"),
                resultSet.getString("categoryName"),
                resultSet.getString("categoryColorCode")
        );
    }
}
