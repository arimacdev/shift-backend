package com.arimac.backend.pmtool.projectmanagementtool.controller;

import com.arimac.backend.pmtool.projectmanagementtool.Response.ResponseController;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/support")
public class ServiceDeskController extends ResponseController {
    private static final Logger logger = LoggerFactory.getLogger(ServiceDeskController.class);

    @ApiOperation(value = "Get Overview of Organization", notes = "Get Overview of Organization")
    @ApiResponse(code = 200, message = "Success", response = List.class)
    @GetMapping("/overview")
    public ResponseEntity<Object> getOrgOverview(){
//        logger.info("HIT - GET analytics/overview ---> getOrgOverview | User: {} | from: {} | to: {}", userId, from, to);
        return sendResponse(123);
    }

}
