package com.arimac.backend.pmtool.projectmanagementtool.dtos.Skill;

import java.util.List;

public class UserSkillMap {
    private String userId;
    private List<SkillCategory> skillMap;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public List<SkillCategory> getSkillMap() {
        return skillMap;
    }

    public void setSkillMap(List<SkillCategory> skillMap) {
        this.skillMap = skillMap;
    }
}
