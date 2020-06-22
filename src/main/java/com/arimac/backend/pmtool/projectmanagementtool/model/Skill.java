package com.arimac.backend.pmtool.projectmanagementtool.model;

import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

public class Skill implements RowMapper<Skill> {
    private String skillId;
    private String categoryId;
    private String skillName;
    private String skillCreator;
    private Timestamp skillCreatedAt;
    private boolean isDeleted;

    public Skill() {
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

    public String getSkillCreator() {
        return skillCreator;
    }

    public void setSkillCreator(String skillCreator) {
        this.skillCreator = skillCreator;
    }

    public Timestamp getSkillCreatedAt() {
        return skillCreatedAt;
    }

    public void setSkillCreatedAt(Timestamp skillCreatedAt) {
        this.skillCreatedAt = skillCreatedAt;
    }

    public boolean isDeleted() {
        return isDeleted;
    }

    public void setDeleted(boolean deleted) {
        isDeleted = deleted;
    }

    public Skill(String skillId, String categoryId, String skillName, String skillCreator, Timestamp skillCreatedAt, boolean isDeleted) {
        this.skillId = skillId;
        this.categoryId = categoryId;
        this.skillName = skillName;
        this.skillCreator = skillCreator;
        this.skillCreatedAt = skillCreatedAt;
        this.isDeleted = isDeleted;
    }

    @Override
    public Skill mapRow(ResultSet resultSet, int i) throws SQLException {
        return new Skill(
                resultSet.getString("skillId"),
                resultSet.getString("categoryId"),
                resultSet.getString("skillName"),
                resultSet.getString("skillCreator"),
                resultSet.getTimestamp("skillCreatedAt"),
                resultSet.getBoolean("isDeleted")
        );
    }
}
