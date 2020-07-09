package com.arimac.backend.pmtool.projectmanagementtool.controller;

import com.arimac.backend.pmtool.projectmanagementtool.Response.Response;
import com.arimac.backend.pmtool.projectmanagementtool.Response.ResponseController;
import com.arimac.backend.pmtool.projectmanagementtool.Service.NotificationService;
import com.arimac.backend.pmtool.projectmanagementtool.dtos.Comments.MentionDto;
import com.arimac.backend.pmtool.projectmanagementtool.dtos.Notification.NotificationDto;
import com.arimac.backend.pmtool.projectmanagementtool.dtos.SlackNotificationDto;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/notification")
public class NotificationController extends ResponseController {
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    private final NotificationService notificationService;

    public NotificationController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @Deprecated
    @ApiOperation(value = "Add Slack Id to User", notes = "User and SlackId mapping")
    @ApiResponse(code = 200, message = "Success", response = Response.class)
    @PutMapping("/user/{userId}/slack")
    public ResponseEntity<Object> addSlackIdToUser(@PathVariable("userId") String userId, @RequestBody SlackNotificationDto slackNotificationDto){
        logger.info("HIT - PUT /users/<userId>/slack ---> addSlackIdToUser | userId: {}| dto: {}",userId,slackNotificationDto);
        return sendResponse(notificationService.addSlackIdToUser(userId, slackNotificationDto));
    }
    @ApiOperation(value = "Register for Notifications", notes = "Register for Notifications")
    @ApiResponse(code = 200, message = "Success", response = Response.class)
    @PostMapping("/register")
    public ResponseEntity<Object> registerForNotifications(@RequestBody NotificationDto notificationDto, @RequestHeader("userId") String userId){
        logger.info("HIT - POST /notification/register ---> registerForNotifications | userId: {}| dto: {}",userId, notificationDto);
        return sendResponse(notificationService.registerForNotifications(userId, notificationDto));
    }
    @ApiOperation(value = "Unsubscribe from Notifications", notes = "Unsubscribe from Notifications")
    @ApiResponse(code = 200, message = "Success", response = Response.class)
    @PutMapping("/status")
    public ResponseEntity<Object> changeSubscriptionStatus(@RequestBody NotificationDto notificationDto, @RequestHeader("userId") String userId){
        logger.info("HIT - POST /notification/register ---> unsubscribeFromNotifications | userId: {}| dto: {}",userId, notificationDto);
        return sendResponse(notificationService.changeSubscriptionStatus(userId, notificationDto));
    }
    @ApiOperation(value = "Send Notification on Mentioning", notes = "Send Notification on Mentioning")
    @ApiResponse(code = 200, message = "Success", response = Response.class)
    @PostMapping("/mention")
    public ResponseEntity<Object> sendMentionNotification(@RequestBody @Valid MentionDto notificationDto, @RequestHeader("userId") String userId){
        logger.info("HIT - POST /notification/register ---> sendMentionNotification | userId: {}| dto: {}",userId, notificationDto);
        return sendResponse(notificationService.sendMentionNotification(userId, notificationDto));
    }



}
