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

}
