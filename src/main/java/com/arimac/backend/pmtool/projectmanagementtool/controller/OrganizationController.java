package com.arimac.backend.pmtool.projectmanagementtool.controller;

import com.arimac.backend.pmtool.projectmanagementtool.Response.ResponseController;
import com.arimac.backend.pmtool.projectmanagementtool.Service.OrganizationService;
import com.arimac.backend.pmtool.projectmanagementtool.dtos.Organization.AddOrganization;
import com.arimac.backend.pmtool.projectmanagementtool.dtos.Organization.UpdateOrganization;
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
        logger.info("HIT - POST organization ---> addOrganization | User: {} | addOrganization : {}", userId, addOrganization);
        return sendResponse(organizationService.addOrganization(userId,addOrganization));
    }

    @ApiOperation(value = "Get All Organizations", notes = "Get All Organizations")
    @ApiResponse(code = 200, message = "Success", response = List.class)
    @GetMapping
    public ResponseEntity<Object> getAllOrganizations(@RequestHeader("user") String userId,
                                                  @RequestParam("startIndex") int startIndex,
                                                  @RequestParam("endIndex") int endIndex,
                                                  @RequestParam("allRecords") boolean allRecords){
        logger.info("HIT - GET getAllOrganizations ---> | User: {} | start : {}| end :{} | allRecords :{}", userId, startIndex, endIndex, allRecords);
        return sendResponse(organizationService.getAllOrganizations(userId,startIndex,endIndex,allRecords));
    }

    @ApiOperation(value = "get Organization By Id", notes = "get Organization By Id")
    @ApiResponse(code = 200, message = "Success", response = List.class)
    @GetMapping("/{organizationId}")
    public ResponseEntity<Object> getOrganizationById(@RequestHeader("user") String userId,
                                                      @PathVariable("organizationId") String organizationId ){
        logger.info("HIT - GET getOrganizationById/<orgId> ---> | User: {} | orgId:{}", userId, organizationId);
        return sendResponse(organizationService.getOrganizationById(userId,organizationId));
    }

    @ApiOperation(value = "Get Projects of an Organization", notes = "Get Projects of an Organization")
    @ApiResponse(code = 200, message = "Success", response = List.class)
    @GetMapping("/{organizationId}/projects")
    public ResponseEntity<Object> getProjectsOfOrganization(@RequestHeader("user") String userId,
                                                      @PathVariable("organizationId") String organizationId){
        logger.info("HIT - GET getProjectsOfOrganization ---> | User: {} | projectId :{}", userId, organizationId);
        return sendResponse(organizationService.getProjectsOfOrganization(userId,organizationId));
    }

    @ApiOperation(value = "Update an Organization", notes = "Update an Organization")
    @ApiResponse(code = 200, message = "Success", response = List.class)
    @PutMapping("/{organizationId}")
    public ResponseEntity<Object> updateOrganization(@RequestHeader("user") String userId,
                                                    @PathVariable("organizationId") String organizationId,
                                                    @Valid @RequestBody UpdateOrganization updateOrganization){
        logger.info("HIT - PUT organization/<id> ---> updateOrganization | Org. {} }  User: {} | updateOrganization : {}", organizationId, userId, updateOrganization);
        return sendResponse(organizationService.updateOrganization(userId, organizationId, updateOrganization));
    }

    @ApiOperation(value = "Flag an Organization", notes = "Flag an Organization")
    @ApiResponse(code = 200, message = "Success", response = List.class)
    @DeleteMapping("/{organizationId}")
    public ResponseEntity<Object> flagOrganization(@RequestHeader("user") String userId,
                                                     @PathVariable("organizationId") String organizationId){
        logger.info("HIT - DELETE organization/<id> ---> flagOrganization | Org. {} }  User: {} ", organizationId, userId);
        return sendResponse(organizationService.flagOrganization(userId, organizationId));
    }
}
