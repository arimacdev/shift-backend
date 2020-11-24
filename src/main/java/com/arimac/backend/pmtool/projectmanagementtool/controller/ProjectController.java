package com.arimac.backend.pmtool.projectmanagementtool.controller;


import com.arimac.backend.pmtool.projectmanagementtool.Response.Response;
import com.arimac.backend.pmtool.projectmanagementtool.Response.ResponseController;
import com.arimac.backend.pmtool.projectmanagementtool.Service.ProjectService;
import com.arimac.backend.pmtool.projectmanagementtool.dtos.*;
import com.arimac.backend.pmtool.projectmanagementtool.dtos.Project.*;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;


@RestController
@RequestMapping("/projects")
public class ProjectController extends ResponseController {

    private static final Logger logger = LoggerFactory.getLogger(ProjectController.class);

    private final ProjectService projectService;

    public ProjectController(ProjectService projectService) {
        this.projectService = projectService;
    }

    @ApiOperation(value = "Project Create", notes = "Create a project for an organization")
    @ApiResponse(code = 200, message = "Success", response = Response.class)
    @PostMapping
    public ResponseEntity<Object> createProject(@RequestBody @Valid ProjectDto projectDto){
        logger.info("HIT - createProject - /projects POST  dto: {}", projectDto);
        return sendResponse(projectService.createProject(projectDto));
    }

    @ApiOperation(value = "Pin/UnPin Projects to users", notes = "Pin/UnPin Projects to users")
    @ApiResponse(code = 200, message = "Success", response = Response.class)
    @PostMapping("/pin")
    public ResponseEntity<Object> PinUnpinProjects(@RequestBody @Valid ProjectPinUnPin projectPinUnPin){
        logger.info("HIT - PinUnpinProjects /projects/pin POST  dto: {}", projectPinUnPin);
        return sendResponse(projectService.PinUnpinProjects(projectPinUnPin));
    }

    @ApiOperation(value = "Get all user assigned projects", notes = "Get all user assigned projects")
    @ApiResponse(code = 200, message = "Success", response = Response.class)
    @GetMapping
    public ResponseEntity<Object> getAllUserAssignedProjects(@RequestParam("userId")String user){
        logger.info("HIT - GET /projects?userId=<user> ---> getAllProjects | userId: {}",user);
        return sendResponse(projectService.getAllUserAssignedProjects(user));
    }

    @ApiOperation(value = "Get all Projects", notes = "Get all projects of an organization")
    @ApiResponse(code = 200, message = "Success", response = Response.class)
    @GetMapping("/all")
    public ResponseEntity<Object> getAllProjects(@RequestHeader("user") String user){
        logger.info("HIT - GET /projects?userId=<user> ---> getAllProjects | userId: {}",user);
        return sendResponse(projectService.getAllProjects(user));
    }


    @ApiOperation(value = "Get a single project", notes = "Get a single project specified by a projectId")
    @ApiResponse(code = 200, message = "Success", response = Response.class)
    @GetMapping("/{projectId}")
    public ResponseEntity<Object> getProjectByUser(@PathVariable("projectId") String projectId, @RequestHeader("user") String userId){
        logger.info("HIT - GET /project/ ---> getProjectByUser | projectId: {} | userId: {}", projectId, userId);
        return sendResponse(projectService.getProjectByUser(projectId, userId));
    }

    @ApiOperation(value = "Assign a user to a project", notes = "Assign a user to a project, allocate a role, specify administrator/non-administrator roles")
    @ApiResponse(code = 200, message = "Success", response = Response.class)
    @PostMapping("/{projectId}/users")
    public ResponseEntity<Object> assignUserToProject(@PathVariable("projectId")String projectId, @RequestBody UserAssignDto userAssignDto){
        logger.info("HIT - POST /<projectId>/users ---> assignUserToProject | projectId: {} | dto: {}", projectId, userAssignDto);
        return sendResponse(projectService.assignUserToProject(projectId, userAssignDto));
    }

    @ApiOperation(value = "Update a single project", notes = "Get a single project specified by a projectId")
    @ApiResponse(code = 200, message = "Success", response = Response.class)
    @PutMapping("/{projectId}")
    public ResponseEntity<Object> updateProject(@PathVariable("projectId") String projectId, @RequestBody ProjectEditDto projectEditDto){
        logger.info("HIT - GET /project/ ---> updateProject | projectId: {} | projectUpdateDto: {}", projectId, projectEditDto);
        return sendResponse(projectService.updateProject(projectId, projectEditDto));
    }

    @ApiOperation(value = "Update Project Weight", notes = "Update Project Weight")
    @ApiResponse(code = 200, message = "Success", response = Response.class)
    @PutMapping("/{projectId}/weight")
    public ResponseEntity<Object> updateProjectWeight(@PathVariable("projectId") String projectId, @RequestHeader("userId") String userId, @RequestBody @Valid ProjectWeightUpdateDto projectWeightUpdateDto){
        logger.info("HIT - GET /project/ ---> updateProjectWeight | projectId: {} | projectUpdateDto: {}", projectId, projectWeightUpdateDto);
        return sendResponse(projectService.updateProjectWeight(projectId, userId, projectWeightUpdateDto));
    }


