package com.arimac.backend.pmtool.projectmanagementtool.controller;

import com.arimac.backend.pmtool.projectmanagementtool.Response.ResponseController;
import com.arimac.backend.pmtool.projectmanagementtool.Service.InternalService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/internal/")
public class InternalController extends ResponseController {

    private final InternalService internalService;

    public InternalController(InternalService internalService) {
        this.internalService = internalService;
    }

    @GetMapping("updateAlias")
    public ResponseEntity<Object> updateProjectAlias(){
        return sendResponse(internalService.updateProjectAlias());
    }

    @GetMapping("migrate/personal")
    public ResponseEntity<Object> migratePersonalTask(){
        return sendResponse(internalService.migratePersonalTask());
    }

    @GetMapping("add/user/role")
    public ResponseEntity<Object> addUserRole(){
        return sendResponse(internalService.addUserRole());
    }

    @GetMapping("add/user/username")
    public ResponseEntity<Object> addUserNameToUsers(){
        return sendResponse(internalService.addUserNameToUsers());
    }

    @PostMapping("add/user/projects")
    public ResponseEntity<Object> addUserToAllProjects(@RequestHeader("userId") String userId){
        return sendResponse(internalService.addUserToAllProjects(userId));
    }
    @GetMapping("projects")
    public ResponseEntity<Object> getAllOrganizationProjects(@RequestHeader("userId") String userId){
        return sendResponse(internalService.addUserToAllProjects(userId));
    }
    @GetMapping("project/{projectId}")
    public ResponseEntity<Object> getProjectById(@PathVariable("projectId") String projectId){
        return sendResponse(internalService.getProjectById(projectId));
    }
    @PostMapping("user")
    public ResponseEntity<Object> getUsersByIds(@RequestBody Set<String> users){
        return sendResponse(internalService.getUsersByIds(users));
    }
    @GetMapping("organization/{organizationId}")
    public ResponseEntity<Object> getOrganizationById(@PathVariable("organizationId")String organizationId){
        return sendResponse(internalService.getOrganizationById(organizationId));
    }
    @PostMapping("projects")
    public ResponseEntity<Object> getProjectMapByIds(@RequestBody List<String> projectIds){
        return sendResponse(internalService.getProjectMapByIds(projectIds));
    }

}
