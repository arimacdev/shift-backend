package com.arimac.backend.pmtool.projectmanagementtool.controller;

import com.arimac.backend.pmtool.projectmanagementtool.Response.Response;
import com.arimac.backend.pmtool.projectmanagementtool.Response.ResponseController;
import com.arimac.backend.pmtool.projectmanagementtool.Service.NpTaskService;
import com.arimac.backend.pmtool.projectmanagementtool.dtos.TaskDto;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/non-project")
public class NPTaskController extends ResponseController {

    private static final Logger logger = LoggerFactory.getLogger(TaskController.class);

    private final NpTaskService npTaskService;

    public NPTaskController(NpTaskService npTaskService) {
        this.npTaskService = npTaskService;
    }

    @ApiOperation(value = "Add non project task", notes = "Create a non project task")
    @ApiResponse(code = 200, message = "Success", response = Response.class)
    @PostMapping("/tasks/personal")
    public ResponseEntity<Object> addPersonalTask(@RequestBody TaskDto taskDto){
        logger.info("HIT - POST /non-project/tasks/personal ---> addPersonalTask {}", taskDto);
        return sendResponse(npTaskService.addPersonalTask(taskDto));
    }

    @ApiOperation(value = "Add non project task", notes = "Create a non project task")
    @ApiResponse(code = 200, message = "Success", response = Response.class)
    @PostMapping("/tasks/personal/{userId}")
    public ResponseEntity<Object> getAllPersonalTasks(@PathVariable String userId){
        logger.info("HIT - POST /non-project/tasks/personal/{} ---> getAllPersonalTasks", userId);
        return sendResponse(npTaskService.getAllPersonalTasks(userId));
    }
}
