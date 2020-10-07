package com.arimac.backend.pmtool.projectmanagementtool.controller;

import com.arimac.backend.pmtool.projectmanagementtool.Response.ResponseController;
import com.arimac.backend.pmtool.projectmanagementtool.Service.ServiceDeskService;
import com.arimac.backend.pmtool.projectmanagementtool.dtos.ServiceDesk.AddTicket;
import com.arimac.backend.pmtool.projectmanagementtool.dtos.ServiceDesk.RequestKey;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/support")
public class ServiceDeskController extends ResponseController {
    private static final Logger logger = LoggerFactory.getLogger(ServiceDeskController.class);

    private final ServiceDeskService serviceDeskService;

    public ServiceDeskController(ServiceDeskService serviceDeskService) {
        this.serviceDeskService = serviceDeskService;
    }

    @ApiOperation(value = "Add Ticket to a Project", notes = "Add Ticket to a Project")
    @ApiResponse(code = 200, message = "Success", response = List.class)
    @PostMapping("/ticket")
    public ResponseEntity<Object> createSupportTicket(@Valid @RequestBody AddTicket addTicket){
        logger.info("POST - POST support/ticket ---> createSupportTicket | addTicket: {}", addTicket);
        return sendResponse(serviceDeskService.createSupportTicket(addTicket));
    }

    @ApiOperation(value = "Request Service Desk Key", notes = "Request Service Desk Key")
    @ApiResponse(code = 200, message = "Success", response = List.class)
    @PostMapping("/ticket/key/reset")
    public ResponseEntity<Object> requestNewServiceKey(@Valid @RequestBody RequestKey requestKey){
        logger.info("POST - support/key/reset ---> requestNewServiceKey | requestNewServiceKey: {}", requestKey);
        return sendResponse(serviceDeskService.requestNewServiceKey(requestKey));
    }

}
