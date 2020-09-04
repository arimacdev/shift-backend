package com.arimac.backend.pmtool.projectmanagementtool.controller;

import com.arimac.backend.pmtool.projectmanagementtool.Response.ResponseController;
import com.arimac.backend.pmtool.projectmanagementtool.Service.ActivityLogService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/activity")
public class ActivityLogController extends ResponseController {

    private static final Logger logger = LoggerFactory.getLogger(ActivityLogController.class);

    private final ActivityLogService activityLogService;

    public ActivityLogController(ActivityLogService activityLogService) {
        this.activityLogService = activityLogService;
    }

    @ApiOperation(value = "Get Logs of a Task", notes = "Get all task logs")
    @ApiResponse(code = 200, message = "Success", response = List.class)
    @GetMapping("/task/{taskId}")
    public ResponseEntity<Object> getAllTaskLogs(@PathVariable("taskId") String taskId, @RequestHeader("userId") String userId, @RequestParam("startIndex") int startIndex,
                                                 @RequestParam("endIndex") int endIndex,
                                                 //TO be removed
                                                 @RequestParam("allLogs") boolean allLogs
    )

    {
        logger.info("HIT - GET activity/task/<taskId> ---> getAllLogs of a Task  Task: {} | User: {} | Start: {}| End: {}: allLogs: {}", taskId, userId,startIndex,endIndex,allLogs);
        return sendResponse(activityLogService.getTaskActivity(userId, taskId,startIndex,endIndex,allLogs));
    }

    @ApiOperation(value = "Get Logs of a Project With Task Logs", notes = "Get all project logs")
    @ApiResponse(code = 200, message = "Success", response = List.class)
    @GetMapping("/project/{projectId}")
    public ResponseEntity<Object> getAllProjectLogsWithTasks(@PathVariable("projectId") String projectId, @RequestHeader("userId") String userId, @RequestParam("startIndex") int startIndex, @RequestParam("endIndex") int endIndex){
        logger.info("HIT - GET activity/project/<projectId> ---> getAllLogs of a Project |  Project: {} | User: {} | Start: {}| End: {}", projectId, userId,startIndex,endIndex);
        return sendResponse(activityLogService.getAllProjectLogsWithTasks(userId, projectId,startIndex,endIndex));
    }
}
