package com.arimac.backend.pmtool.projectmanagementtool.Service;

import com.arimac.backend.pmtool.projectmanagementtool.dtos.Skill.SkillDto;
import com.arimac.backend.pmtool.projectmanagementtool.dtos.Skill.SkillUserDto;

import java.util.List;
import java.util.Set;

public interface SkillService {
    Object addSkillToCategory(String userId, String categoryId, SkillDto skillDto);
    Object getAllCategorySkills(String userId, String categoryId);
    Object deleteSkill(String userId, String categoryId, String skillId);
    Object updateSkill(String userId, String categoryId, String skillId, SkillDto skillDto);
    Object addSkillsToUser(String userId, String categoryId,  SkillUserDto skillUserDto);
    Object deleteSkillsFromUser(String userId, String categoryId,  SkillUserDto skillUserDto);
    Object getAllUserSkillMap(String userId, String assignee);
    Object getSkillMatrixOfUsers(String userId, int limit, int offset);
    Object skillFilteration(String userId, Set<String> skills);
    Object getAllUserMatchingSkills(String userId, String assignee);
    Object getCategorySkillMapping(String userId);
}
