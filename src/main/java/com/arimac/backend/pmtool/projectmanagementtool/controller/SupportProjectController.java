package com.arimac.backend.pmtool.projectmanagementtool.controller;


import com.arimac.backend.pmtool.projectmanagementtool.Response.ResponseController;
import com.arimac.backend.pmtool.projectmanagementtool.Service.SupportProjectService;
import com.arimac.backend.pmtool.projectmanagementtool.dtos.SupportProject.AddSupportProject;
import com.arimac.backend.pmtool.projectmanagementtool.dtos.SupportProject.AddSupportUserDto;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/support")
public class SupportProjectController extends ResponseController {
    private static final Logger logger = LoggerFactory.getLogger(SupportProjectController.class);

    private final SupportProjectService supportProjectService;

    public SupportProjectController(SupportProjectService supportProjectService) {
        this.supportProjectService = supportProjectService;
    }

    @ApiOperation(value = "Add Support Admin  for Project", notes = "Add Support Admin  for Project")
    @ApiResponse(code = 200, message = "Success", response = List.class)
    @PostMapping("/user/admin")
    public ResponseEntity<Object> createAdminForSupportProject(@Valid @RequestBody AddSupportUserDto addSupportUserDto,
                                                                   @RequestHeader("user") String user,
                                                                   @RequestHeader("project") String project){
        logger.info("POST - support/user/admin ---> createAdminForSupportProject | addSupportUserDto: {} | User: {} | project: {}", addSupportUserDto, user,project);
        return sendResponse(supportProjectService.createAdminForSupportProject(user,project, addSupportUserDto));
    }

    @ApiOperation(value = "Get Support User By Email", notes = "Get Support User By Email")
    @ApiResponse(code = 200, message = "Success", response = List.class)
    @GetMapping("/user")
    public ResponseEntity<Object> getSupportUserByEmail(@RequestHeader("user") String user,
                                                        @RequestParam("email") String email){
        logger.info("POST - support/user?email---> getSupportUserByEmail | email: {} | User: {} ", email, user);
        return sendResponse(supportProjectService.getSupportUserByEmail(user,email));
    }

}
