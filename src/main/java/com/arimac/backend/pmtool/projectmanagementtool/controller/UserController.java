package com.arimac.backend.pmtool.projectmanagementtool.controller;

import com.arimac.backend.pmtool.projectmanagementtool.Response.Response;
import com.arimac.backend.pmtool.projectmanagementtool.Response.ResponseController;
import com.arimac.backend.pmtool.projectmanagementtool.Service.UserService;
import com.arimac.backend.pmtool.projectmanagementtool.dtos.UserRegistrationDto;
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




}
