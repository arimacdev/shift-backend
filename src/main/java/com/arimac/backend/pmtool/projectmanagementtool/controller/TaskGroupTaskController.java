package com.arimac.backend.pmtool.projectmanagementtool.controller;

import com.arimac.backend.pmtool.projectmanagementtool.Response.Response;
import com.arimac.backend.pmtool.projectmanagementtool.Response.ResponseController;
import com.arimac.backend.pmtool.projectmanagementtool.Service.TaskGroupTaskService;
import com.arimac.backend.pmtool.projectmanagementtool.dtos.Task.TaskParentChildUpdateDto;
import com.arimac.backend.pmtool.projectmanagementtool.dtos.TaskGroupTask.TaskGroupTaskDto;
import com.arimac.backend.pmtool.projectmanagementtool.dtos.TaskGroupTask.TaskGroupTaskUpdateDto;
import com.arimac.backend.pmtool.projectmanagementtool.dtos.TaskUpdateDto;
import com.arimac.backend.pmtool.projectmanagementtool.enumz.TaskTypeEnum;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/taskgroup")
public class TaskGroupTaskController extends ResponseController {
    private static final Logger logger = LoggerFactory.getLogger(TaskGroupTaskController.class);

    private final TaskGroupTaskService taskGroupTaskService;

    public TaskGroupTaskController(TaskGroupTaskService taskGroupTaskService) {
        this.taskGroupTaskService = taskGroupTaskService;
    }

    @ApiOperation(value = "Add taskgroup task", notes = "Create a taskgroup task")
    @ApiResponse(code = 200, message = "Success", response = Response.class)
    @PostMapping("/{taskgroupId}/task")
    public ResponseEntity<Object> addTaskGroupTask(@PathVariable("taskgroupId") String taskgroupId, @RequestBody TaskGroupTaskDto taskDto){
        logger.info("HIT - POST /taskgroup/<taskgroupId>/task ---> addTaskGroupTask taskgroupId: {} dto: {}", taskgroupId, taskDto);
        return sendResponse(taskGroupTaskService.addTaskGroupTask(taskgroupId, taskDto));
    }

    @ApiOperation(value = "Get a single Task", notes = "Get single task in a taskGroup")
    @ApiResponse(code = 200, message = "Success", response = Response.class)
    @GetMapping("/{taskgroupId}/tasks/{taskId}")
    public ResponseEntity<Object> getTaskGroupTask(@RequestHeader("user") String userId, @PathVariable("taskgroupId") String taskgroupId, @PathVariable("taskId") String taskId){
        logger.info("HIT - GET /taskgroup/<taskgroupId>/tasks/<taskId> ---> getProjectTask | taskgroupId: {} | userId: {} | taskId: {}", taskgroupId, userId, taskId);
        return sendResponse(taskGroupTaskService.getTaskGroupTask(userId, taskgroupId, taskId));
    }

    @ApiOperation(value = "Update a single Task", notes = "Update a single task of a taskGroup")
    @ApiResponse(code = 200, message = "Success", response = Response.class)
    @PutMapping("/{taskgroupId}/tasks/{taskId}")
    public ResponseEntity<Object> updateTaskGroupTask(@RequestHeader("user") String userId, @PathVariable("taskgroupId") String taskgroupId, @PathVariable("taskId") String taskId, @RequestBody TaskGroupTaskUpdateDto taskUpdateDto){
        logger.info("HIT - PUT /taskgroup/<taskgroupId>/tasks/<taskId> ---> updateTaskGroupTask | taskgroupId: {} | userId: {} | taskId: {} | taskUpdateDto: {}", taskgroupId, userId, taskId, taskUpdateDto);
        return sendResponse(taskGroupTaskService.updateTaskGroupTask(userId, taskgroupId, taskId, taskUpdateDto));
    }

    @ApiOperation(value = "Delete a single Task", notes = "Delete a single task of a taskGroup")
    @ApiResponse(code = 200, message = "Success", response = Response.class)
    @DeleteMapping("/{taskgroupId}/tasks/{taskId}")
    public ResponseEntity<Object> flagTaskGroupTask(@RequestHeader("user") String userId, @PathVariable("taskgroupId") String taskgroupId, @PathVariable("taskId") String taskId){
        logger.info("HIT - DELETE /taskgroup/<taskgroupId>/tasks/<taskId> ---> flagTaskGroupTask | taskgroupId: {} | userId: {} | taskId: {}", taskgroupId, userId, taskId);
        return sendResponse(taskGroupTaskService.flagTaskGroupTask(userId, taskgroupId, taskId));
    }

