package com.arimac.backend.pmtool.projectmanagementtool.controller;

import com.arimac.backend.pmtool.projectmanagementtool.Response.ResponseController;
import com.arimac.backend.pmtool.projectmanagementtool.Service.OrganizationService;
import com.arimac.backend.pmtool.projectmanagementtool.dtos.Organization.AddOrganization;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/organization")
public class OrganizationController extends ResponseController {
    private static final Logger logger = LoggerFactory.getLogger(OrganizationController.class);

    private final OrganizationService organizationService;

    public OrganizationController(OrganizationService organizationService) {
        this.organizationService = organizationService;
    }

    @ApiOperation(value = "Create an Organization", notes = "Create an Organization")
    @ApiResponse(code = 200, message = "Success", response = List.class)
    @PostMapping
    public ResponseEntity<Object> addOrganization(@RequestHeader("user") String userId,
                                             @Valid @RequestBody AddOrganization addOrganization){
        logger.info("HIT - POST client ---> addOrganization | User: {} | addOrganization : {}", userId, addOrganization);
        return sendResponse(organizationService.addOrganization(userId,addOrganization));
    }
}
