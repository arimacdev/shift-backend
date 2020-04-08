package com.arimac.backend.pmtool.projectmanagementtool.controller;

import com.arimac.backend.pmtool.projectmanagementtool.Response.Response;
import com.arimac.backend.pmtool.projectmanagementtool.Response.ResponseController;
import com.arimac.backend.pmtool.projectmanagementtool.Service.TaskGroupService;
import com.arimac.backend.pmtool.projectmanagementtool.dtos.TaskGroup.TaskGroupDto;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/taskgroup")
public class TaskGroupController extends ResponseController {

    private static final Logger logger = LoggerFactory.getLogger(TaskGroupController.class);

    private final TaskGroupService taskGroupService;

    public TaskGroupController(TaskGroupService taskGroupService) {
        this.taskGroupService = taskGroupService;
    }

    @ApiOperation(value = "Task Group Create", notes = "Create a Task Group Create")
    @ApiResponse(code = 200, message = "Success", response = Response.class)
    @PostMapping
    public ResponseEntity<Object> createTaskGroup(@RequestBody TaskGroupDto taskGroupDto){
        logger.info("HIT - createTaskGroup - /taskgroup POST  dto: {}", taskGroupDto);
        return sendResponse(taskGroupService.createTaskGroup(taskGroupDto));
    }


}
