package com.arimac.backend.pmtool.projectmanagementtool.controller;

import com.arimac.backend.pmtool.projectmanagementtool.Response.Response;
import com.arimac.backend.pmtool.projectmanagementtool.Response.ResponseController;
import com.arimac.backend.pmtool.projectmanagementtool.Service.FileUploadService;
import com.arimac.backend.pmtool.projectmanagementtool.enumz.FileUploadEnum;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
//@RequestMapping("/projects")
public class FileUploadController extends ResponseController {

    private static final Logger logger = LoggerFactory.getLogger(FileUploadController.class);

    private final FileUploadService fileUploadService;

    public FileUploadController(FileUploadService fileUploadService) {
        this.fileUploadService = fileUploadService;
    }

    @ApiOperation(value = "Upload a File to a Task", notes = "Upload a file to a task")
    @ApiResponse(code = 200, message = "Success", response = Response.class)
    @PostMapping("/projects/{projectId}/tasks/{taskId}/upload")
    public ResponseEntity<Object> uploadFileToTask(@RequestHeader("user") String userId, @RequestParam("files") MultipartFile multipartFile, @RequestParam("type") FileUploadEnum fileType, @PathVariable("projectId") String projectId, @PathVariable("taskId") String taskId) throws IOException {
        logger.info("HIT - POST /projects/<projectId>/tasks/<taskId>/upload ---> uploadFileToTask | projectId: {} | userId: {} | taskId: {}", projectId, userId, taskId);
        return sendResponse(fileUploadService.uploadFileToTask(userId, projectId, taskId, fileType, multipartFile));
    }

    @ApiOperation(value = "Upload a Profile Picture", notes = "Upload a profile picture to a user profile")
    @ApiResponse(code = 200, message = "Success", response = Response.class)
    @PostMapping("/user/profile/upload")
    public ResponseEntity<Object> uploadProfilePicture(@RequestHeader("user") String userId, @RequestParam("files") MultipartFile multipartFile, @RequestParam("type") FileUploadEnum fileType) throws IOException {
        logger.info("HIT - POST user/profile/upload ---> uploadProfilePicture |userId: {} ",userId);
        return sendResponse(fileUploadService.uploadProfilePicture(userId,  fileType, multipartFile));
    }

}
