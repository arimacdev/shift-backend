package com.arimac.backend.pmtool.projectmanagementtool.controller;

import com.arimac.backend.pmtool.projectmanagementtool.Response.Response;
import com.arimac.backend.pmtool.projectmanagementtool.Response.ResponseController;
import com.arimac.backend.pmtool.projectmanagementtool.Service.TaskGroupService;
import com.arimac.backend.pmtool.projectmanagementtool.dtos.TaskGroup.TaskGroupAddDto;
import com.arimac.backend.pmtool.projectmanagementtool.dtos.TaskGroup.TaskGroupDto;
import com.arimac.backend.pmtool.projectmanagementtool.dtos.TaskGroup.TaskGroupUpdateDto;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
        logger.info("HIT - POST createTaskGroup - /taskgroup POST  dto: {}", taskGroupDto);
        return sendResponse(taskGroupService.createTaskGroup(taskGroupDto));
    }

    @ApiOperation(value = "Task Group Retrieve By User", notes = "Create a Task Group Create")
    @ApiResponse(code = 200, message = "Success", response = Response.class)
    @GetMapping
    public ResponseEntity<Object> getAllTaskGroupsByUser(@RequestHeader("user") String userId){
        logger.info("HIT - GET getAllTaskGroupsByUser - /taskgroup GET  userId: {}", userId);
        return sendResponse(taskGroupService.getAllTaskGroupsByUser(userId));
    }

    @ApiOperation(value = "Add Members to Task Group", notes = "Create a Task Group Create")
    @ApiResponse(code = 200, message = "Success", response = Response.class)
    @PostMapping("/add")
    public ResponseEntity<Object> addMembersToTaskGroup(@RequestBody TaskGroupAddDto taskGroupAddDto){
        logger.info("HIT - POST addMembersToTaskGroup - /taskgroup/add  dto: {}", taskGroupAddDto);
        return sendResponse(taskGroupService.addMembersToTaskGroup(taskGroupAddDto));
    }

    @ApiOperation(value = "Update Task Group", notes = "Create a Task Group Create")
    @ApiResponse(code = 200, message = "Success", response = Response.class)
    @PutMapping("/{taskGroupId}")
    public ResponseEntity<Object> updateTaskGroup(@PathVariable("taskGroupId") String taskGroupId, @RequestBody TaskGroupUpdateDto taskGroupUpdateDto){
        logger.info("HIT - PUT addMembersToTaskGroup - /taskgroup/<taskGroupId> | taskGroupId {} |  dto: {}", taskGroupUpdateDto, taskGroupId);
        return sendResponse(taskGroupService.updateTaskGroup(taskGroupId, taskGroupUpdateDto));
    }

    @ApiOperation(value = "Flag Task Group", notes = "Flag Task Group")
    @ApiResponse(code = 200, message = "Success", response = Response.class)
    @DeleteMapping("/{taskGroupId}")
    public ResponseEntity<Object> flagTaskGroup(@PathVariable("taskGroupId") String taskGroupId, @RequestHeader("user") String userId){
        logger.info("HIT - DELETE flagTaskGroup - /taskgroup/<taskGroupId> | taskGroupId {} | ownerId {}", taskGroupId, userId);
        return sendResponse(taskGroupService.flagTaskGroup(taskGroupId, userId));
    }




}
