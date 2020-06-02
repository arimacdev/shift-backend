package com.arimac.backend.pmtool.projectmanagementtool.controller;

import com.arimac.backend.pmtool.projectmanagementtool.Response.Response;
import com.arimac.backend.pmtool.projectmanagementtool.Response.ResponseController;
import com.arimac.backend.pmtool.projectmanagementtool.Service.UserService;
import com.arimac.backend.pmtool.projectmanagementtool.dtos.SlackNotificationDto;
import com.arimac.backend.pmtool.projectmanagementtool.dtos.User.UserActiveStatusDto;
import com.arimac.backend.pmtool.projectmanagementtool.dtos.UserRegistrationDto;
import com.arimac.backend.pmtool.projectmanagementtool.dtos.UserUpdateDto;
import com.arimac.backend.pmtool.projectmanagementtool.model.User;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
@Validated
public class UserController extends ResponseController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @ApiOperation(value = "Create User", notes = "Creates a user")
    @ApiResponse(code = 200, message = "Success", response = Response.class)
    @PostMapping
    public ResponseEntity<Object> createUser(@RequestBody UserRegistrationDto userRegistrationDto){
        logger.info("HIT - POST /users ---> createUser | dto: {}", userRegistrationDto);
        return sendResponse(userService.createUser(userRegistrationDto));
    }

    @ApiOperation(value = "Create first User", notes = "Creates the initial user")
    @ApiResponse(code = 200, message = "Success", response = Response.class)
    @PostMapping("/first")
    public ResponseEntity<Object> createFirstUser(@RequestBody UserRegistrationDto userRegistrationDto){
        logger.info("HIT - POST /users ---> createFirstUser | dto: {}", userRegistrationDto);
        return sendResponse(userService.createFirstUser(userRegistrationDto));
    }

    @ApiOperation(value = "Get all users", notes = "Get all users")
    @ApiResponse(code = 200, message = "Success", response = List.class)
    @GetMapping
    public ResponseEntity<Object> getAllUsers(){
        logger.info("HIT - GET /users ---> getAllUsers");
        return sendResponse(userService.getAllUsers());
    }

    @ApiOperation(value = "Get user by userId", notes = "Retrieve a single user")
    @ApiResponse(code = 200, message = "Success", response = User.class)
    @GetMapping("/{userId}")
    public ResponseEntity<Object> getUserByUserId(@PathVariable("userId") String userId){
        logger.info("HIT - GET /users/<userId> ---> getUserByUserId | userId: {}",userId);
        return sendResponse(userService.getUserByUserId(userId));
    }

    @ApiOperation(value = "Get user by userId", notes = "Retrieve a single user")
    @ApiResponse(code = 200, message = "Success", response = User.class)
    @PutMapping("/{userId}")
    public ResponseEntity<Object> updateUserByUserId(@PathVariable("userId") String userId, @RequestBody UserUpdateDto userUpdateDto){
        logger.info("HIT - PUT /users/<userId> ---> updateUserByUserId | userId: {}| dto: {}",userId,userUpdateDto);
        return sendResponse(userService.updateUserByUserId(userId, userUpdateDto));
    }

    @ApiOperation(value = "Get all users of a project", notes = "Get all users of a project")
    @ApiResponse(code = 200, message = "Success", response = User.class)
    @GetMapping("/project/{projectId}")
    public ResponseEntity<Object> getAllProjectUsers(@PathVariable("projectId") String projectId){
        logger.info("HIT - GET /users/<userId> ---> getAllProjectUsers | projectId: {}",projectId);
        return sendResponse(userService.getAllProjectUsers(projectId));
    }

    @ApiOperation(value = "Get all blocked users of a project", notes = "Get all users of a project")
    @ApiResponse(code = 200, message = "Success", response = User.class)
    @GetMapping("/project/{projectId}/blocked")
    public ResponseEntity<Object> getAllBlockedProjectUsers(@PathVariable("projectId") String projectId){
        logger.info("HIT - GET /users/<userId>/blocked ---> getAllBlockedProjectUsers | projectId: {}",projectId);
        return sendResponse(userService.getAllBlockedProjectUsers(projectId));
    }

    @ApiOperation(value = "Add Slack Id to User", notes = "User and SlackId mapping")
    @ApiResponse(code = 200, message = "Success", response = Response.class)
    @PutMapping("/{userId}/slack")
    public ResponseEntity<Object> addSlackIdToUser(@PathVariable("userId") String userId, @RequestBody SlackNotificationDto slackNotificationDto){
        logger.info("HIT - PUT /users/<userId>/slack ---> addSlackIdToUser | userId: {}| dto: {}",userId,slackNotificationDto);
        return sendResponse(userService.addSlackIdToUser(userId, slackNotificationDto));
    }

    @ApiOperation(value = "Update Slack Notification status", notes = "On/Off Slack notifications")
    @ApiResponse(code = 200, message = "Success", response = Response.class)
    @PutMapping("/{userId}/slack/status")
    public ResponseEntity<Object> updateNotificationStatus(@PathVariable("userId") String userId, @RequestBody SlackNotificationDto slackNotificationDto){
        logger.info("HIT - PUT /users/<userId>/slack ---> addSlackIdToUser | userId: {}| dto: {}",userId,slackNotificationDto);
        return sendResponse(userService.updateNotificationStatus(userId, slackNotificationDto));
    }

    @ApiOperation(value = "Activate User", notes = "Deactivate User")
    @ApiResponse(code = 200, message = "Success", response = Response.class)
    @PostMapping("/activate")
    public ResponseEntity<Object> activateUser(@RequestBody UserActiveStatusDto userActiveStatusDto){
        logger.info("HIT - POST /users/activate ---> userActiveStatusDto | dto: {}", userActiveStatusDto);
        return sendResponse(userService.activateUser(userActiveStatusDto));
    }

    @ApiOperation(value = "Deactivate User", notes = "Deactivate User")
    @ApiResponse(code = 200, message = "Success", response = Response.class)
    @PostMapping("/deactivate")
    public ResponseEntity<Object> deactivateUser(@RequestBody UserActiveStatusDto userActiveStatusDto){
        logger.info("HIT - POST /users/deactivate ---> userActiveStatusDto | dto: {}", userActiveStatusDto);
        return sendResponse(userService.deactivateUser(userActiveStatusDto));
    }

}
