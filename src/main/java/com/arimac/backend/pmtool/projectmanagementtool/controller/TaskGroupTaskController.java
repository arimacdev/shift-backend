package com.arimac.backend.pmtool.projectmanagementtool.controller;

import com.arimac.backend.pmtool.projectmanagementtool.Response.Response;
import com.arimac.backend.pmtool.projectmanagementtool.Response.ResponseController;
import com.arimac.backend.pmtool.projectmanagementtool.Service.TaskGroupTaskService;
import com.arimac.backend.pmtool.projectmanagementtool.dtos.TaskGroupTask.TaskGroupTaskDto;
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
}
