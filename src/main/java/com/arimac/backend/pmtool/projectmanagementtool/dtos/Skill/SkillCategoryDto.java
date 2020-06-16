package com.arimac.backend.pmtool.projectmanagementtool.dtos.Skill;

import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class SkillCategoryDto implements RowMapper<SkillCategoryDto> {
    private String skillId;
    private String categoryId;
    private String skillName;
    private String categoryName;
    private String categoryColorCode;

    public SkillCategoryDto() {
    }

    public SkillCategoryDto(String skillId, String categoryId, String skillName, String categoryName, String categoryColorCode) {
        this.skillId = skillId;
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
    public SkillCategoryDto mapRow(ResultSet resultSet, int i) throws SQLException {
        return new SkillCategoryDto(
                resultSet.getString("skillId"),
                resultSet.getString("categoryId"),
                resultSet.getString("skillName"),
                resultSet.getString("categoryName"),
                resultSet.getString("categoryColorCode")
        );
    }
}
