package com.arimac.backend.pmtool.projectmanagementtool.controller;

import com.arimac.backend.pmtool.projectmanagementtool.Response.ResponseController;
import com.arimac.backend.pmtool.projectmanagementtool.Service.InternalService;
import com.arimac.backend.pmtool.projectmanagementtool.dtos.ProjectDto;
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
}
