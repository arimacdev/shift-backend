package com.arimac.backend.pmtool.projectmanagementtool.controller;

import com.arimac.backend.pmtool.projectmanagementtool.Response.Response;
import com.arimac.backend.pmtool.projectmanagementtool.Response.ResponseController;
import com.arimac.backend.pmtool.projectmanagementtool.Service.SprintService;
import com.arimac.backend.pmtool.projectmanagementtool.dtos.Sprint.SprintDto;
import com.arimac.backend.pmtool.projectmanagementtool.dtos.Sprint.SprintUpdateDto;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/sprints")
public class SprintController extends ResponseController {
    private static final Logger logger = LoggerFactory.getLogger(SprintController.class);

    private final SprintService sprintService;

    public SprintController(SprintService sprintService) {
        this.sprintService = sprintService;
    }

    @ApiOperation(value = "Create a sprint", notes = "Create a sprint for a project")
    @ApiResponse(code = 200, message = "Success", response = Response.class)
    @PostMapping
    public ResponseEntity<Object> createSprint(@RequestBody SprintDto sprintDto){
        logger.info("HIT - POST createSprint - /sprints POST  dto: {}", sprintDto);
        return sendResponse(sprintService.createSprint(sprintDto));
    }

    @ApiOperation(value = "Get All Sprints of a Project", notes = "Get All Sprints of a Project")
    @ApiResponse(code = 200, message = "Success", response = Response.class)
    @GetMapping("/{projectId}")
    public ResponseEntity<Object> getAllProjectSprints(@PathVariable("projectId") String projectId, @RequestHeader("userId") String userId){
        logger.info("HIT - GET getAllProjectSprints - /sprints/<sprintId> POST  projectId: {} | userId: {}", projectId, userId);
        return sendResponse(sprintService.getAllProjectSprints(userId, projectId));
    }

    @ApiOperation(value = "Update a sprint of a Project", notes = "Update Sprint of a Project")
    @ApiResponse(code = 200, message = "Success", response = Response.class)
    @PutMapping("/{projectId}/{sprintId}")
    public ResponseEntity<Object> updateSprint(@PathVariable("projectId") String projectId, @PathVariable("sprintId") String sprintId, @RequestHeader("userId") String userId, @RequestBody SprintUpdateDto sprintUpdateDto){
        logger.info("HIT - GET getAllProjectSprints - /sprints/<sprintId> POST  userId: {} | projectId: {} | sprintId: {} | sprintUpdateDto {}", userId, projectId, sprintId, sprintUpdateDto);
        return sendResponse(sprintService.updateSprint(userId, projectId, sprintId, sprintUpdateDto));
    }


}
