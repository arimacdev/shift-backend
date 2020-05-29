package com.arimac.backend.pmtool.projectmanagementtool.controller;

import com.arimac.backend.pmtool.projectmanagementtool.Response.Response;
import com.arimac.backend.pmtool.projectmanagementtool.Response.ResponseController;
import com.arimac.backend.pmtool.projectmanagementtool.Service.AdminService;
import com.arimac.backend.pmtool.projectmanagementtool.dtos.Role.UserRoleDto;
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
    @GetMapping("/user/roles")
    public ResponseEntity<Object> getAllRealmRoles(@RequestHeader("userId") String userId){
        logger.info("HIT -GET /user/roles ---> getAllRealmRoles | userId: {}", userId);
        return sendResponse(adminService.getAllRealmRoles(userId));
    }

    @ApiOperation(value = "Get Role Mappings of a User", notes = "Get Role Mappings of a User")
    @ApiResponse(code = 200, message = "Success", response = Response.class)
    @GetMapping("/user/{userId}/roles")
    public ResponseEntity<Object> getAllUserRoleMappings(@RequestHeader("userId") String adminId, @PathVariable("userId") String userId){
        logger.info("HIT -GET /user/<userId>/roles ---> getAllUserRoleMappings | userId: {} | adminId: {}", userId, adminId);
        return sendResponse(adminService.getAllUserRoleMappings(userId, adminId));
    }

    @ApiOperation(value = "Add Role To User", notes = "Add Role To User")
    @ApiResponse(code = 200, message = "Success", response = Response.class)
    @PostMapping("/user/roles")
    public ResponseEntity<Object> addRoleToUser(@RequestBody UserRoleDto userRoleDto, @RequestHeader("userId") String userId){
        logger.info("HIT -POST /user/roles ---> addRoleToUser | userId: {} | Dto : {}", userId, userRoleDto);
        return sendResponse(adminService.addRoleToUser(userId, userRoleDto));
    }

    @ApiOperation(value = "Add Role To User", notes = "Add Role To User")
    @ApiResponse(code = 200, message = "Success", response = Response.class)
    @DeleteMapping("/user/roles")
    public ResponseEntity<Object> removerUserRole(@RequestBody UserRoleDto userRoleDto, @RequestHeader("userId") String userId){
        logger.info("HIT -DELETE /user/roles ---> removerUserRole | userId: {} | Dto : {}", userId, userRoleDto);
        return sendResponse(adminService.removerUserRole(userId, userRoleDto));
    }


}
