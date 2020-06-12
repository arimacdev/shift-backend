package com.arimac.backend.pmtool.projectmanagementtool.repository;

import com.arimac.backend.pmtool.projectmanagementtool.dtos.Skill.SkillDto;
import com.arimac.backend.pmtool.projectmanagementtool.model.Skill;

import java.util.List;

public interface SkillRepository {
    void addSkill(Skill skill);
    Skill getSkillByNameAndCategory(String categoryId, String name);
    List<Skill> getAllCategorySkills(String categoryId);
    Skill getSkillByIdAndCategory(String categoryId, String skillId);
    void flagSkill(String skillId);
    void updateSkill(SkillDto skillDto, String skillId);

}