    @ApiOperation(value = "Get all Tasks of a of all Users", notes = "(All Tasks) Get all Tasks in a Task Group")
    @ApiResponse(code = 200, message = "Success", response = Response.class)
    @GetMapping("/{taskgroupId}/tasks")
    public ResponseEntity<Object> getAllTaskGroupTasksByUser(@RequestParam("userId") String userId, @PathVariable("taskgroupId") String taskgroupId){
        logger.info("HIT - GET /taskgroup/<taskgroupId>/tasks ---> getAllTaskGroupTasksByUser | taskgroupId: {} | userId: {}", taskgroupId, userId);
        return sendResponse(taskGroupTaskService.getAllTaskGroupTasksByUser(userId, taskgroupId));
    }

    @ApiOperation(value = "Get Task completion of an TaskGroup By User", notes = "(People Tab) Get Task completion of a TaskGroup By User")
    @ApiResponse(code = 200, message = "Success", response = Response.class)
    @GetMapping("/{taskgroupId}/tasks/{userId}/completion/details")
    public ResponseEntity<Object> getTaskCompletionUserDetails(@PathVariable("userId") String userId, @PathVariable("taskgroupId") String taskgroupId){
        logger.info("HIT - GET /taskgroup/<taskgroupId>/tasks/<userId>/completion/details ---> getTaskCompletionUserDetails | taskgroupId: {} | userId: {}", taskgroupId, userId);
        return sendResponse(taskGroupTaskService.getTaskCompletionUserDetails(userId, taskgroupId));
    }

    @ApiOperation(value = "Get all Tasks of a TaskGroup assigned to a user", notes = "(My Tasks) Get all Tasks in a TaskGroup assigned to user")
    @ApiResponse(code = 200, message = "Success", response = Response.class)
    @GetMapping("/{taskgroupId}/tasks/user")
    public ResponseEntity<Object> getAllUserAssignedTasks(@RequestParam("userId") String userId, @PathVariable("taskgroupId") String taskgroupId){
        logger.info("HIT - GET /taskgroup/<taskgroupId>/tasks ---> getAllUserAssignedTasks | projectId: {} | userId: {}", taskgroupId, userId);
        return sendResponse(taskGroupTaskService.getAllUserAssignedTasks(userId, taskgroupId));
    }

    @ApiOperation(value = "Get a files of a single Task", notes = "Get file of  single task in a TaskGroup")
    @ApiResponse(code = 200, message = "Success", response = Response.class)
    @GetMapping("/{taskgroupId}/tasks/{taskId}/files")
    public ResponseEntity<Object> getTaskGroupTaskFiles(@RequestHeader("user") String userId, @PathVariable("taskgroupId") String taskgroupId, @PathVariable("taskId") String taskId){
        logger.info("HIT - GET /taskgroup/<taskgroupId>/tasks/<taskId>/files ---> getTaskGroupTaskFiles | taskgroupId: {} | userId: {} | taskId: {}", taskgroupId, userId, taskId);
        return sendResponse(taskGroupTaskService.getProjectTaskFiles(userId, taskgroupId, taskId));
    }

    @ApiOperation(value = "Transition from Parent to Child Task", notes = "Transition from Parent to Child Task")
    @ApiResponse(code = 200, message = "Success", response = Response.class)
    @PutMapping("/{taskgroupId}/tasks/{taskId}/parent/transition")
    public ResponseEntity<Object> transitionFromParentToChild(@RequestHeader("user") String userId, @PathVariable("taskgroupId") String taskgroupId, @PathVariable("taskId") String taskId, @RequestBody TaskParentChildUpdateDto taskParentChildUpdateDto){
        logger.info("HIT - PUT /<taskgroupId>/tasks/<taskId>/parent/transition ---> transitionFromParentToChild | taskgroupId: {} | userId: {} | taskId: {} | TaskParentChildUpdateDto: {}", taskgroupId, userId, taskId, taskParentChildUpdateDto);
        return sendResponse(taskGroupTaskService.transitionFromParentToChild(userId, taskgroupId, taskId, taskParentChildUpdateDto));
    }

    @ApiOperation(value = "Get Children of a Parent Task", notes = "Get Children of a Parent Task")
    @ApiResponse(code = 200, message = "Success", response = Response.class)
    @GetMapping("/{taskgroupId}/tasks/{taskId}/children")
    public ResponseEntity<Object> getAllChildrenOfParentTask(@RequestHeader("user") String userId, @PathVariable("taskgroupId") String taskgroupId, @PathVariable("taskId") String taskId){
        logger.info("HIT - GET /<taskgroupId>/tasks/<taskId>/children ---> getAllChildrenOfParentTask | taskgroupId: {} | userId: {} | taskId: {}", taskgroupId, userId, taskId);
        return sendResponse(taskGroupTaskService.getAllChildrenOfParentTask(userId, taskgroupId, taskId));
    }


}
