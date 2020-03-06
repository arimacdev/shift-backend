package com.arimac.backend.pmtool.projectmanagementtool.controller;

import com.arimac.backend.pmtool.projectmanagementtool.Response.Response;
import com.arimac.backend.pmtool.projectmanagementtool.Response.ResponseController;
import com.arimac.backend.pmtool.projectmanagementtool.Service.UserService;
import com.arimac.backend.pmtool.projectmanagementtool.dtos.UserRegistrationDto;
import com.arimac.backend.pmtool.projectmanagementtool.dtos.UserUpdateDto;
import com.arimac.backend.pmtool.projectmanagementtool.model.User;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
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

}
