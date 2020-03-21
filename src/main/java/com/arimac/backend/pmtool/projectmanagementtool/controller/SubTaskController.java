package com.arimac.backend.pmtool.projectmanagementtool.controller;

import com.arimac.backend.pmtool.projectmanagementtool.Response.Response;
import com.arimac.backend.pmtool.projectmanagementtool.Response.ResponseController;
import com.arimac.backend.pmtool.projectmanagementtool.Service.SubTaskService;
import com.arimac.backend.pmtool.projectmanagementtool.dtos.SubTaskDto;
import com.arimac.backend.pmtool.projectmanagementtool.dtos.SubTaskUpdateDto;
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

    @ApiOperation(value = "Add sub-task to a task", notes = "Create a sub-task for a task")
    @ApiResponse(code = 200, message = "Success", response = Response.class)
    @PostMapping("/{projectId}/tasks/{taskId}/subtask")
    public ResponseEntity<Object> addSubTaskToProject(@PathVariable("projectId") String projectId, @PathVariable("taskId") String taskId, @RequestBody SubTaskDto subTaskDto){
        logger.info("HIT - POST /projects/<projectId>/tasks/<taskId>/subtask ---> addSubTaskToProject | projectId: {} |  taskId: {} | subTaskDto {}", projectId, taskId, subTaskDto);
        return sendResponse(subTaskService.addSubTaskToProject(projectId, taskId, subTaskDto));
    }

    @ApiOperation(value = "Get all subtasks of a task", notes = "Get all subtasks belonging to a task")
    @ApiResponse(code = 200, message = "Success", response = Response.class)
    @GetMapping("/{projectId}/tasks/{taskId}/subtask")
    public ResponseEntity<Object> getAllSubTaksOfATask(@RequestParam("userId") String userId, @PathVariable("projectId") String projectId, @PathVariable("taskId") String taskId){
        logger.info("HIT - POST /projects/<projectId>/tasks/<taskId>/subtask?userId={userId} ---> getAllSubTaksOfATask | projectId: {} |  taskId: {} | userId {}", projectId, taskId, userId);
        return sendResponse(subTaskService.getAllSubTaksOfATask(userId, projectId, taskId));
    }

    @ApiOperation(value = "Update SubTask", notes = "Update SubTask of a Task")
    @ApiResponse(code = 200, message = "Success", response = Response.class)
    @PutMapping("/{projectId}/tasks/{taskId}/subtask/{subtaskId}")
    public ResponseEntity<Object> updateSubTaskOfATask(@RequestHeader("user") String user, @PathVariable("projectId") String projectId, @PathVariable("taskId") String taskId, @PathVariable("subtaskId") String subtaskId,@RequestBody SubTaskUpdateDto subTaskUpdateDto){
        logger.info("HIT - PUT /projects/<projectId>/tasks/<taskId>/subtask/<subTaskId> ---> updateSubTaskOfATask | projectId: {} |  taskId: {} | subtaskId {} | subTaskUpdateDto {}" , projectId, taskId, subtaskId, subTaskUpdateDto);
        return sendResponse(subTaskService.updateSubTaskOfATask(user,projectId, taskId, subtaskId, subTaskUpdateDto));
    }

    @ApiOperation(value = "Flag a  SubTask", notes = "Flag a  SubTask of a Task")
    @ApiResponse(code = 200, message = "Success", response = Response.class)
    @DeleteMapping("/{projectId}/tasks/{taskId}/subtask/{subtaskId}")
    public ResponseEntity<Object> flagSubTaskOfATask(@RequestHeader("user") String user, @PathVariable("projectId") String projectId, @PathVariable("taskId") String taskId, @PathVariable("subtaskId") String subtaskId){
        logger.info("HIT - DELETE /projects/<projectId>/tasks/<taskId>/subtask/<subTaskId> ---> flagSubTaskOfATask | projectId: {} |  taskId: {} | subtaskId {}" , projectId, taskId, subtaskId);
        return sendResponse(subTaskService.flagSubTaskOfATask(user,projectId, taskId, subtaskId));
    }



}
