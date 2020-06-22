package com.arimac.backend.pmtool.projectmanagementtool.controller;

import com.arimac.backend.pmtool.projectmanagementtool.Response.Response;
import com.arimac.backend.pmtool.projectmanagementtool.Response.ResponseController;
import com.arimac.backend.pmtool.projectmanagementtool.Service.SkillService;
import com.arimac.backend.pmtool.projectmanagementtool.dtos.Category.CategoryDto;
import com.arimac.backend.pmtool.projectmanagementtool.dtos.Skill.SkillDto;
import com.arimac.backend.pmtool.projectmanagementtool.dtos.Skill.SkillUserDto;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/category")
public class SkillController extends ResponseController {
    private static final Logger logger = LoggerFactory.getLogger(SkillController.class);

    private final SkillService skillService;

    public SkillController(SkillService skillService) {
        this.skillService = skillService;
    }

    @ApiOperation(value = "Add Skill for Category", notes = "Create Skill for a Category")
    @ApiResponse(code = 200, message = "Success", response = Response.class)
    @PostMapping("/{categoryId}/skill")
    public ResponseEntity<Object> addSkillToCategory(@Valid @RequestBody SkillDto skillDto, @RequestHeader("userId") String userId, @PathVariable("categoryId") String categoryId){
        logger.info("HIT - POST | addSkillToCategory /category/<categoryId>/skill | userId: {} | dto: {} | categoryId: {}", userId, skillDto, categoryId);
        return sendResponse(skillService.addSkillToCategory(userId, categoryId, skillDto));
    }

    @ApiOperation(value = "Get All Skills of a Category", notes = "Get All Skills of a Category")
    @ApiResponse(code = 200, message = "Success", response = Response.class)
    @GetMapping("/{categoryId}/skill")
    public ResponseEntity<Object> getAllCategorySkills(@RequestHeader("userId") String userId, @PathVariable("categoryId") String categoryId){
        logger.info("HIT - GET | getAllCategorySkills /category/<categoryId>/skill | userId: {} |  categoryId: {}", userId,  categoryId);
        return sendResponse(skillService.getAllCategorySkills(userId, categoryId));
    }

    @ApiOperation(value = "Delete a Skill", notes = "Delete Skill")
    @ApiResponse(code = 200, message = "Success", response = Response.class)
    @DeleteMapping("/{categoryId}/skill/{skillId}")
    public ResponseEntity<Object> deleteSkill(@RequestHeader("userId") String userId, @PathVariable("categoryId") String categoryId, @PathVariable("skillId") String skillId){
        logger.info("HIT - DELETE | deleteSkill /category/<categoryId>/skill/<skillId> | userId: {} |  categoryId: {}", userId,  categoryId);
        return sendResponse(skillService.deleteSkill(userId, categoryId, skillId));
    }

    @ApiOperation(value = "Update a Skill", notes = "Update a  Skill")
    @ApiResponse(code = 200, message = "Success", response = Response.class)
    @PutMapping("/{categoryId}/skill/{skillId}")
    public ResponseEntity<Object> updateSkill(@RequestHeader("userId") String userId, @PathVariable("categoryId") String categoryId, @PathVariable("skillId") String skillId, @Valid @RequestBody SkillDto skillDto){
        logger.info("HIT - PUT | updateSkill /category/<categoryId>/skill/<skillId> | userId: {} |  categoryId: {} | skillDto : {}", userId,  categoryId, skillDto);
        return sendResponse(skillService.updateSkill(userId, categoryId, skillId, skillDto));
    }

    @ApiOperation(value = "Add Skills a User", notes = "Add Skills a User")
    @ApiResponse(code = 200, message = "Success", response = Response.class)
    @PostMapping("/{categoryId}/user/skill")
    public ResponseEntity<Object> addSkillsToUser(@RequestHeader("userId") String userId, @PathVariable("categoryId") String categoryId, @Valid @RequestBody SkillUserDto skillUserDto){
        logger.info("HIT - PUT | updateSkill /category/<categoryId>/skill/<skillId> | userId: {} |  categoryId: {} | skillUserDto : {}", userId,  categoryId, skillUserDto);
        return sendResponse(skillService.addSkillsToUser(userId, categoryId, skillUserDto));
    }

