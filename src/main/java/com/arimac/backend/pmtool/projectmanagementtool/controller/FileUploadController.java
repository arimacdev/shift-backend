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
    public ResponseEntity<Object> uploadFileToTask(@RequestHeader("user") String userId, @RequestParam("files") MultipartFile multipartFile, @RequestParam("type") FileUploadEnum fileType, @PathVariable("projectId") String projectId, @PathVariable("taskId") String taskId) {
        logger.info("HIT - POST /projects/<projectId>/tasks/<taskId>/upload ---> uploadFileToTask | projectId: {} | userId: {} | taskId: {}", projectId, userId, taskId);
        return sendResponse(fileUploadService.uploadFileToTask(userId, projectId, taskId, fileType, multipartFile));
    }

    @ApiOperation(value = "Upload a File to a TaskGroup Task", notes = "Upload a file to a TaskGroup Task")
    @ApiResponse(code = 200, message = "Success", response = Response.class)
    @PostMapping("/taskgroup/{taskgroupId}/tasks/{taskId}/upload")
    public ResponseEntity<Object> uploadFileToTaskGroupTask(@RequestHeader("user") String userId, @RequestParam("files") MultipartFile multipartFile, @RequestParam("type") FileUploadEnum fileType, @PathVariable("taskgroupId") String taskgroupId, @PathVariable("taskId") String taskId){
        logger.info("HIT - POST /taskgroup/<projectId>/tasks/<taskId>/upload ---> uploadFileToTaskGroupTask | projectId: {} | userId: {} | taskId: {}", taskgroupId, userId, taskId);
        return sendResponse(fileUploadService.uploadFileToTaskGroupTask(userId, taskgroupId, taskId, fileType, multipartFile));
    }

    @ApiOperation(value = "Delete a File from a Task", notes = "Delete a file to a task")
    @ApiResponse(code = 200, message = "Success", response = Response.class)
    @DeleteMapping("/projects/{projectId}/tasks/{taskId}/upload/{taskFileId}")
    public ResponseEntity<Object> deleteFileFromTask(@RequestHeader("user") String userId, @PathVariable("projectId") String projectId, @PathVariable("taskId") String taskId, @PathVariable("taskFileId") String taskFileId)  {
        logger.info("HIT - DELETE /projects/<projectId>/tasks/<taskId>/upload/<taskFileId> ---> flagTaskFile | projectId: {} | userId: {} | taskId: {} | taskFileId: {}", projectId, userId, taskId, taskFileId);
        return sendResponse(fileUploadService.deleteFileFromTask(userId, projectId, taskId, taskFileId));
    }

    @ApiOperation(value = "Delete a File from a TaskGroup Task", notes = "Delete a file from TaskGroup task")
    @ApiResponse(code = 200, message = "Success", response = Response.class)
    @DeleteMapping("/taskgroup/{taskgroupId}/tasks/{taskId}/upload/{taskFileId}")
    public ResponseEntity<Object> deleteFileFromTaskGroupTask(@RequestHeader("user") String userId, @PathVariable("taskgroupId") String taskgroupId, @PathVariable("taskId") String taskId, @PathVariable("taskFileId") String taskFileId)  {
        logger.info("HIT - DELETE /taskgroup/<taskgroupId>/tasks/<taskId>/upload/<taskFileId> ---> flagTaskFile | projectId: {} | userId: {} | taskId: {} | type: {}", taskgroupId, userId, taskId, taskFileId);
        return sendResponse(fileUploadService.deleteFileFromTaskGroupTask(userId, taskgroupId, taskId, taskFileId));
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
    public ResponseEntity<Object> uploadProfilePicture(@RequestHeader("user") String userId, @RequestParam("files") MultipartFile multipartFile, @RequestParam("type") FileUploadEnum fileType){
        logger.info("HIT - POST user/profile/upload ---> uploadProfilePicture |userId: {} ",userId);
        return sendResponse(fileUploadService.uploadProfilePicture(userId,  fileType, multipartFile));
    }

    @ApiOperation(value = "Upload Project Files", notes = "Upload a project related files")
    @ApiResponse(code = 200, message = "Success", response = Response.class)
    @PostMapping("/projects/{projectId}/files/upload")
    public ResponseEntity<Object> uploadProjectFiles(@RequestHeader("user") String userId, @PathVariable("projectId") String projectId,  @RequestParam("files") MultipartFile[] multipartFiles, @RequestParam("type") FileUploadEnum fileType)  {
        logger.info("HIT - POST projects/<projectId>/files/upload ---> uploadProjectFiles |userId: {} | projectId :{}",userId, projectId);
        return sendResponse(fileUploadService.uploadProjectFiles(userId, projectId, fileType, multipartFiles));
    }

    @ApiOperation(value = "Upload Comment Files", notes = "Upload a comment related files")
    @ApiResponse(code = 200, message = "Success", response = Response.class)
    @PostMapping("/task/comment/{commentId}/file")
    public ResponseEntity<Object> uploadCommentFile(@RequestHeader("user") String userId, @PathVariable("commentId") String commentId,  @RequestParam("files") MultipartFile multipartFiles, @RequestParam("type") FileUploadEnum fileType)  {
        logger.info("HIT - POST projects/<projectId>/files/upload ---> uploadProjectFiles |userId: {} | projectId :{}",userId, commentId);
        return sendResponse(fileUploadService.uploadCommentFile(userId, commentId, fileType, multipartFiles));
    }

    @ApiOperation(value = "Get all Project Files", notes = "Upload a project related files")
    @ApiResponse(code = 200, message = "Success", response = Response.class)
    @GetMapping("/projects/{projectId}/files")
    public ResponseEntity<Object> getAllProjectFiles(@RequestHeader("user") String userId, @PathVariable("projectId") String projectId){
        logger.info("HIT - GET projects/<projectId>/files ---> getAllProjectFiles |userId: {} | projectId :{}",userId, projectId);
        return sendResponse(fileUploadService.getAllProjectFiles(userId, projectId));
    }

    @ApiOperation(value = "Flag a project file", notes = "Upload a project related files")
    @ApiResponse(code = 200, message = "Success", response = Response.class)
    @DeleteMapping("/projects/{projectId}/files/{projectFileId}")
    public ResponseEntity<Object> flagProjectFile(@RequestHeader("user") String userId, @PathVariable("projectId") String projectId, @PathVariable("projectFileId") String projectFileId) {
        logger.info("HIT - DELETE projects/<projectId>/files/<projectFileId> ---> getAllProjectFiles |userId: {} | projectId :{} | projectFileId: {}",userId, projectId, projectFileId);
        return sendResponse(fileUploadService.flagProjectFile(userId, projectId, projectFileId));
    }

}
