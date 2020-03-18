package com.arimac.backend.pmtool.projectmanagementtool.controller;


import com.arimac.backend.pmtool.projectmanagementtool.Response.Response;
import com.arimac.backend.pmtool.projectmanagementtool.Response.ResponseController;
import com.arimac.backend.pmtool.projectmanagementtool.Service.ProjectService;
import com.arimac.backend.pmtool.projectmanagementtool.dtos.*;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


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
    public ResponseEntity<Object> createProject(@RequestBody ProjectDto projectDto){
        logger.info("HIT - createProject - /projects POST  dto: {}", projectDto);
        return sendResponse(projectService.createProject(projectDto));
    }

    @ApiOperation(value = "Get all Projects by user", notes = "Get all projects of an organization")
    @ApiResponse(code = 200, message = "Success", response = Response.class)
    @GetMapping
    public ResponseEntity<Object> getAllProjectsByUser(@RequestParam("userId")String user){
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

    @ApiOperation(value = "Get a single project", notes = "Get a single project specified by a projectId")
    @ApiResponse(code = 200, message = "Success", response = Response.class)
    @PutMapping("/{projectId}")
    public ResponseEntity<Object> updateProject(@PathVariable("projectId") String projectId, @RequestBody ProjectEditDto projectEditDto){
        logger.info("HIT - GET /project/ ---> updateProject | projectId: {} | projectUpdateDto: {}", projectId, projectEditDto);
        return sendResponse(projectService.updateProject(projectId, projectEditDto));
    }


    @ApiOperation(value = "Update a user assigned to a project", notes = "Update job role/project role of an assignee")
    @ApiResponse(code = 200, message = "Success", response = Response.class)
    @PutMapping("/{projectId}/users/{userId}")
    @CrossOrigin(origins = "http://localhost:3000")
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

    @CrossOrigin(origins = "http://localhost:3000")
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
    @CrossOrigin(origins = "http://localhost:3000")
    public ResponseEntity<Object> blockOrUnBlockProjectUser(@PathVariable("userId") String userId, @PathVariable("projectId") String projectId, @RequestBody ProjectUserBlockDto projectUserBlockDto){
        logger.info("HIT - POST /<projectId>/users/<userId>/block ---> blockOrUnBlockProjectUser | projectId: {} | userId: {} | dto: {}", projectId, userId, projectUserBlockDto);
        return sendResponse(projectService.blockOrUnBlockProjectUser(userId, projectId, projectUserBlockDto));
    }



}