    @ApiOperation(value = "Delete Skills from User", notes = "Delete Skills from User")
    @ApiResponse(code = 200, message = "Success", response = Response.class)
    @DeleteMapping("/{categoryId}/user/skill")
    public ResponseEntity<Object> deleteSkillsFromUser(@RequestHeader("userId") String userId, @PathVariable("categoryId") String categoryId,@Valid @RequestBody SkillUserDto skillUserDto){
        logger.info("HIT - PUT | updateSkill /category/<categoryId>/skill/<skillId> | userId: {} |  categoryId: {} | skillUserDto : {}", userId,  categoryId, skillUserDto);
        return sendResponse(skillService.deleteSkillsFromUser(userId, categoryId, skillUserDto));
    }

    @ApiOperation(value = "Add Skills a User", notes = "Add Skills a User")
    @ApiResponse(code = 200, message = "Success", response = Response.class)
    @GetMapping("/user/{assignee}/skillmap")
    public ResponseEntity<Object> getAllUserSkills(@RequestHeader("userId") String userId, @PathVariable("assignee") String assignee){
        logger.info("HIT - GET | getAllUserSkills /category/user/<assignee>/skillmap| userId: {} | assignee: {} ", userId, assignee);
        return sendResponse(skillService.getAllUserSkillMap(userId, assignee));
    }
    //THIS****
    @ApiOperation(value = "Add Skills a User", notes = "Add Skills a User")
    @ApiResponse(code = 200, message = "Success", response = Response.class)
    @GetMapping("/user/skillmatrix")
    public ResponseEntity<Object> getSkillMatrixOfUsers(@RequestHeader("userId") String userId, @RequestParam("limit") int limit, @RequestParam("offset") int offset){
        logger.info("HIT - GET | getSkillMatrixOfUsers /category/user/skillmatrix | userId: {} ", userId);
        return sendResponse(skillService.getSkillMatrixOfUsers(userId, limit, offset));
    }

    @ApiOperation(value = "Add Skills a User", notes = "Add Skills a User")
    @ApiResponse(code = 200, message = "Success", response = Response.class)
    @GetMapping("/user/skills")
    public ResponseEntity<Object> skillFilteration(@RequestHeader("userId") String userId, @RequestParam("skill") Set<String> skills){
        logger.info("HIT - GET | skillFilteration /category/user/skills?<skill> | userId: {} | skills: {} ", userId, skills);
        return sendResponse(skillService.skillFilteration(userId, skills));
    }

    @ApiOperation(value = "Add Skills a User for User Profile", notes = "Add Skills a User")
    @ApiResponse(code = 200, message = "Success", response = Response.class)
    @GetMapping("/user/{assignee}/skillmap/profile")
    public ResponseEntity<Object> getAllUserMatchingSkills(@RequestHeader("userId") String userId, @PathVariable("assignee") String assignee){
        logger.info("HIT - GET | getAllUserMatchingSkills /category/user/<assignee>/skillmap/profile| userId: {} | assignee: {} ", userId, assignee);
        return sendResponse(skillService.getAllUserMatchingSkills(userId, assignee));
    }

    @ApiOperation(value = "Get Category/Skill Mapping", notes = "Get Category/Skill Mapping")
    @ApiResponse(code = 200, message = "Success", response = Response.class)
    @GetMapping("/skill/mapping")
    public ResponseEntity<Object> getCategorySkillMapping(@RequestHeader("userId") String userId){
        logger.info("HIT - GET | getAllUserMatchingSkills /category/skill/mapping| userId: {}" , userId);
        return sendResponse(skillService.getCategorySkillMapping(userId));
    }



}
