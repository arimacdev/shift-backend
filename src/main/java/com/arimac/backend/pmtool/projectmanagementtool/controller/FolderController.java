package com.arimac.backend.pmtool.projectmanagementtool.controller;

import com.arimac.backend.pmtool.projectmanagementtool.Response.Response;
import com.arimac.backend.pmtool.projectmanagementtool.Response.ResponseController;
import com.arimac.backend.pmtool.projectmanagementtool.Service.FolderService;
import com.arimac.backend.pmtool.projectmanagementtool.dtos.Folder.FolderAddDto;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class FolderController  extends ResponseController{
    private static final Logger logger = LoggerFactory.getLogger(FolderController.class);

    private final FolderService folderService;

    public FolderController(FolderService folderService) {
        this.folderService = folderService;
    }

    @ApiOperation(value = "Create a Folder to a project", notes = "Create a Folder to a project")
    @ApiResponse(code = 200, message = "Success", response = Response.class)
    @PostMapping("/projects/{projectId}/folder")
    public ResponseEntity<Object> createFolder(@RequestHeader("user") String userId, @PathVariable("projectId") String projectId, @RequestBody FolderAddDto folderAddDto) {
        logger.info("HIT - POST /projects/<projectId>/folder/---> createFolder | folderDto: {} | userId: {}", projectId, folderAddDto);
        return sendResponse(folderService.createFolder(projectId, userId, folderAddDto));
    }

    @ApiOperation(value = "Get Main Folders of a project", notes = "Create a Folder to a project")
    @ApiResponse(code = 200, message = "Success", response = Response.class)
    @GetMapping("/projects/{projectId}/folder")
    public ResponseEntity<Object> getMainFolders(@RequestHeader("user") String userId, @PathVariable("projectId") String projectId) {
        logger.info("HIT - POST /projects/<projectId>/folder/---> getMainFolders | userId: {} | projectId: {}",userId, projectId);
        return sendResponse(folderService.getMainFolders(userId, projectId));
    }

    @ApiOperation(value = "Get Files & Folders of a Folder", notes = "Get Files & Folders of a Folder")
    @ApiResponse(code = 200, message = "Success", response = Response.class)
    @GetMapping("/projects/{projectId}/folder/{folderId}")
    public ResponseEntity<Object> getFilesFoldersOfFolder(@RequestHeader("user") String userId, @PathVariable("projectId") String projectId, @PathVariable("folderId") String folderId) {
        logger.info("HIT - POST /projects/<projectId>/folder/<folderId>---> getFilesFoldersOfFolder | userId: {} | projectId: {} | folderId: {}",userId, projectId, folderId);
        return sendResponse(folderService.getFilesFoldersOfFolder(userId, projectId, folderId));
    }

}
