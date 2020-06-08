package com.arimac.backend.pmtool.projectmanagementtool.controller;

import com.arimac.backend.pmtool.projectmanagementtool.Response.ResponseController;
import com.arimac.backend.pmtool.projectmanagementtool.Service.ActivityLogService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/log")
public class ActivityLogController extends ResponseController {

    private static final Logger logger = LoggerFactory.getLogger(ActivityLogController.class);

    private final ActivityLogService activityLogService;

    public ActivityLogController(ActivityLogService activityLogService) {
        this.activityLogService = activityLogService;
    }


//    @ApiOperation(value = "Get all logs", notes = "Get all logs")
//    @ApiResponse(code = 200, message = "Success", response = List.class)
//    @GetMapping("/{projectId}")
//    public ResponseEntity<Object> getAllLogs(@PathVariable("projectId") String projectId){
//        logger.info("HIT - GET /logs ---> getAllLogs {}", projectId);
//        return sendResponse(activityLogService.getAllLogs(projectId));
//    }
}
