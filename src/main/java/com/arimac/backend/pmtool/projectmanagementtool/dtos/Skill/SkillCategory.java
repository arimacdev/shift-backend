package com.arimac.backend.pmtool.projectmanagementtool.dtos.Skill;

import java.util.List;
import java.util.Map;

public class SkillCategory {
    private String categoryId;
    private String categoryName;
    private String categoryColorCode;
    private List<CategorySkill> skillSet;

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

    public String getCategoryColorCode() {
        return categoryColorCode;
    }

    public void setCategoryColorCode(String categoryColorCode) {
        this.categoryColorCode = categoryColorCode;
    }

    public List<CategorySkill> getSkillSet() {
        return skillSet;
    }

    public void setSkillSet(List<CategorySkill> skillSet) {
        this.skillSet = skillSet;
    }
}
