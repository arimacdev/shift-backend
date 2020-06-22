package com.arimac.backend.pmtool.projectmanagementtool.dtos.Skill;

import javax.validation.constraints.NotEmpty;
import java.util.Set;

public class SkillUserDto {
    @NotEmpty
    private String assigneeId;
    @NotEmpty
    private Set<String> skills;

    public String getAssigneeId() {
        return assigneeId;
    }

    public void setAssigneeId(String assigneeId) {
        this.assigneeId = assigneeId;
    }

    public Set<String> getSkills() {
        return skills;
    }

    public void setSkills(Set<String> skills) {
        this.skills = skills;
    }

    @Override
    public String toString() {
        return "SkillUserDto{" +
                "assigneeId='" + assigneeId + '\'' +
                ", skills=" + skills +
                '}';
    }
}
