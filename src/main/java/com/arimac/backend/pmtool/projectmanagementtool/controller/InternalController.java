package com.arimac.backend.pmtool.projectmanagementtool.controller;

import com.arimac.backend.pmtool.projectmanagementtool.Response.ResponseController;
import com.arimac.backend.pmtool.projectmanagementtool.Service.InternalService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("internal/")
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

}
