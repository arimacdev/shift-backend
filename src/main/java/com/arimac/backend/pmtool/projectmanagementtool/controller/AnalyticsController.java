package com.arimac.backend.pmtool.projectmanagementtool.controller;

import com.arimac.backend.pmtool.projectmanagementtool.Response.ResponseController;
import com.arimac.backend.pmtool.projectmanagementtool.Service.AnalyticsService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/analytics")
public class AnalyticsController extends ResponseController {
    private static final Logger logger = LoggerFactory.getLogger(AnalyticsController.class);

    private final AnalyticsService analyticsService;

    public AnalyticsController(AnalyticsService analyticsService) {
        this.analyticsService = analyticsService;
    }

    @ApiOperation(value = "Get Overview of Organization", notes = "Get Overview of Organization")
    @ApiResponse(code = 200, message = "Success", response = List.class)
    @GetMapping("/overview")
    public ResponseEntity<Object> getOrgOverview(@RequestHeader("user") String userId, @RequestHeader("from") String from, @RequestHeader("to") String to){
        logger.info("HIT - GET analytics/overview ---> getOrgOverview | User: {} | from: {} | to: {}", userId, from, to);
        return sendResponse(analyticsService.getOrgOverview(userId,from,to));
    }


}
