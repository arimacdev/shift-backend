package com.arimac.backend.pmtool.projectmanagementtool.Service;

import com.arimac.backend.pmtool.projectmanagementtool.dtos.UserRegistrationDto;
import com.arimac.backend.pmtool.projectmanagementtool.dtos.UserUpdateDto;
import com.arimac.backend.pmtool.projectmanagementtool.model.User;

import java.util.List;

public interface UserService {
    Object createUser(UserRegistrationDto userRegistrationDto);
    Object getAllUsers();
    Object getUserByUserId(String userId);
    Object updateUserByUserId(String userId, UserUpdateDto userUpdateDto);
    Object getAllProjectUsers(String projectId);
}
