package com.arimac.backend.pmtool.projectmanagementtool.dtos.Skill;

import java.util.List;

public class SkillMapUserResponse {
    private String userId;
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
}
