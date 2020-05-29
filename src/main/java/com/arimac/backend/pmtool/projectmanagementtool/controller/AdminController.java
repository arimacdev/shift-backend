package com.arimac.backend.pmtool.projectmanagementtool.controller;

import com.arimac.backend.pmtool.projectmanagementtool.Response.Response;
import com.arimac.backend.pmtool.projectmanagementtool.Response.ResponseController;
import com.arimac.backend.pmtool.projectmanagementtool.Service.AdminService;
import com.arimac.backend.pmtool.projectmanagementtool.dtos.Role.AddUserRoleDto;
import com.arimac.backend.pmtool.projectmanagementtool.dtos.UserRegistrationDto;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin")
public class AdminController extends ResponseController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    private final AdminService adminService;

    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    @ApiOperation(value = "Get All Realm Roles", notes = "Get All Realm Roles")
    @ApiResponse(code = 200, message = "Success", response = Response.class)
    @GetMapping("/roles")
    public ResponseEntity<Object> getAllRealmRoles(@RequestHeader String userId){
        logger.info("HIT -GET /admin/roles ---> getAllRealmRoles | userId: {}", userId);
        return sendResponse(adminService.getAllRealmRoles(userId));
    }

    @ApiOperation(value = "Add Role To User", notes = "Add Role To User")
    @ApiResponse(code = 200, message = "Success", response = Response.class)
    @PostMapping("/user/addrole")
    public ResponseEntity<Object> addRoleToUser(@RequestBody AddUserRoleDto addUserRoleDto, @RequestHeader("userId") String userId){
        logger.info("HIT -POST /admin/user/addrole ---> addRoleToUser | userId: {} | Dto : {}", userId, addUserRoleDto);
        return sendResponse(adminService.addRoleToUser(userId, addUserRoleDto));
    }


}
