package com.arimac.backend.pmtool.projectmanagementtool.Service.Impl;

import com.arimac.backend.pmtool.projectmanagementtool.Response.Response;
import com.arimac.backend.pmtool.projectmanagementtool.Service.SkillService;
import com.arimac.backend.pmtool.projectmanagementtool.dtos.Skill.SkillDto;
import com.arimac.backend.pmtool.projectmanagementtool.enumz.ResponseMessage;
import com.arimac.backend.pmtool.projectmanagementtool.exception.ErrorMessage;
import com.arimac.backend.pmtool.projectmanagementtool.model.Category;
import com.arimac.backend.pmtool.projectmanagementtool.model.Skill;
import com.arimac.backend.pmtool.projectmanagementtool.model.User;
import com.arimac.backend.pmtool.projectmanagementtool.repository.CategoryRepository;
import com.arimac.backend.pmtool.projectmanagementtool.repository.SkillRepository;
import com.arimac.backend.pmtool.projectmanagementtool.repository.UserRepository;
import com.arimac.backend.pmtool.projectmanagementtool.utils.UtilsService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
public class SkillServiceImpl implements SkillService {

    private final SkillRepository skillRepository;
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;
    private final UtilsService utilsService;

    public SkillServiceImpl(SkillRepository skillRepository, CategoryRepository categoryRepository, UserRepository userRepository, UtilsService utilsService) {
        this.skillRepository = skillRepository;
        this.categoryRepository = categoryRepository;
        this.userRepository = userRepository;
        this.utilsService = utilsService;
    }

    @Override
    public Object addSkillToCategory(String userId, String categoryId, SkillDto skillDto) {
        User user = userRepository.getUserByUserId(userId);
        if (user == null)
            return new ErrorMessage(ResponseMessage.USER_NOT_FOUND, HttpStatus.NOT_FOUND);
        Category category = categoryRepository.getCategoryById(categoryId);
        if (category == null)
            return new ErrorMessage(ResponseMessage.CATEGORY_NOT_FOUND, HttpStatus.NOT_FOUND);
        if (skillRepository.getSkillByNameAndCategory(categoryId, skillDto.getSkillName()) != null)
            return new ErrorMessage(ResponseMessage.SKILL_NAME_EXIST, HttpStatus.CONFLICT);
        Skill skill = new Skill();
        skill.setSkillId(utilsService.getUUId());
        skill.setSkillName(skillDto.getSkillName());
        skill.setCategoryId(categoryId);
        skill.setSkillCreator(userId);
        skill.setSkillCreatedAt(utilsService.getCurrentTimestamp());

        skillRepository.addSkill(skill);

        return new Response(ResponseMessage.SUCCESS, HttpStatus.OK);
    }
}
