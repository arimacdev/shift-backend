package com.arimac.backend.pmtool.projectmanagementtool.Service;

import com.arimac.backend.pmtool.projectmanagementtool.dtos.Skill.SkillDto;

public interface SkillService {
    Object addSkillToCategory(String userId, String categoryId, SkillDto skillDto);
}
