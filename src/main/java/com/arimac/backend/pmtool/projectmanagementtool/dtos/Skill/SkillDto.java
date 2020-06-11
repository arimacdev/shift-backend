package com.arimac.backend.pmtool.projectmanagementtool.dtos.Skill;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

public class SkillDto {
    @NotEmpty
    private String skillName;

    public String getSkillName() {
        return skillName;
    }

    public void setSkillName(String skillName) {
        this.skillName = skillName;
    }
}
