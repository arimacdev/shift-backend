package com.arimac.backend.pmtool.projectmanagementtool.controller;

import com.arimac.backend.pmtool.projectmanagementtool.Response.Response;
import com.arimac.backend.pmtool.projectmanagementtool.Response.ResponseController;
import com.arimac.backend.pmtool.projectmanagementtool.Service.SubTaskService;
import com.arimac.backend.pmtool.projectmanagementtool.dtos.SubTaskDto;
import com.arimac.backend.pmtool.projectmanagementtool.dtos.TaskDto;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/projects")
public class SubTaskController extends ResponseController {

    private static final Logger logger = LoggerFactory.getLogger(SubTaskController.class);

    private final SubTaskService subTaskService;

    public SubTaskController(SubTaskService subTaskService) {
        this.subTaskService = subTaskService;
    }

    @ApiOperation(value = "Add sub-task to a project", notes = "Create a sub-task for a project")
    @ApiResponse(code = 200, message = "Success", response = Response.class)
    @PostMapping("/{projectId}/tasks/{taskId}/subtask")
    public ResponseEntity<Object> addSubTaskToProject(@PathVariable("projectId") String projectId, @PathVariable("taskId") String taskId, @RequestBody SubTaskDto subTaskDto){
        logger.info("HIT - POST /projects/<projectId>/tasks/<taskId>/subtask ---> addSubTaskToProject | projectId: {} |  taskId: {} | subTaskDto {}", projectId, taskId, subTaskDto);
        return sendResponse(subTaskService.addSubTaskToProject(projectId, taskId, subTaskDto));
    }


}
