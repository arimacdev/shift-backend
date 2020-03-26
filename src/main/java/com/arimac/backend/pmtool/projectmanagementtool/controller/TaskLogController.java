package com.arimac.backend.pmtool.projectmanagementtool.controller;

import com.arimac.backend.pmtool.projectmanagementtool.Response.ResponseController;
import com.arimac.backend.pmtool.projectmanagementtool.Service.TaskLogService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/log")
public class TaskLogController extends ResponseController {

    private static final Logger logger = LoggerFactory.getLogger(TaskLogController.class);

    private final TaskLogService taskLogService;

    public TaskLogController(TaskLogService taskLogService) {
        this.taskLogService = taskLogService;
    }


    @ApiOperation(value = "Get all logs", notes = "Get all logs")
    @ApiResponse(code = 200, message = "Success", response = List.class)
    @GetMapping("/{projectId}")
    public ResponseEntity<Object> getAllLogs(@PathVariable("projectId") String projectId){
        logger.info("HIT - GET /logs ---> getAllLogs {}", projectId);
        return sendResponse(taskLogService.getAllLogs(projectId));
    }
}
