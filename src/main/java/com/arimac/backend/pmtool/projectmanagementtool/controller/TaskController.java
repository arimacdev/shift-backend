package com.arimac.backend.pmtool.projectmanagementtool.controller;

import com.arimac.backend.pmtool.projectmanagementtool.Response.Response;
import com.arimac.backend.pmtool.projectmanagementtool.Response.ResponseController;
import com.arimac.backend.pmtool.projectmanagementtool.Service.TaskService;
import com.arimac.backend.pmtool.projectmanagementtool.dtos.TaskDto;
import com.arimac.backend.pmtool.projectmanagementtool.dtos.TaskUpdateDto;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/projects")
public class TaskController extends ResponseController {

    private static final Logger logger = LoggerFactory.getLogger(TaskController.class);

    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @CrossOrigin(origins = "http://localhost:3000")
    @ApiOperation(value = "Add task to a project", notes = "Create tasks for a project")
    @ApiResponse(code = 200, message = "Success", response = Response.class)
    @PostMapping("/{projectId}/tasks")
    public ResponseEntity<Object> addTaskToProject(@PathVariable("projectId") String projectId, @RequestBody TaskDto taskDto){
        logger.info("HIT - POST /projects/<projectId>/tasks ---> addTaskToProject | projectId: {} |  dto: {}", projectId, taskDto);
        return sendResponse(taskService.addTaskToProject(projectId, taskDto));
    }

    @ApiOperation(value = "Get all Tasks of a project of all Users", notes = "Get all Tasks in a project")
    @ApiResponse(code = 200, message = "Success", response = Response.class)
    @GetMapping("/{projectId}/tasks")
    public ResponseEntity<Object> getAllProjectTasksByUser(@RequestParam("userId") String userId, @PathVariable("projectId") String projectId){
        logger.info("HIT - GET /projects/<projectId>/tasks ---> getAllProjectTasksByUser | projectId: {} | userId: {}", projectId, userId);
        return sendResponse(taskService.getAllProjectTasksByUser(userId, projectId));
    }

    @ApiOperation(value = "Get all Tasks of a project assigned to a user", notes = "Get all Tasks in a project")
    @ApiResponse(code = 200, message = "Success", response = Response.class)
    @GetMapping("/{projectId}/tasks/user")
    public ResponseEntity<Object> getAllUserAssignedTask(@RequestParam("userId") String userId, @PathVariable("projectId") String projectId){
        logger.info("HIT - GET /projects/<projectId>/tasks ---> getAllProjectTasksByUser | projectId: {} | userId: {}", projectId, userId);
        return sendResponse(taskService.getAllUserAssignedTasks(userId, projectId));
    }

    @ApiOperation(value = "Get a single Task", notes = "Get single task in a project")
    @ApiResponse(code = 200, message = "Success", response = Response.class)
    @GetMapping("/{projectId}/tasks/{taskId}")
    public ResponseEntity<Object> getProjectTask(@RequestHeader("user") String userId, @PathVariable("projectId") String projectId, @PathVariable("taskId") String taskId){
        logger.info("HIT - GET /projects/<projectId>/tasks/<taskId> ---> getProjectTask | projectId: {} | userId: {} | taskId: {}", projectId, userId, taskId);
        return sendResponse(taskService.getProjectTask(userId, projectId, taskId));
    }

    @ApiOperation(value = "Update a single Task", notes = "Update a single task of a project")
    @ApiResponse(code = 200, message = "Success", response = Response.class)
    @PutMapping("/{projectId}/tasks/{taskId}")
    public ResponseEntity<Object> updateProjectTask(@RequestHeader("user") String userId, @PathVariable("projectId") String projectId, @PathVariable("taskId") String taskId, @RequestBody TaskUpdateDto taskUpdateDto){
        logger.info("HIT - PUT /projects/<projectId>/tasks/<taskId> ---> updateProjectTask | projectId: {} | userId: {} | taskId: {} | taskUpdateDto: {}", projectId, userId, taskId, taskUpdateDto);
        return sendResponse(taskService.updateProjectTask(userId, projectId, taskId, taskUpdateDto));
    }

    @ApiOperation(value = "Delete a single Task", notes = "Delete a single task of a project")
    @ApiResponse(code = 200, message = "Success", response = Response.class)
    @DeleteMapping("/{projectId}/tasks/{taskId}")
    public ResponseEntity<Object> flagProjectTask(@RequestHeader("user") String userId, @PathVariable("projectId") String projectId, @PathVariable("taskId") String taskId){
        logger.info("HIT - DELETE /projects/<projectId>/tasks/<taskId> ---> flagProjectTask | projectId: {} | userId: {} | taskId: {}", projectId, userId, taskId);
        return sendResponse(taskService.flagProjectTask(userId, projectId, taskId));
    }

    @ApiOperation(value = "Get Task completion of a Project By User", notes = "Get Task completion of a Project By User")
    @ApiResponse(code = 200, message = "Success", response = Response.class)
    @GetMapping("/{projectId}/tasks/{userId}/completion")
    public ResponseEntity<Object> getProjectTaskCompletionByUser(@PathVariable("userId") String userId, @PathVariable("projectId") String projectId){
        logger.info("HIT - GET /projects/<projectId>/tasks/<userId>/completion ---> getProjectTaskCompletionByUser | projectId: {} | userId: {} ", projectId, userId);
        return sendResponse(taskService.getProjectTaskCompletionByUser(userId, projectId));
    }

    @ApiOperation(value = "Get Task completion of a Project By User", notes = "Get Task completion of a Project By User")
    @ApiResponse(code = 200, message = "Success", response = Response.class)
    @GetMapping("/{projectId}/tasks/{userId}/completion/details")
    public ResponseEntity<Object> getProjectTaskCompletionUserDetails(@PathVariable("userId") String userId, @PathVariable("projectId") String projectId){
        logger.info("HIT - GET /projects/<projectId>/tasks/<userId>/completion/details ---> getProjectTaskCompletionUserDetails | projectId: {} | userId: {} ", projectId, userId);
        return sendResponse(taskService.getProjectTaskCompletionUserDetails(userId, projectId));
    }

    @ApiOperation(value = "Get Project Task completion status", notes = "Get Task completion of a Project")
    @ApiResponse(code = 200, message = "Success", response = Response.class)
    @GetMapping("/{projectId}/tasks/completion")
    @CrossOrigin(origins = "http://localhost:3000")
    public ResponseEntity<Object> getProjectTaskCompletion(@RequestHeader("user") String userId, @PathVariable("projectId") String projectId){
        logger.info("HIT - GET /projects/<projectId>/tasks/completion ---> getProjectTaskCompletion | projectId: {} | userId: {} ", projectId, userId);
        return sendResponse(taskService.getProjectTaskCompletion(userId, projectId));
    }

}
