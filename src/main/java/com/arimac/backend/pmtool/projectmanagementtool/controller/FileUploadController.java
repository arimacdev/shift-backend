package com.arimac.backend.pmtool.projectmanagementtool.controller;

import com.arimac.backend.pmtool.projectmanagementtool.Response.Response;
import com.arimac.backend.pmtool.projectmanagementtool.Response.ResponseController;
import com.arimac.backend.pmtool.projectmanagementtool.Service.FileUploadService;
import com.arimac.backend.pmtool.projectmanagementtool.enumz.FileUploadEnum;
import com.arimac.backend.pmtool.projectmanagementtool.enumz.TaskTypeEnum;
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
    public ResponseEntity<Object> uploadFileToTask(@RequestHeader("user") String userId, @RequestParam("files") MultipartFile multipartFile, @RequestParam("type") FileUploadEnum fileType, @RequestParam("taskType") TaskTypeEnum taskType, @PathVariable("projectId") String projectId, @PathVariable("taskId") String taskId) throws IOException {
        logger.info("HIT - POST /projects/<projectId>/tasks/<taskId>/upload ---> uploadFileToTask | projectId: {} | userId: {} | taskId: {} | taskType: {}", projectId, userId, taskId, taskType);
        return sendResponse(fileUploadService.uploadFileToTask(userId, projectId, taskId, taskType, fileType, multipartFile));
    }

    @ApiOperation(value = "Delete a File from a Task", notes = "Upload a file to a task")
    @ApiResponse(code = 200, message = "Success", response = Response.class)
    @DeleteMapping("/projects/{projectId}/tasks/{taskId}/upload/{taskFileId}")
    public ResponseEntity<Object> deleteFileFromTask(@RequestHeader("user") String userId,  @PathVariable("projectId") String projectId, @PathVariable("taskId") String taskId, @PathVariable("taskFileId") String taskFileId)  {
        logger.info("HIT - DELETE /projects/<projectId>/tasks/<taskId>/upload/<taskFileId> ---> flagTaskFile | projectId: {} | userId: {} | taskId: {} | taskFileId: {}", projectId, userId, taskId, taskFileId);
        return sendResponse(fileUploadService.deleteFileFromTask(userId, projectId, taskId, taskFileId));
    }


    @ApiOperation(value = "Upload a File to a Personal Task", notes = "Upload a file to a task")
    @ApiResponse(code = 200, message = "Success", response = Response.class)
    @PostMapping("/personal/tasks/{taskId}/upload")
    public ResponseEntity<Object> uploadFileToPersonalTask(@RequestHeader("user") String userId, @RequestParam("files") MultipartFile multipartFile, @RequestParam("type") FileUploadEnum fileType, @PathVariable("taskId") String taskId) throws IOException {
        logger.info("HIT - POST /projects/<projectId>/tasks/<taskId>/upload ---> uploadFileToTask || userId: {} | taskId: {}", userId, taskId);
        return sendResponse(fileUploadService.uploadFileToPersonalTask(userId, taskId, fileType, multipartFile));
    }

    @ApiOperation(value = "Upload a Profile Picture", notes = "Upload a profile picture to a user profile")
    @ApiResponse(code = 200, message = "Success", response = Response.class)
    @PostMapping("/user/profile/upload")
    public ResponseEntity<Object> uploadProfilePicture(@RequestHeader("user") String userId, @RequestParam("files") MultipartFile multipartFile, @RequestParam("type") FileUploadEnum fileType) throws IOException {
        logger.info("HIT - POST user/profile/upload ---> uploadProfilePicture |userId: {} ",userId);
        return sendResponse(fileUploadService.uploadProfilePicture(userId,  fileType, multipartFile));
    }

}
