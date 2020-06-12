package com.arimac.backend.pmtool.projectmanagementtool.Service.Impl;

import com.arimac.backend.pmtool.projectmanagementtool.Response.Response;
import com.arimac.backend.pmtool.projectmanagementtool.Service.SkillService;
import com.arimac.backend.pmtool.projectmanagementtool.dtos.Skill.SkillDto;
import com.arimac.backend.pmtool.projectmanagementtool.dtos.Skill.SkillUserDto;
import com.arimac.backend.pmtool.projectmanagementtool.dtos.Skill.SkillUserResponseDto;
import com.arimac.backend.pmtool.projectmanagementtool.enumz.ResponseMessage;
import com.arimac.backend.pmtool.projectmanagementtool.exception.ErrorMessage;
import com.arimac.backend.pmtool.projectmanagementtool.model.Category;
import com.arimac.backend.pmtool.projectmanagementtool.model.Skill;
import com.arimac.backend.pmtool.projectmanagementtool.model.User;
import com.arimac.backend.pmtool.projectmanagementtool.model.UserSkill;
import com.arimac.backend.pmtool.projectmanagementtool.repository.CategoryRepository;
import com.arimac.backend.pmtool.projectmanagementtool.repository.SkillRepository;
import com.arimac.backend.pmtool.projectmanagementtool.repository.UserRepository;
import com.arimac.backend.pmtool.projectmanagementtool.utils.UtilsService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

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

    @Override
    public Object getAllCategorySkills(String userId, String categoryId) {
        User user = userRepository.getUserByUserId(userId);
        if (user == null)
            return new ErrorMessage(ResponseMessage.USER_NOT_FOUND, HttpStatus.NOT_FOUND);
        Category category = categoryRepository.getCategoryById(categoryId);
        if (category == null)
            return new ErrorMessage(ResponseMessage.CATEGORY_NOT_FOUND, HttpStatus.NOT_FOUND);
        return new Response(ResponseMessage.SUCCESS, HttpStatus.OK, skillRepository.getAllCategorySkills(categoryId));
    }

    @Override
    public Object deleteSkill(String userId, String categoryId, String skillId) {
        User user = userRepository.getUserByUserId(userId);
        if (user == null)
            return new ErrorMessage(ResponseMessage.USER_NOT_FOUND, HttpStatus.NOT_FOUND);
        Category category = categoryRepository.getCategoryById(categoryId);
        if (category == null)
            return new ErrorMessage(ResponseMessage.CATEGORY_NOT_FOUND, HttpStatus.NOT_FOUND);
        Skill skill = skillRepository.getSkillByIdAndCategory(categoryId, skillId);
        if (skill == null)
            return new ErrorMessage(ResponseMessage.SKILL_NOT_FOUND, HttpStatus.NOT_FOUND);
        skillRepository.flagSkill(skillId);
        return new Response(ResponseMessage.SUCCESS, HttpStatus.OK);
    }

    @Override
    public Object updateSkill(String userId, String categoryId, String skillId, SkillDto skillDto) {
        User user = userRepository.getUserByUserId(userId);
        if (user == null)
            return new ErrorMessage(ResponseMessage.USER_NOT_FOUND, HttpStatus.NOT_FOUND);
        Category category = categoryRepository.getCategoryById(categoryId);
        if (category == null)
            return new ErrorMessage(ResponseMessage.CATEGORY_NOT_FOUND, HttpStatus.NOT_FOUND);
        Skill skill = skillRepository.getSkillByIdAndCategory(categoryId, skillId);
        if (skill == null)
            return new ErrorMessage(ResponseMessage.SKILL_NOT_FOUND, HttpStatus.NOT_FOUND);
        if (skillRepository.getSkillByNameAndCategory(categoryId, skillDto.getSkillName()) != null)
                return new ErrorMessage(ResponseMessage.SKILL_NAME_EXIST, HttpStatus.CONFLICT);
        skillRepository.updateSkill(skillDto, skillId);
        return new Response(ResponseMessage.SUCCESS, HttpStatus.OK);
    }

    @Override
    public Object addSkillsToUser(String userId, String categoryId, SkillUserDto skillUserDto) {
        User assigner = userRepository.getUserByUserId(userId);
        if (assigner == null)
            return new ErrorMessage(ResponseMessage.ASSIGNEE_NOT_FOUND, HttpStatus.NOT_FOUND);
        User assignee = userRepository.getUserByUserId(skillUserDto.getAssigneeId());
        if (assignee == null)
            return new ErrorMessage(ResponseMessage.ASSIGNEE_NOT_FOUND, HttpStatus.NOT_FOUND);
        Category category = categoryRepository.getCategoryById(categoryId);
        if (category == null)
            return new ErrorMessage(ResponseMessage.CATEGORY_NOT_FOUND, HttpStatus.NOT_FOUND);
        if (skillRepository.checkIfSkillAdded(skillUserDto.getAssigneeId(), categoryId, skillUserDto.getSkills()))
            return new ErrorMessage(ResponseMessage.SKILL_ALREADY_ADDED, HttpStatus.CONFLICT);
        UserSkill userSkill = new UserSkill();
        userSkill.setCategoryId(categoryId);
        userSkill.setUserId(skillUserDto.getAssigneeId());
        for (String newSkill: skillUserDto.getSkills()){
            Skill skill = skillRepository.getSkillByIdAndCategory(categoryId, newSkill);
            if (skill != null) {
                userSkill.setSkillId(newSkill);
                skillRepository.addSkillToUser(userSkill);
            }
        }
        return new Response(ResponseMessage.SUCCESS, HttpStatus.OK);
    }

    @Override
    public Object deleteSkillsFromUser(String userId, String categoryId, SkillUserDto skillUserDto) {
        User assigner = userRepository.getUserByUserId(userId);
        if (assigner == null)
            return new ErrorMessage(ResponseMessage.ASSIGNEE_NOT_FOUND, HttpStatus.NOT_FOUND);
        User assignee = userRepository.getUserByUserId(skillUserDto.getAssigneeId());
        if (assignee == null)
            return new ErrorMessage(ResponseMessage.ASSIGNEE_NOT_FOUND, HttpStatus.NOT_FOUND);
        Category category = categoryRepository.getCategoryById(categoryId);
        if (category == null)
            return new ErrorMessage(ResponseMessage.CATEGORY_NOT_FOUND, HttpStatus.NOT_FOUND);
        if (!skillRepository.checkIfSkillAdded(skillUserDto.getAssigneeId(), categoryId, skillUserDto.getSkills()))
            return new ErrorMessage(ResponseMessage.SKILL_NOT_ADDED, HttpStatus.NOT_FOUND);
        skillRepository.removeSkillsFromUser(skillUserDto.getAssigneeId(), categoryId, skillUserDto.getSkills());

        return new Response(ResponseMessage.SUCCESS, HttpStatus.OK);

    }

    @Override
    public Object getAllUserSkillMap(String userId, String assignee) {
        User assigner = userRepository.getUserByUserId(userId);
        if (assigner == null)
            return new ErrorMessage(ResponseMessage.USER_NOT_FOUND, HttpStatus.NOT_FOUND);
        User assigneeUser = userRepository.getUserByUserId(assignee);
        if (assigneeUser == null)
            return new ErrorMessage(ResponseMessage.USER_NOT_FOUND, HttpStatus.NOT_FOUND);
        List<SkillUserResponseDto> userSkillList = skillRepository.getAllUserSkillMap(assignee);
        return new Response(ResponseMessage.SUCCESS, HttpStatus.OK, userSkillList);
    }


}
