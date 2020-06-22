package com.arimac.backend.pmtool.projectmanagementtool.repository;

import com.arimac.backend.pmtool.projectmanagementtool.dtos.Skill.SkillCategoryDto;
import com.arimac.backend.pmtool.projectmanagementtool.dtos.Skill.SkillCategoryUserResponse;
import com.arimac.backend.pmtool.projectmanagementtool.dtos.Skill.SkillDto;
import com.arimac.backend.pmtool.projectmanagementtool.dtos.Skill.SkillUserResponseDto;
import com.arimac.backend.pmtool.projectmanagementtool.model.Skill;
import com.arimac.backend.pmtool.projectmanagementtool.model.UserSkill;

import java.util.List;
import java.util.Set;

public interface SkillRepository {
    void addSkill(Skill skill);
    Skill getSkillByNameAndCategory(String categoryId, String name);
    List<Skill> getAllCategorySkills(String categoryId);
    Skill getSkillByIdAndCategory(String categoryId, String skillId);
    void flagSkill(String skillId);
    void updateSkill(SkillDto skillDto, String skillId);
    void addSkillToUser(UserSkill userSkill);
    boolean checkIfSkillAdded(String userId, String categoryId, Set<String> skills);
    void removeSkillsFromUser(String userId, String categoryId, Set<String> skills);
    List<SkillCategoryUserResponse> getSkillFilteration(Set<String> skills);
    List<SkillUserResponseDto> getAllUserSkillMap(String userId);
    List<SkillCategoryDto> getSkillMatrix();
    List<SkillCategoryDto> getMatrixForCategories(Set<String> skills);
    List<SkillUserResponseDto> getAllUserMatchingSkills(String userId);
}
