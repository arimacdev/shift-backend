package com.arimac.backend.pmtool.projectmanagementtool.controller;

import com.arimac.backend.pmtool.projectmanagementtool.Response.ResponseController;
import com.arimac.backend.pmtool.projectmanagementtool.Service.MeetingService;
import com.arimac.backend.pmtool.projectmanagementtool.dtos.Meeting.AddMeeting;
import com.arimac.backend.pmtool.projectmanagementtool.dtos.Meeting.AddMinute;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/meeting")
public class MeetingController extends ResponseController {
    private static final Logger logger = LoggerFactory.getLogger(AnalyticsController.class);

    private final MeetingService meetingService;

    public MeetingController(MeetingService meetingService) {
        this.meetingService = meetingService;
    }

    @ApiOperation(value = "Create a meeting", notes = "Create a meeting")
    @ApiResponse(code = 200, message = "Success", response = List.class)
    @PostMapping
    public ResponseEntity<Object> addMeeting(@RequestHeader("user") String userId,
                                             @Valid @RequestBody AddMeeting addMeeting){
        logger.info("HIT - POST meeting ---> addMeeting | User: {} | addMeeting : {}", userId, addMeeting);
        return sendResponse(meetingService.addMeeting(userId,addMeeting));
    }

    @ApiOperation(value = "Add discusstion points", notes = "Add discusstion points")
    @ApiResponse(code = 200, message = "Success", response = List.class)
    @PostMapping("/discussion")
    public ResponseEntity<Object> addDiscussionPoint(@RequestHeader("user") String userId,
                                             @Valid @RequestBody AddMinute addMinute){
        logger.info("HIT - POST meeting ---> addDiscussionPoint | User: {} | addMinute : {}", userId, addMinute);
        return sendResponse(meetingService.addDiscussionPoint(userId,addMinute));
    }
}
