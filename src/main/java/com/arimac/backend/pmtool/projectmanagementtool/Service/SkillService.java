package com.arimac.backend.pmtool.projectmanagementtool.Service;

import com.arimac.backend.pmtool.projectmanagementtool.dtos.Skill.SkillDto;
import com.arimac.backend.pmtool.projectmanagementtool.dtos.Skill.SkillUserDto;

public interface SkillService {
    Object addSkillToCategory(String userId, String categoryId, SkillDto skillDto);
    Object getAllCategorySkills(String userId, String categoryId);
    Object deleteSkill(String userId, String categoryId, String skillId);
    Object updateSkill(String userId, String categoryId, String skillId, SkillDto skillDto);
    Object addSkillsToUser(String userId, String categoryId,  SkillUserDto skillUserDto);
    Object deleteSkillsFromUser(String userId, String categoryId,  SkillUserDto skillUserDto);


}
