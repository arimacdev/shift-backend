package com.arimac.backend.pmtool.projectmanagementtool.controller;


import com.arimac.backend.pmtool.projectmanagementtool.Response.Response;
import com.arimac.backend.pmtool.projectmanagementtool.Response.ResponseController;
import com.arimac.backend.pmtool.projectmanagementtool.Service.ProjectService;
import com.arimac.backend.pmtool.projectmanagementtool.dtos.ProjectDto;
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

    @ApiOperation(value = "Get all Projects", notes = "Get all projects of an organization")
    @ApiResponse(code = 200, message = "Success", response = Response.class)
    @GetMapping
    public ResponseEntity<Object> getAllProjects(@RequestParam("userId")String user){
        logger.info("HIT - GET /projects?userId={} --- getAllProjects",user);
        return sendResponse(projectService.getAllProjects(user));
    }

//    @ApiOperation(value = "Get a single project", notes = "Get a single project specified by a projectId")
//    @ApiResponse(code = 200, message = "Success", response = Response.class)
//    @GetMapping
//    public ResponseEntity<Object> getProject(@PathVariable("projectId") String projectId){
//        logger.info("HIT - /project/{} GET project", projectId);
//        return sendResponse(projectService.getProject());
//    }
}
