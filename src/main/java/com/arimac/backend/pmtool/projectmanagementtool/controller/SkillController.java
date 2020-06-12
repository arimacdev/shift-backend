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
    @PostMapping("/{categoryId}/skill/{skillId}")
    public ResponseEntity<Object> addSkillsToUser(@RequestHeader("userId") String userId, @PathVariable("categoryId") String categoryId, @PathVariable("skillId") String skillId, @Valid @RequestBody SkillUserDto skillUserDto){
        logger.info("HIT - PUT | updateSkill /category/<categoryId>/skill/<skillId> | userId: {} |  categoryId: {} | skillUserDto : {}", userId,  categoryId, skillUserDto);
        return sendResponse(skillService.addSkillsToUser(userId, categoryId, skillId, skillUserDto));
    }
}
