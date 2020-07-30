package com.arimac.backend.pmtool.projectmanagementtool.controller;

import com.arimac.backend.pmtool.projectmanagementtool.Response.Response;
import com.arimac.backend.pmtool.projectmanagementtool.Response.ResponseController;
import com.arimac.backend.pmtool.projectmanagementtool.Service.FolderService;
import com.arimac.backend.pmtool.projectmanagementtool.dtos.Folder.FolderDto;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

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
    public ResponseEntity<Object> createFolder(@RequestHeader("user") String userId, @PathVariable("projectId") String projectId, @RequestBody FolderDto folderDto) {
        logger.info("HIT - POST /projects/<projectId>/folder/---> createFolder | folderDto: {} | userId: {}", projectId, folderDto);
        return sendResponse(folderService.createFolder(projectId, userId, folderDto));
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

    @ApiOperation(value = "Update Folder", notes = "Update Folder")
    @ApiResponse(code = 200, message = "Success", response = Response.class)
    @PutMapping("/projects/{projectId}/folder/{folderId}")
    public ResponseEntity<Object> updateFolder(@RequestHeader("user") String userId, @PathVariable("projectId") String projectId, @PathVariable("folderId") String folderId, @Valid @RequestBody FolderDto folderDto) {
        logger.info("HIT - PUT /projects/<projectId>/folder/<folderId>---> updateFolder | userId: {} | projectId: {} | folderId: {} | folderDto: {}",userId, projectId, folderId, folderDto);
        return sendResponse(folderService.updateFolder(userId, projectId, folderId, folderDto));
    }

}
