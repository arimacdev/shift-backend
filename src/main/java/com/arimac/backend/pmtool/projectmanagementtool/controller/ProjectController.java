package com.arimac.backend.pmtool.projectmanagementtool.controller;


import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/project")
public class ProjectController {

    private static final Logger logger = LoggerFactory.getLogger(ProjectController.class);

    @ApiOperation(value = "Project Create", notes = "Create a project for an organization")
    @ApiResponse(code = 200, message = "Success")
    @PostMapping
    public void createProject(){

    }
}
