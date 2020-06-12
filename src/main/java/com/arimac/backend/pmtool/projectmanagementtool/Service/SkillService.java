package com.arimac.backend.pmtool.projectmanagementtool.Service;

import com.arimac.backend.pmtool.projectmanagementtool.dtos.Skill.SkillDto;

public interface SkillService {
    Object addSkillToCategory(String userId, String categoryId, SkillDto skillDto);
    Object getAllCategorySkills(String userId, String categoryId);
    Object deleteSkill(String userId, String categoryId, String skillId);
    Object updateSkill(String userId, String categoryId, String skillId, SkillDto skillDto);

}
