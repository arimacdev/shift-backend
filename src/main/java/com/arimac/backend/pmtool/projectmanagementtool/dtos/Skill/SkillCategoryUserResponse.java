package com.arimac.backend.pmtool.projectmanagementtool.dtos.Skill;

import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class SkillCategoryUserResponse implements RowMapper<SkillCategoryUserResponse> {
    private String skillId;
    private String userId;
    private String categoryId;
    private String skillName;
    private String categoryName;
    private String categoryColorCode;
    private String firstName;
    private String lastName;
    private String profileImage;

    public SkillCategoryUserResponse() {
    }


    public SkillCategoryUserResponse(String skillId, String userId, String categoryId, String skillName, String categoryName, String categoryColorCode, String firstName, String lastName, String profileImage) {
        this.skillId = skillId;
        this.userId = userId;
        this.categoryId = categoryId;
        this.skillName = skillName;
        this.categoryName = categoryName;
        this.categoryColorCode = categoryColorCode;
        this.firstName = firstName;
        this.lastName = lastName;
        this.profileImage = profileImage;
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

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }

    @Override
    public SkillCategoryUserResponse mapRow(ResultSet resultSet, int i) throws SQLException {
        return new SkillCategoryUserResponse(
                resultSet.getString("skillId"),
                resultSet.getString("userId"),
                resultSet.getString("categoryId"),
                resultSet.getString("skillName"),
                resultSet.getString("categoryName"),
                resultSet.getString("categoryColorCode"),
                resultSet.getString("firstName"),
                resultSet.getString("lastName"),
                resultSet.getString("profileImage")
        );
    }

}