package com.arimac.backend.pmtool.projectmanagementtool.controller;

import com.arimac.backend.pmtool.projectmanagementtool.Response.ResponseController;
import com.arimac.backend.pmtool.projectmanagementtool.Service.AnalyticsService;
import com.arimac.backend.pmtool.projectmanagementtool.enumz.AnalyticsEnum.ChartCriteriaEnum;
import com.arimac.backend.pmtool.projectmanagementtool.enumz.AnalyticsEnum.ProjectSummaryTypeEnum;
import com.arimac.backend.pmtool.projectmanagementtool.enumz.FilterOrderEnum;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

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

    @ApiOperation(value = "Get Overview of Projects", notes = "Get Overview of Projects")
    @ApiResponse(code = 200, message = "Success", response = List.class)
    @GetMapping("/overview/projects")
    public ResponseEntity<Object> getProjectOverview(@RequestHeader("user") String userId, @RequestHeader("from") String from, @RequestHeader("to") String to){
        logger.info("HIT - GET analytics/overview/projects ---> getOrgOverview | User: {} | from: {} | to: {}", userId, from, to);
        return sendResponse(analyticsService.getProjectOverview(userId,from,to));
    }

    @ApiOperation(value = "Get Project Summary", notes = "Get Project Summary")
    @ApiResponse(code = 200, message = "Success", response = List.class)
    @GetMapping("/summary/projects")
    public ResponseEntity<Object> getProjectSummary(@RequestHeader("user") String userId,
                                                    @RequestParam("from") String from,
                                                    @RequestParam("to") String to,
                                                    @RequestParam("status") Set<String> status,
                                                    @RequestParam("key") String key,
                                                    @RequestParam("orderType") FilterOrderEnum orderType,
                                                    @RequestParam("orderBy")ProjectSummaryTypeEnum orderBy,
                                                    @RequestParam("startIndex")int startIndex,
                                                    @RequestParam("endIndex")int endIndex){
        logger.info("HIT - GET analytics/summary/projects ---> getProjectSummary | User: {} | from: {} | to: {} | status {} | key {} | orderType{} | orderBy{}| startIndex {}| endIndex {}", userId, from, to, status, key, orderType, orderBy,startIndex,endIndex);
        return sendResponse(analyticsService.getProjectSummary(userId,from,to,status,key,orderBy,orderType,startIndex,endIndex));
    }

    @ApiOperation(value = "Task Rate Graph", notes = "Task Rate Graph")
    @ApiResponse(code = 200, message = "Success", response = List.class)
    @GetMapping("/rate/task")
    public ResponseEntity<Object> getTaskRate(@RequestHeader("user") String userId, @RequestParam("from") String from, @RequestParam("to") String to, @RequestParam("criteria")ChartCriteriaEnum criteria){
        logger.info("HIT - GET analytics/rate/task ---> getTaskRate | User: {} | from: {} | to: {} | criteria: {} ", userId, from, to, criteria);
        return sendResponse(analyticsService.getTaskRate(userId, from, to, criteria));
    }




}
