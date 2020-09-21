package com.arimac.backend.pmtool.projectmanagementtool.controller;

import com.arimac.backend.pmtool.projectmanagementtool.Response.ResponseController;
import com.arimac.backend.pmtool.projectmanagementtool.Service.MeetingService;
import com.arimac.backend.pmtool.projectmanagementtool.dtos.Meeting.AddMeeting;
import com.arimac.backend.pmtool.projectmanagementtool.dtos.Meeting.AddMinute;
import com.arimac.backend.pmtool.projectmanagementtool.dtos.Meeting.UpdateMeeting;
import com.arimac.backend.pmtool.projectmanagementtool.dtos.Meeting.UpdateMinute;
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

    @ApiOperation(value = "Update a meeting", notes = "Update a meeting")
    @ApiResponse(code = 200, message = "Success", response = List.class)
    @PutMapping("/{meetingId}")
    public ResponseEntity<Object> updateMeeting(@RequestHeader("user") String userId,
                                                @PathVariable("meetingId") String meetingId,
                                                @Valid @RequestBody UpdateMeeting updateMeeting){
        logger.info("HIT -/<meetingId> PUT meeting ---> updateMeeting | User: {} | updateMeeting : {}", userId, updateMeeting);
        return sendResponse(meetingService.updateMeeting(userId,meetingId,updateMeeting));
    }

    @ApiOperation(value = "Delete a meeting", notes = "Delete a meeting")
    @ApiResponse(code = 200, message = "Success", response = List.class)
    @DeleteMapping("/{meetingId}")
    public ResponseEntity<Object> deleteMeeting(@RequestHeader("user") String userId,
                                                @PathVariable("meetingId") String meetingId,
                                                @RequestParam("projectId") String projectId
                                               ){
        logger.info("HIT -/<meetingId> DELETE meeting ---> deleteMeeting | User: {} | meeting : {} | projectId: {}", userId, meetingId, projectId);
        return sendResponse(meetingService.deleteMeeting(userId,meetingId,projectId));
    }

    @ApiOperation(value = "Add discusstion points", notes = "Add discusstion points")
    @ApiResponse(code = 200, message = "Success", response = List.class)
    @PostMapping("/discussion")
    public ResponseEntity<Object> addDiscussionPoint(@RequestHeader("user") String userId,
                                             @Valid @RequestBody AddMinute addMinute){
        logger.info("HIT - POST meeting/discussion ---> addDiscussionPoint | User: {} | addMinute : {}", userId, addMinute);
        return sendResponse(meetingService.addDiscussionPoint(userId,addMinute));
    }

    @ApiOperation(value = "Update Discussion Point of a Meeting", notes = "Update Discussion Point of a Meeting")
    @ApiResponse(code = 200, message = "Success", response = List.class)
    @PutMapping("/{meetingId}/discussion/{discussionId}")
    public ResponseEntity<Object> updateDiscussionPoint(@RequestHeader("user") String userId,
                                                        @PathVariable("meetingId") String meetingId,
                                                        @PathVariable("discussionId") String discussionId,
                                                        @Valid @RequestBody UpdateMinute updateMinute){
        logger.info("HIT - PUT meeting/<meetingId>/discussion/<discussionId> ---> updateDiscussionPoint | User: {} | meetingId: {} | discussionId: {} | updateMinute : {}", userId, meetingId, discussionId, updateMinute);
        return sendResponse(meetingService.updateDiscussionPoint(userId,meetingId,discussionId,updateMinute));
    }

    @ApiOperation(value = "Delete Discussion Point of a Meeting", notes = "Delete Discussion Point of a Meeting")
    @ApiResponse(code = 200, message = "Success", response = List.class)
    @DeleteMapping("/{meetingId}/discussion/{discussionId}")
    public ResponseEntity<Object> deleteDiscussionPoint(@RequestHeader("user") String userId,
                                                        @PathVariable("meetingId") String meetingId,
                                                        @PathVariable("discussionId") String discussionId,
                                                        @RequestParam("projectId") String projectId){
        logger.info("HIT - PUT meeting/<meetingId>/discussion/<discussionId> ---> deleteDiscussionPoint | User: {} | meetingId: {} | discussionId: {} | projectId : {}", userId, meetingId, discussionId, projectId);
        return sendResponse(meetingService.deleteDiscussionPoint(userId,meetingId,discussionId,projectId));
    }


    @ApiOperation(value = "Get discussion points", notes = "Get Discussion Points of a meeting")
    @ApiResponse(code = 200, message = "Success", response = List.class)
    @GetMapping("/{meetingId}")
    public ResponseEntity<Object> getDiscussionPointOfMeeting(@RequestHeader("user") String userId,
                                                              @PathVariable("meetingId") String meetingId,
                                                              @RequestParam("projectId") String projectId){
        logger.info("HIT - GET meeting/{{meetingId}} ---> getDiscussionPointOfMeeting | User: {} | meetingId : {} | projectId{}", userId, meetingId, projectId);
        return sendResponse(meetingService.getDiscussionPointOfMeeting(userId,meetingId,projectId));
    }



}