    @ApiOperation(value = "Update a user assigned to a project", notes = "Update job role/project role of an assignee")
    @ApiResponse(code = 200, message = "Success", response = Response.class)
    @PutMapping("/{projectId}/users/{userId}")
    public ResponseEntity<Object> updateAssigneeProjectRole(@PathVariable("projectId")String projectId,@PathVariable("userId") String userId, @RequestBody ProjectUserUpdateDto updateDto){
        logger.info("HIT - PUT /<projectId>/users/<userId> ---> updateAssigneeProjectRole | projectId: {} | userId: {} | dto: {}", projectId, userId, updateDto);
        return sendResponse(projectService.updateAssigneeProjectRole(projectId, userId, updateDto));
    }

    @ApiOperation(value = "Delete a user assigned to a project", notes = "Remove user assignment from a project")
    @ApiResponse(code = 200, message = "Success", response = Response.class)
    @DeleteMapping("/{projectId}/users/{userId}")
    public ResponseEntity<Object> removeProjectAssignee(@PathVariable("projectId")String projectId,@PathVariable("userId") String assignee, @RequestBody ProjectUserDeleteDto deleteDto){
        logger.info("HIT - DELETE /<projectId>/users/<userId> ---> removeProjectAssignee | projectId: {} | userId: {} | dto: {}", projectId, assignee, deleteDto);
        return sendResponse(projectService.removeProjectAssignee(projectId, assignee, deleteDto));
    }

    @ApiOperation(value = "Delete a project", notes = "Flag a project, along with tasks and subtasks")
    @ApiResponse(code = 200, message = "Success", response = Response.class)
    @DeleteMapping("/{projectId}")
    public ResponseEntity<Object> flagProject(@RequestHeader("user") String userId,@PathVariable("projectId")String projectId){
        logger.info("HIT - DELETE /<projectId>/ ---> flagProject | projectId: {} | userId: {}", projectId, userId);
        return sendResponse(projectService.flagProject(userId, projectId));
    }

    @ApiOperation(value = "Block/Unblock a user from a project", notes = "Block/Unblock a user")
    @ApiResponse(code = 200, message = "Success", response = Response.class)
    @PostMapping("/{projectId}/users/{userId}/block")
    public ResponseEntity<Object> blockOrUnBlockProjectUser(@PathVariable("userId") String userId, @PathVariable("projectId") String projectId, @RequestBody ProjectUserBlockDto projectUserBlockDto){
        logger.info("HIT - POST /<projectId>/users/<userId>/block ---> blockOrUnBlockProjectUser | projectId: {} | userId: {} | dto: {}", projectId, userId, projectUserBlockDto);
        return sendResponse(projectService.blockOrUnBlockProjectUser(userId, projectId, projectUserBlockDto));
    }

    @ApiOperation(value = "Add/Update Project Keys", notes = "Add/Update Project Keys")
    @ApiResponse(code = 200, message = "Success", response = Response.class)
    @PutMapping("/{projectId}/keys")
    public ResponseEntity<Object> addOrUpdateProjectKeys(@PathVariable("projectId") String projectId, @Valid @RequestBody ProjectKeys projectKeys){
        logger.info("HIT - PUT /project/<projectId>/keys ---> addOrUpdateProjectKeys | projectId: {} | projectUpdateDto: {}", projectId, projectKeys);
        return sendResponse(projectService.addOrUpdateProjectKeys(projectId, projectKeys));
    }

    @ApiOperation(value = "Get Project Keys", notes = "Get Project Keys")
    @ApiResponse(code = 200, message = "Success", response = Response.class)
    @GetMapping("/{projectId}/keys")
    public ResponseEntity<Object> getProjectKeys(@PathVariable("projectId") String projectId, @RequestHeader("user")String userId){
        logger.info("HIT - GET /project/<projectId>/keys ---> getProjectKeys | projectId: {} | userId: {}", projectId, userId);
        return sendResponse(projectService.getProjectKeys(projectId, userId));
    }

    @ApiOperation(value = "Transition to Support Stage", notes = "Transition to Support Stage")
    @ApiResponse(code = 200, message = "Success", response = Response.class)
    @PostMapping("/support")
    public ResponseEntity<Object> transitionToSupport(@RequestHeader("userId") String userId, @Valid @RequestBody ProjectSupport projectSupport){
        logger.info("HIT - POST /support ---> transitionToSupport | userId: {} | dto: {}",  userId, projectSupport);
        return sendResponse(projectService.transitionToSupport(userId, projectSupport));
    }



}
