package com.arimac.backend.pmtool.projectmanagementtool.controller;

import com.arimac.backend.pmtool.projectmanagementtool.Response.Response;
import com.arimac.backend.pmtool.projectmanagementtool.Response.ResponseController;
import com.arimac.backend.pmtool.projectmanagementtool.Service.NpTaskService;
import com.arimac.backend.pmtool.projectmanagementtool.dtos.SubTaskDto;
import com.arimac.backend.pmtool.projectmanagementtool.dtos.SubTaskUpdateDto;
import com.arimac.backend.pmtool.projectmanagementtool.dtos.TaskDto;
import com.arimac.backend.pmtool.projectmanagementtool.dtos.TaskUpdateDto;
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

    @ApiOperation(value = "Get all personal tasks", notes = "Get all personal tasks")
    @ApiResponse(code = 200, message = "Success", response = Response.class)
    @GetMapping("/tasks/personal/{userId}")
    public ResponseEntity<Object> getAllPersonalTasks(@PathVariable String userId){
        logger.info("HIT - GET /non-project/tasks/personal/{} ---> getAllPersonalTasks", userId);
        return sendResponse(npTaskService.getAllPersonalTasks(userId));
    }

    @ApiOperation(value = "Update a personal task", notes = "Update a personal task a project")
    @ApiResponse(code = 200, message = "Success", response = Response.class)
    @PutMapping("/tasks/personal/{taskId}")// DONE
    public ResponseEntity<Object> updatePersonalTask(@PathVariable("taskId") String taskId, @RequestHeader("user") String user, @RequestBody TaskUpdateDto taskUpdateDto){
        logger.info("HIT - PUT  /non-project/tasks/personal/<taskId> ---> updatePersonalTask | userId: {} | taskId: {} | taskUpdateDto: {}",user, taskId,  taskUpdateDto);
        return sendResponse(npTaskService.updatePersonalTask(user,  taskId, taskUpdateDto));
    }


    @ApiOperation(value = "Add sub-task to a personal task", notes = "Create a sub-task for a task")
    @ApiResponse(code = 200, message = "Success", response = Response.class)
    @PostMapping("/tasks/personal/{taskId}/subtask")
    public ResponseEntity<Object> addSubTaskToPersonalTask(@PathVariable("taskId") String taskId, @RequestBody SubTaskDto subTaskDto){
        logger.info("HIT - POST tasks/personal/<taskId>/subtask ---> addSubTaskToPersonalTask |  taskId: {} | subTaskDto {}", taskId, subTaskDto);
        return sendResponse(npTaskService.addSubTaskToPersonalTask(taskId, subTaskDto));
    }

    @ApiOperation(value = "Get all subtasks of a task", notes = "Get all subtasks belonging to a task")
    @ApiResponse(code = 200, message = "Success", response = Response.class)
    @GetMapping("/tasks/personal/{taskId}/subtask")
    public ResponseEntity<Object> getAllSubTaksOfATask(@RequestParam("userId") String userId, @PathVariable("taskId") String taskId){
        logger.info("HIT - GET /tasks/personal/<taskId>/subtask ---> getAllSubTaksOfATask | taskId: {} | userId {}",  taskId, userId);
        return sendResponse(npTaskService.getAllSubTaksOfATask(userId, taskId));
    }

    @ApiOperation(value = "Update SubTask", notes = "Update SubTask of a Task")
    @ApiResponse(code = 200, message = "Success", response = Response.class)
    @PutMapping("/tasks/personal/{taskId}/subtask/{subtaskId}")
    public ResponseEntity<Object> updateSubTaskOfATask(@RequestHeader("user") String user, @PathVariable("taskId") String taskId, @PathVariable("subtaskId") String subtaskId,@RequestBody SubTaskUpdateDto subTaskUpdateDto){
        logger.info("HIT - HIT - GET /tasks/personal/<taskId>/subtask/<subtaskId>  ---> updateSubTaskOfATask |  taskId: {} | subtaskId {} | subTaskUpdateDto {}" ,  taskId, subtaskId, subTaskUpdateDto);
        return sendResponse(npTaskService.updateSubTaskOfATask(user, taskId, subtaskId, subTaskUpdateDto));
    }

    @ApiOperation(value = "Get a files of a personal Task", notes = "Get file of  single task in a project")
    @ApiResponse(code = 200, message = "Success", response = Response.class)
    @GetMapping("/tasks/personal/{taskId}/files")
    public ResponseEntity<Object> getProjectTaskFiles(@RequestHeader("user") String userId, @PathVariable("taskId") String taskId){
        logger.info("HIT - GET /tasks/personal/tasks/<taskId>/files ---> getPersonalTaskFiles  | userId: {} | taskId: {}",  userId, taskId);
        return sendResponse(npTaskService.getPersonalTaskFiles(userId, taskId));
    }

//
//    @ApiOperation(value = "Flag a  SubTask", notes = "Flag a  SubTask of a Task")
//    @ApiResponse(code = 200, message = "Success", response = Response.class)
//    @DeleteMapping("/{projectId}/tasks/{taskId}/subtask/{subtaskId}")
//    public ResponseEntity<Object> flagSubTaskOfATask(@RequestHeader("user") String user, @PathVariable("projectId") String projectId, @PathVariable("taskId") String taskId, @PathVariable("subtaskId") String subtaskId){
//        logger.info("HIT - DELETE /projects/<projectId>/tasks/<taskId>/subtask/<subTaskId> ---> flagSubTaskOfATask | projectId: {} |  taskId: {} | subtaskId {}" , projectId, taskId, subtaskId);
//        return sendResponse(subTaskService.flagSubTaskOfATask(user,projectId, taskId, subtaskId));
//    }

}
