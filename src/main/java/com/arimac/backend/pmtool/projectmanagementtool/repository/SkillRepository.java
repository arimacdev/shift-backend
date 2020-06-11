package com.arimac.backend.pmtool.projectmanagementtool.repository;

import com.arimac.backend.pmtool.projectmanagementtool.model.Skill;

public interface SkillRepository {
    void addSkill(Skill skill);
    Skill getSkillByNameAndCategory(String categoryId, String name);
}
