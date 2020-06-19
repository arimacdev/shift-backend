package com.arimac.backend.pmtool.projectmanagementtool.dtos.Skill;

import java.util.List;

public class SkillMapUserResponse {
    private String userId;
    private String firstName;
    private String lastName;
    private String userProfileImage;
    private List<SkillCategory> category;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public List<SkillCategory> getCategory() {
        return category;
    }

    public void setCategory(List<SkillCategory> category) {
        this.category = category;
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

    public String getUserProfileImage() {
        return userProfileImage;
    }

    public void setUserProfileImage(String userProfileImage) {
        this.userProfileImage = userProfileImage;
    }
}
