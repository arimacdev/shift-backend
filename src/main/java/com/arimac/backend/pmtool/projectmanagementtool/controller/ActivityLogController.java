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
@RequestMapping("/activity/task")
public class ActivityLogController extends ResponseController {

    private static final Logger logger = LoggerFactory.getLogger(ActivityLogController.class);

    private final ActivityLogService activityLogService;

    public ActivityLogController(ActivityLogService activityLogService) {
        this.activityLogService = activityLogService;
    }

    @ApiOperation(value = "Get Logs of a Task", notes = "Get all logs")
    @ApiResponse(code = 200, message = "Success", response = List.class)
    @GetMapping("/{taskId}")
    public ResponseEntity<Object> getAllLogs(@PathVariable("taskId") String taskId, @RequestHeader("userId") String userId, @RequestParam("startIndex") int startIndex, @RequestParam("endIndex") int endIndex){
        logger.info("HIT - GET /logs ---> getAllLogs of a Task  Task: {} | User: {} | Start: {}| End: {}", taskId, userId,startIndex,endIndex);
        return sendResponse(activityLogService.getTaskActivity(userId, taskId,startIndex,endIndex));
    }
}
