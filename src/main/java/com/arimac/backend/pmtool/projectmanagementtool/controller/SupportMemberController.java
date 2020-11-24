package com.arimac.backend.pmtool.projectmanagementtool.controller;

import com.arimac.backend.pmtool.projectmanagementtool.Response.Response;
import com.arimac.backend.pmtool.projectmanagementtool.Response.ResponseController;
import com.arimac.backend.pmtool.projectmanagementtool.Service.SupportMemberService;
import com.arimac.backend.pmtool.projectmanagementtool.dtos.SupportMember.AddSupportMember;
import com.arimac.backend.pmtool.projectmanagementtool.dtos.UserRegistrationDto;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/member")
public class SupportMemberController extends ResponseController {
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    private final SupportMemberService supportMemberService;

    public SupportMemberController(SupportMemberService supportMemberService) {
        this.supportMemberService = supportMemberService;
    }

    @ApiOperation(value = "Add Support Member to Project", notes = "Add Support Member to Project")
    @ApiResponse(code = 200, message = "Success", response = Response.class)
    @PostMapping
    public ResponseEntity<Object> addSupportMember(@Valid @RequestBody AddSupportMember addSupportMember,
                                                   @RequestHeader("user") String user){
        logger.info("HIT - POST /member ---> addSupportMember | dto: {} | user: {}", addSupportMember, user);
        return sendResponse(supportMemberService.addSupportMember(user,addSupportMember));
    }

    @ApiOperation(value = "Get Support Members By Project", notes = "Get Support Members By Project")
    @ApiResponse(code = 200, message = "Success", response = Response.class)
    @GetMapping("/project/{projectId}")
    public ResponseEntity<Object> getSupportMemberByProject(@PathVariable("projectId") String projectId,
                                                   @RequestHeader("user") String user){
        logger.info("HIT - GET /member/project/<projectId> ---> getSupportMemberByProject | project: {} | user: {}", projectId, user);
        return sendResponse(supportMemberService.getSupportMemberByProject(user,projectId));
    }

    @ApiOperation(value = "Remove Member from Project", notes = "Remove Member from Project")
    @ApiResponse(code = 200, message = "Success", response = Response.class)
    @DeleteMapping
    public ResponseEntity<Object> removeSupportMemberFromProject(@RequestHeader("user") String user,
                                                                 @RequestParam("memberId") String memberId,
                                                                 @RequestParam("projectId") String projectId){
        logger.info("HIT - DELETE /member ---> removeSupportMemberFromProject | memberId: {} | projectId: {} | user: {}", memberId, projectId, user);
        return sendResponse(supportMemberService.removeSupportMemberFromProject(user, memberId, projectId));
    }
}
