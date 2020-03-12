package com.arimac.backend.pmtool.projectmanagementtool.controller;

import com.arimac.backend.pmtool.projectmanagementtool.Response.Response;
import com.arimac.backend.pmtool.projectmanagementtool.Response.ResponseController;
import com.arimac.backend.pmtool.projectmanagementtool.Service.FileUploadService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/projects")
public class FileUploadController extends ResponseController {

    private static final Logger logger = LoggerFactory.getLogger(FileUploadController.class);

    private final FileUploadService fileUploadService;

    public FileUploadController(FileUploadService fileUploadService) {
        this.fileUploadService = fileUploadService;
    }

    @ApiOperation(value = "Upload a File to a Task", notes = "Upload a file to a task")
    @ApiResponse(code = 200, message = "Success", response = Response.class)
    @PostMapping("/{projectId}/tasks/{taskId}/upload")
    public ResponseEntity<Object> uploadFileToTask(@RequestHeader("user") String userId, @RequestParam("file") MultipartFile multipartFile, @PathVariable("projectId") String projectId, @PathVariable("taskId") String taskId){
        logger.info("HIT - GET /projects/<projectId>/tasks/<taskId>/upload ---> uploadFileToTask | projectId: {} | userId: {} | taskId: {} | file {}", projectId, userId, taskId, multipartFile.getOriginalFilename());
        return sendResponse(fileUploadService.uploadFileToTask(userId, projectId, taskId, multipartFile));
    }

}