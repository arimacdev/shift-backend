package com.arimac.backend.pmtool.projectmanagementtool.controller;

import com.arimac.backend.pmtool.projectmanagementtool.Response.Response;
import com.arimac.backend.pmtool.projectmanagementtool.Response.ResponseController;
import com.arimac.backend.pmtool.projectmanagementtool.Service.TaskService;
import com.arimac.backend.pmtool.projectmanagementtool.dtos.Sprint.SprintUpdateDto;
import com.arimac.backend.pmtool.projectmanagementtool.dtos.Sprint.TaskSprintUpdateDto;
import com.arimac.backend.pmtool.projectmanagementtool.dtos.TaskDto;
import com.arimac.backend.pmtool.projectmanagementtool.dtos.TaskUpdateDto;
import com.arimac.backend.pmtool.projectmanagementtool.enumz.TaskTypeEnum;
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

    @ApiOperation(value = "Add task to a project", notes = "Create tasks for a project")
    @ApiResponse(code = 200, message = "Success", response = Response.class)
    @PostMapping("/{projectId}/tasks") // DONE
    public ResponseEntity<Object> addTaskToProject(@PathVariable("projectId") String projectId, @RequestBody TaskDto taskDto){
        logger.info("HIT - POST /projects/<projectId>/tasks ---> addTaskToProject | projectId: {} |  dto: {}", projectId, taskDto);
        return sendResponse(taskService.addTaskToProject(projectId, taskDto));
    }

    @ApiOperation(value = "Get a single Task", notes = "Get single task in a project")
    @ApiResponse(code = 200, message = "Success", response = Response.class)
    @GetMapping("/{projectId}/tasks/{taskId}") // DONE
    public ResponseEntity<Object> getProjectTask(@RequestHeader("user") String userId, @RequestHeader("type") TaskTypeEnum type, @PathVariable("projectId") String projectId, @PathVariable("taskId") String taskId){
        logger.info("HIT - GET /projects/<projectId>/tasks/<taskId> ---> getProjectTask | projectId: {} | userId: {} | taskId: {} | type:{}", projectId, userId, taskId, type);
        return sendResponse(taskService.getProjectTask(userId, projectId, taskId, type));
    }

    @ApiOperation(value = "Get a files of a single Task", notes = "Get file of  single task in a project")
    @ApiResponse(code = 200, message = "Success", response = Response.class)
    @GetMapping("/{projectId}/tasks/{taskId}/files") //DONE
    public ResponseEntity<Object> getProjectTaskFiles(@RequestHeader("user") String userId, @RequestHeader("type") TaskTypeEnum taskType, @PathVariable("projectId") String projectId, @PathVariable("taskId") String taskId){
        logger.info("HIT - GET /projects/<projectId>/tasks/<taskId>/files ---> getProjectTaskFiles | projectId: {} | userId: {} | taskId: {}| taskType: {}", projectId, userId, taskId, taskType);
        return sendResponse(taskService.getProjectTaskFiles(userId, projectId, taskId, taskType));
    }


    @ApiOperation(value = "Update a single Task", notes = "Update a single task of a project")
    @ApiResponse(code = 200, message = "Success", response = Response.class)
    @PutMapping("/{projectId}/tasks/{taskId}")// DONE
    public ResponseEntity<Object> updateProjectTask(@RequestHeader("user") String userId, @PathVariable("projectId") String projectId, @PathVariable("taskId") String taskId, @RequestBody TaskUpdateDto taskUpdateDto){
        logger.info("HIT - PUT /projects/<projectId>/tasks/<taskId> ---> updateProjectTask | projectId: {} | userId: {} | taskId: {} | taskUpdateDto: {}", projectId, userId, taskId, taskUpdateDto);
        return sendResponse(taskService.updateProjectTask(userId, projectId, taskId, taskUpdateDto));
    }

    @ApiOperation(value = "Delete a single Task", notes = "Delete a single task of a project")
    @ApiResponse(code = 200, message = "Success", response = Response.class)
    @DeleteMapping("/{projectId}/tasks/{taskId}")
    public ResponseEntity<Object> flagProjectTask(@RequestHeader("user") String userId, @PathVariable("projectId") String projectId, @PathVariable("taskId") String taskId, @RequestHeader("type") TaskTypeEnum taskType){
        logger.info("HIT - DELETE /projects/<projectId>/tasks/<taskId> ---> flagProjectTask | projectId: {} | userId: {} | taskId: {}| type: {}", projectId, userId, taskId, taskType);
        return sendResponse(taskService.flagProjectTask(userId, projectId, taskId, taskType));
    }

