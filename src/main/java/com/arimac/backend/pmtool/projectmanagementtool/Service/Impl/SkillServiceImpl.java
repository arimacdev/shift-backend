package com.arimac.backend.pmtool.projectmanagementtool.Service.Impl;

import com.arimac.backend.pmtool.projectmanagementtool.Response.Response;
import com.arimac.backend.pmtool.projectmanagementtool.Service.SkillService;
import com.arimac.backend.pmtool.projectmanagementtool.dtos.Skill.*;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
        List<SkillCategoryDto> categorySkillList = skillRepository.getSkillMatrix();
        Map<String,SkillCategory> skillMatrix = getSkillMatrix(categorySkillList, false);
//        for (SkillCategoryDto categorySkill: categorySkillList){
//            if (skillMatrix.get(categorySkill.getCategoryId())!= null){
//                SkillCategory skillCategory = skillMatrix.get(categorySkill.getCategoryId());
//                List<CategorySkill> skills = skillCategory.getSkillSet();
//                CategorySkill skill = new CategorySkill();
//                skill.setSkillId(categorySkill.getSkillId());
//                skill.setSkillName(categorySkill.getSkillName());
//                skill.setIsAssigned(false);
//                skills.add(skill);
//                skillCategory.setSkillSet(skills);
//                skillMatrix.put(categorySkill.getCategoryId(), skillCategory);
//            } else {
//                SkillCategory skillCategory = new SkillCategory();
//                skillCategory.setCategoryId(categorySkill.getCategoryId());
//                skillCategory.setCategoryName(categorySkill.getCategoryName());
//                skillCategory.setCategoryColorCode(categorySkill.getCategoryColorCode());
//                List<CategorySkill> skillSet = new ArrayList<>();
//                CategorySkill skill = new CategorySkill();
//                skill.setSkillId(categorySkill.getSkillId());
//                skill.setIsAssigned(false);
//                skill.setSkillName(categorySkill.getSkillName());
//                skillSet.add(skill);
//                skillCategory.setSkillSet(skillSet);
//                skillMatrix.put(categorySkill.getCategoryId(), skillCategory);
//            }
//        }

        //List<SkillCategory> skillCategoryList = new ArrayList<>(skillMatrix.values());

        Map<String, Map<String, SkillCategory>> userSkillMap = new HashMap<>();
        List<SkillMapUserResponse> skillMapUserResponseList = new ArrayList<>();
        List<SkillUserResponseDto> userSkillList = skillRepository.getAllUserSkillMap(assignee);
        if (userSkillList.isEmpty()){
            userSkillMap.put(assignee, skillMatrix);
           // return new Response(ResponseMessage.SUCCESS, HttpStatus.OK, new ArrayList<>(getSkillMatrix(categorySkillList, true).values()));
        } else {
            //List<SkillCategory> skillList = new ArrayList<>(getSkillMatrix(categorySkillList, false).values());
            //Map<String, SkillCategory> userSkillCategory1 = new HashMap<>();
            for (SkillUserResponseDto userSkill : userSkillList) {
                if (userSkillMap.get(userSkill.getUserId()) != null) {
                    Map<String, SkillCategory> userSkillCategory = userSkillMap.get(userSkill.getUserId());
                    if (userSkillCategory.get(userSkill.getCategoryId()) != null) {
                        SkillCategory skillCategory = userSkillCategory.get(userSkill.getCategoryId());
                        List<CategorySkill> skillSet = skillCategory.getSkillSet();
                        for (CategorySkill skill : skillSet) {
                            if (userSkill.getSkillId().equals(skill.getSkillId())) {
                                skill.setIsAssigned(true);
                            }
                        }
                    }
                } else {
                    userSkillMap.put(userSkill.getUserId(), skillMatrix);
                }

            }
        }

        for (Map.Entry<String, Map<String,SkillCategory>> entry: userSkillMap.entrySet()){
            SkillMapUserResponse skillMapUserResponse = new SkillMapUserResponse();
            skillMapUserResponse.setUserId(entry.getKey());
            List<SkillCategory> skillCategoryList1 = new ArrayList<>(entry.getValue().values());
            skillMapUserResponse.setCategory(skillCategoryList1);
            skillMapUserResponseList.add(skillMapUserResponse);
        }
        return new Response(ResponseMessage.SUCCESS, HttpStatus.OK, skillMapUserResponseList);
    }

    @Override
    public Object getSkillMatrixOfUsers(String userId, int limit, int offset) {
        User assigner = userRepository.getUserByUserId(userId);
        if (assigner == null)
            return new ErrorMessage(ResponseMessage.USER_NOT_FOUND, HttpStatus.NOT_FOUND);
       List<User> userList = userRepository.getAllUsersWithPagination(limit, offset);
        List<SkillCategoryDto> categorySkillList = skillRepository.getSkillMatrix();

        Map<User, Map<String, SkillCategory>> userSkillMap = new HashMap<>();
        List<SkillMapUserResponse> skillMapUserResponseList = new ArrayList<>();
        for (User assignee: userList) {
            List<SkillUserResponseDto> userSkillList = skillRepository.getAllUserSkillMap(assignee.getUserId());
            Map<String,SkillCategory> skillMatrix = getSkillMatrix(categorySkillList, false);
            if (userSkillList.isEmpty()) {
                userSkillMap.put(assignee, skillMatrix);
            } else {
                for (SkillUserResponseDto userSkill : userSkillList) {
                    if (userSkillMap.get(assignee) != null) {
                        Map<String, SkillCategory> userSkillCategory = userSkillMap.get(assignee);
                        if (userSkillCategory.get(userSkill.getCategoryId()) != null) {
                            SkillCategory skillCategory = userSkillCategory.get(userSkill.getCategoryId());
                            List<CategorySkill> skillSet = skillCategory.getSkillSet();
                            for (CategorySkill skill : skillSet) {
                                if (userSkill.getSkillId().equals(skill.getSkillId())) {
                                    skill.setIsAssigned(true);
                                }
                            }
                        }
                    } else {
                        userSkillMap.put(assignee, skillMatrix);
                    }
                }
            }
        }

        for (Map.Entry<User, Map<String,SkillCategory>> entry: userSkillMap.entrySet()){
            SkillMapUserResponse skillMapUserResponse = new SkillMapUserResponse();
            skillMapUserResponse.setUserId(entry.getKey().getUserId());
            skillMapUserResponse.setFirstName(entry.getKey().getFirstName());
            skillMapUserResponse.setLastName(entry.getKey().getLastName());
            skillMapUserResponse.setUserProfileImage(entry.getKey().getProfileImage());
            List<SkillCategory> skillCategoryList1 = new ArrayList<>(entry.getValue().values());
            skillMapUserResponse.setCategory(skillCategoryList1);
            skillMapUserResponseList.add(skillMapUserResponse);
        }
        return new Response(ResponseMessage.SUCCESS, HttpStatus.OK, skillMapUserResponseList);
    }

    @Override
    public Object getAllUserMatchingSkills(String userId, String assignee) {
        User assigner = userRepository.getUserByUserId(userId);
        if (assigner == null)
            return new ErrorMessage(ResponseMessage.USER_NOT_FOUND, HttpStatus.NOT_FOUND);
        User assigneeUser = userRepository.getUserByUserId(assignee);
        if (assigneeUser == null)
            return new ErrorMessage(ResponseMessage.USER_NOT_FOUND, HttpStatus.NOT_FOUND);
        List<SkillUserResponseDto> userSkillList = skillRepository.getAllUserMatchingSkills(userId);
        Map<String, UserMatchingSkillsResponse> categorizedList = new HashMap<>();
        for (SkillUserResponseDto userSkill: userSkillList){
            if (categorizedList.get(userSkill.getCategoryId())!= null){
                List<CategorySkill> skillSet = categorizedList.get(userSkill.getCategoryId()).getSkillSet();
                CategorySkill categorySkill = new CategorySkill();
                categorySkill.setSkillName(userSkill.getSkillName());
                categorySkill.setSkillId(userSkill.getSkillId());
                categorySkill.setIsAssigned(true);
                skillSet.add(categorySkill);
            } else {
                UserMatchingSkillsResponse category = new UserMatchingSkillsResponse();
                category.setCategoryId(userSkill.getCategoryId());
                category.setCategoryName(userSkill.getCategoryName());
                category.setCategoryColorCode(userSkill.getCategoryColorCode());
                List<CategorySkill> skillSet = new ArrayList<>();
                CategorySkill categorySkill = new CategorySkill();
                categorySkill.setSkillName(userSkill.getSkillName());
                categorySkill.setSkillId(userSkill.getSkillId());
                categorySkill.setIsAssigned(true);
                skillSet.add(categorySkill);
                category.setSkillSet(skillSet);
                categorizedList.put(userSkill.getCategoryId(),category);
            }
        }
        List<UserMatchingSkillsResponse> skillsResponse = new ArrayList<>(categorizedList.values());
        return new Response(ResponseMessage.SUCCESS, HttpStatus.OK, skillsResponse);
    }

    @Override
    public Object getCategorySkillMapping(String userId) {
        User user = userRepository.getUserByUserId(userId);
        if (user == null)
            return new ErrorMessage(ResponseMessage.USER_NOT_FOUND, HttpStatus.NOT_FOUND);
        List<SkillCategoryDto> categorySkillList = skillRepository.getSkillMatrix();
        Map<String,SkillCategory> skillMatrix = getSkillMatrix(categorySkillList, false);
        List<SkillCategory> skillList = new ArrayList<>(skillMatrix.values());
        return new Response(ResponseMessage.SUCCESS, HttpStatus.OK, skillList);

    }


    private Map<String, SkillCategory> getSkillMatrix(List<SkillCategoryDto> categorySkillList, boolean isAssigned){
        Map<String,SkillCategory> skillMatrix = new HashMap<>();
        for (SkillCategoryDto categorySkill: categorySkillList){
            if (skillMatrix.get(categorySkill.getCategoryId())!= null){
                SkillCategory skillCategory = skillMatrix.get(categorySkill.getCategoryId());
                List<CategorySkill> skills = skillCategory.getSkillSet();
                CategorySkill skill = new CategorySkill();
                skill.setSkillId(categorySkill.getSkillId());
                skill.setSkillName(categorySkill.getSkillName());
                if (isAssigned)
                skill.setIsAssigned(false);
                skills.add(skill);
                skillCategory.setSkillSet(skills);
                skillMatrix.put(categorySkill.getCategoryId(), skillCategory);
            } else {
                SkillCategory skillCategory = new SkillCategory();
                skillCategory.setCategoryId(categorySkill.getCategoryId());
                skillCategory.setCategoryName(categorySkill.getCategoryName());
                skillCategory.setCategoryColorCode(categorySkill.getCategoryColorCode());
                List<CategorySkill> skillSet = new ArrayList<>();
                CategorySkill skill = new CategorySkill();
                skill.setSkillId(categorySkill.getSkillId());
                if (isAssigned)
                skill.setIsAssigned(false);
                skill.setSkillName(categorySkill.getSkillName());
                skillSet.add(skill);
                skillCategory.setSkillSet(skillSet);
                skillMatrix.put(categorySkill.getCategoryId(), skillCategory);
            }
        }

        return skillMatrix;
    }

}
