package com.arimac.backend.pmtool.projectmanagementtool.controller;

import com.arimac.backend.pmtool.projectmanagementtool.Response.Response;
import com.arimac.backend.pmtool.projectmanagementtool.Response.ResponseController;
import com.arimac.backend.pmtool.projectmanagementtool.Service.FilterTemplateService;
import com.arimac.backend.pmtool.projectmanagementtool.dtos.ProjectDto;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/templates")
public class FilterTemplateController extends ResponseController {

    private static final Logger logger = LoggerFactory.getLogger(FilterTemplateController.class);

    private final FilterTemplateService filterTemplateService;

    public FilterTemplateController(FilterTemplateService filterTemplateService) {
        this.filterTemplateService = filterTemplateService;
    }

    @ApiOperation(value = "Create a filter template", notes = "Create a filter template for a user")
    @ApiResponse(code = 200, message = "Success", response = Response.class)
    @PostMapping
    public ResponseEntity<Object> createFilterTemplate(){
        logger.info("HIT - createFilterTemplate - /templates");
        return sendResponse(filterTemplateService.createFilterTemplate());
    }


}