//    @ApiOperation(value = "Get Task completion of a Project By User", notes = "Get Task completion of a Project By User")
//    @ApiResponse(code = 200, message = "Success", response = Response.class)
//    @GetMapping("/{projectId}/tasks/{userId}/completion")
//    public ResponseEntity<Object> getProjectTaskCompletionByUser(@PathVariable("userId") String userId, @PathVariable("projectId") String projectId){
//        logger.info("HIT - GET /projects/<projectId>/tasks/<userId>/completion ---> getProjectTaskCompletionByUser | projectId: {} | userId: {} ", projectId, userId);
//        return sendResponse(taskService.getProjectTaskCompletionByUser(userId, projectId));
//    }

    @ApiOperation(value = "Get all Tasks of a project of all Users", notes = "(All Tasks) Get all Tasks in a project")
    @ApiResponse(code = 200, message = "Success", response = Response.class)
    @GetMapping("/{projectId}/tasks") //DONE
    public ResponseEntity<Object> getAllProjectTasksByUser(@RequestParam("userId") String userId, @RequestHeader("type") TaskTypeEnum type, @PathVariable("projectId") String projectId){
        logger.info("HIT - GET /projects/<projectId>/tasks ---> getAllProjectTasksByUser | projectId: {} | userId: {} || type: {}", projectId, userId, type);
        return sendResponse(taskService.getAllProjectTasksByUser(userId, projectId, type));
    }

    @ApiOperation(value = "Get all Tasks of a project assigned to a user", notes = "(My Tasks) Get all Tasks in a project assigned to user")
    @ApiResponse(code = 200, message = "Success", response = Response.class)
    @GetMapping("/{projectId}/tasks/user")
    public ResponseEntity<Object> getAllUserAssignedTask(@RequestParam("userId") String userId, @PathVariable("projectId") String projectId){
        logger.info("HIT - GET /projects/<projectId>/tasks ---> getAllProjectTasksByUser | projectId: {} | userId: {}", projectId, userId);
        return sendResponse(taskService.getAllUserAssignedTasks(userId, projectId));
    }

    @ApiOperation(value = "Get Task completion of an Entity By User", notes = "(People Tab) Get Task completion of a Project By User")
    @ApiResponse(code = 200, message = "Success", response = Response.class)
    @GetMapping("/{projectId}/tasks/{userId}/completion/details") // DONE
    public ResponseEntity<Object> getProjectTaskCompletionUserDetails(@PathVariable("userId") String userId, @PathVariable("projectId") String projectId, @RequestHeader("type") TaskTypeEnum type){
        logger.info("HIT - GET /projects/<projectId>/tasks/<userId>/completion/details ---> getProjectTaskCompletionUserDetails | entityId: {} | userId: {} | TaskType :{}", projectId, userId, type);
        return sendResponse(taskService.getProjectTaskCompletionUserDetails(userId, projectId, type));
    }

    @ApiOperation(value = "Get Project Task completion status of a Project", notes = "(Projects Tab) Get All Task completion status of a Project")
    @ApiResponse(code = 200, message = "Success", response = Response.class)
    @GetMapping("/{projectId}/tasks/completion")
    public ResponseEntity<Object> getProjectTaskCompletion(@RequestHeader("user") String userId, @PathVariable("projectId") String projectId){
        logger.info("HIT - GET /projects/<projectId>/tasks/completion ---> getProjectTaskCompletion | projectId: {} | userId: {} ", projectId, userId);
        return sendResponse(taskService.getProjectTaskCompletion(userId, projectId));
    }

    @ApiOperation(value = "Get all users with all task completion status of all projects", notes = "(WorkLoad I) Get all users with all task completion status of all projects")
    @ApiResponse(code = 200, message = "Success", response = Response.class)
    @GetMapping("/tasks/users/workload")
    public ResponseEntity<Object> getAllUsersWithTaskCompletion(@RequestHeader("user") String userId){
        logger.info("HIT - GET /projects/tasks/users/workload ---> getAllUsersWithTaskCompletion | userId: {} ",  userId);
        return sendResponse(taskService.getAllUsersWithTaskCompletion(userId));
    }

    @ApiOperation(value = "Get all users with all task completion status of all projects", notes = "(WorkLoad I) Get all users with all task completion status of all projects")
    @ApiResponse(code = 200, message = "Success", response = Response.class)
    @GetMapping("/tasks/users/{userId}/workload")
    public ResponseEntity<Object> getAllUserAssignedTaskWithCompletion(@PathVariable("userId") String userId, @RequestHeader("user") String user, @RequestHeader("from") String from, @RequestHeader("to") String to){
        logger.info("HIT - GET /projects/tasks/users/<userId>/workload ---> getAllUserAssignedTaskWithCompletion | from user: {} || about userId: {} || from {} || to {}",  user, userId, from, to);
        return sendResponse(taskService.getAllUserAssignedTaskWithCompletion(user,userId, from, to));
    }

    @ApiOperation(value = "Update Sprint of a  Task", notes = "Update Sprint of a  Task")
    @ApiResponse(code = 200, message = "Success", response = Response.class)
    @PutMapping("/{projectId}/tasks/{taskId}/sprint")
    public ResponseEntity<Object> updateProjectTaskSprint(@RequestHeader("user") String userId, @PathVariable("projectId") String projectId, @PathVariable("taskId") String taskId, @RequestBody TaskSprintUpdateDto taskSprintUpdateDto){
        logger.info("HIT - PUT /projects/<projectId>/tasks/<taskId>/sprint ---> updateProjectTaskSprint | projectId: {} | userId: {} | taskId: {} | TaskSprintUpdateDto: {}", projectId, userId, taskId, taskSprintUpdateDto);
        return sendResponse(taskService.updateProjectTaskSprint(userId, projectId, taskId, taskSprintUpdateDto));
    }

}
