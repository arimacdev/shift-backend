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
        logger.info("GET - support/user?email---> getSupportUserByEmail | email: {} | User: {} ", email, user);
        return sendResponse(supportProjectService.getSupportUserByEmail(user,email));
    }

    @ApiOperation(value = "Get Support Projects", notes = "Get Support Projects")
    @ApiResponse(code = 200, message = "Success", response = List.class)
    @GetMapping("/projects")
    public ResponseEntity<Object> getSupportProjects(@RequestHeader("user") String user){
        logger.info("GET - support/projects---> getSupportProjects | User: {} ", user);
        return sendResponse(supportProjectService.getSupportProjects(user));
    }

    @ApiOperation(value = "Get Support Users By Organization", notes = "Get Support Users By Organization")
    @ApiResponse(code = 200, message = "Success", response = List.class)
    @GetMapping("/user/organization/{organizationId}")
    public ResponseEntity<Object> getSupportUsersByOrganization(@RequestHeader("user") String user,
                                                             @PathVariable("organizationId") String organizationId){
        logger.info("GET - support/user/organization/<Org.Id>---> getSupportUsersByOrganization | User: {} || Org. Id {} ", user, organizationId);
        return sendResponse(supportProjectService.getSupportUsersByOrganization(user, organizationId));
    }

    @ApiOperation(value = "Get Support Users By Project", notes = "Get Support Users By Project")
    @ApiResponse(code = 200, message = "Success", response = List.class)
    @GetMapping("/user/project/{projectId}")
    public ResponseEntity<Object> getSupportUsersByProject(@RequestHeader("user") String user,
                                                           @PathVariable("projectId") String projectId){
        logger.info("GET - support/user/project/<projectId>---> getSupportUsersByProject | User: {} || project {} ", user, projectId);
        return sendResponse(supportProjectService.getSupportUsersByProject(user, projectId));
    }

    @ApiOperation(value = "Get Support Ticket status of a Project", notes = "Get Support Ticket status of a Project")
    @ApiResponse(code = 200, message = "Success", response = List.class)
    @GetMapping("/ticket/project/{projectId}/status")
    public ResponseEntity<Object> getSupportTicketStatusByProject(@RequestHeader("user") String user,
                                                                  @PathVariable("projectId") String projectId){
        logger.info("GET - support/ticket/project/<projectId>/status---> getSupportTicketStatusByProject | User: {} | project: {}", user, projectId);
        return sendResponse(supportProjectService.getSupportTicketStatusByProject(user, projectId));
    }

    @ApiOperation(value = "Get Support Tickets of a project", notes = "Get Support Tickets of a project")
    @ApiResponse(code = 200, message = "Success", response = List.class)
    @GetMapping("/ticket/project/{projectId}")
    public ResponseEntity<Object> getSupportTicketsByProject(@RequestHeader("user") String user,
                                                             @PathVariable("projectId") String projectId,
                                                             @RequestParam("startIndex") int startIndex,
                                                             @RequestParam("endIndex") int endIndex){
        logger.info("GET - support/ticket/project/<projectId>---> getSupportTicketsByProject | User: {} | project: {} | startIndex : {}, endIndex: {}", user, projectId, startIndex, endIndex);
        return sendResponse(supportProjectService.getSupportTicketsByProject(user, projectId, startIndex, endIndex));
    }

}
