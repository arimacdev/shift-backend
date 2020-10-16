package com.arimac.backend.pmtool.projectmanagementtool.controller;


import com.arimac.backend.pmtool.projectmanagementtool.Response.ResponseController;
import com.arimac.backend.pmtool.projectmanagementtool.Service.SupportProjectService;
import com.arimac.backend.pmtool.projectmanagementtool.dtos.SupportProject.AddSupportProject;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/project")
public class SupportProjectController extends ResponseController {
    private static final Logger logger = LoggerFactory.getLogger(SupportProjectController.class);

    private final SupportProjectService supportProjectService;

    public SupportProjectController(SupportProjectService supportProjectService) {
        this.supportProjectService = supportProjectService;
    }

    @ApiOperation(value = "Add Support Project", notes = "Add Support Project")
    @ApiResponse(code = 200, message = "Success", response = List.class)
    @PostMapping
    public ResponseEntity<Object> createSupportProject(@Valid @RequestBody AddSupportProject addSupportProject){
        logger.info("POST - support/project ---> createSupportProject | addSupportProject: {}", addSupportProject);
        //return sendResponse(supportProjectService.createSupportProject(addSupportProject));
        return null;
    }

}
