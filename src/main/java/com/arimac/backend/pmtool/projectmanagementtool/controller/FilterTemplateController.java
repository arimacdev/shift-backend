package com.arimac.backend.pmtool.projectmanagementtool.controller;

import com.arimac.backend.pmtool.projectmanagementtool.Response.Response;
import com.arimac.backend.pmtool.projectmanagementtool.Response.ResponseController;
import com.arimac.backend.pmtool.projectmanagementtool.Service.FilterTemplateService;
import com.arimac.backend.pmtool.projectmanagementtool.dtos.ProjectDto;
import com.arimac.backend.pmtool.projectmanagementtool.dtos.Template.TemplateDto;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/template")
public class FilterTemplateController extends ResponseController {

    private static final Logger logger = LoggerFactory.getLogger(FilterTemplateController.class);

    private final FilterTemplateService filterTemplateService;

    public FilterTemplateController(FilterTemplateService filterTemplateService) {
        this.filterTemplateService = filterTemplateService;
    }

    @ApiOperation(value = "Create a filter template", notes = "Create a filter template for a user")
    @ApiResponse(code = 200, message = "Success", response = Response.class)
    @PostMapping
    public ResponseEntity<Object> createFilterTemplate(@RequestBody TemplateDto templateDto){
        logger.info("HIT - createFilterTemplate - /template | dto : {}", templateDto);
        return sendResponse(filterTemplateService.createFilterTemplate(templateDto));
    }

    @ApiOperation(value = "Get All filter templates of a User", notes = "Get All filter templates of a User")
    @ApiResponse(code = 200, message = "Success", response = Response.class)
    @GetMapping("/{userId}")
    public ResponseEntity<Object> getAllUserTemplates(@PathVariable("userId") String userId){
        logger.info("HIT - getAllUserTemplates - /template/<userId> | dto : {}", userId);
        return sendResponse(filterTemplateService.getAllUserTemplates(userId));
    }


}
