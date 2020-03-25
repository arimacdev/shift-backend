package com.arimac.backend.pmtool.projectmanagementtool.Service;

import com.arimac.backend.pmtool.projectmanagementtool.dtos.SlackNotificationDto;
import com.arimac.backend.pmtool.projectmanagementtool.dtos.UserRegistrationDto;
import com.arimac.backend.pmtool.projectmanagementtool.dtos.UserUpdateDto;
import com.arimac.backend.pmtool.projectmanagementtool.model.User;

import java.util.List;

public interface UserService {
    Object createUser(UserRegistrationDto userRegistrationDto);
    Object createFirstUser(UserRegistrationDto userRegistrationDto);
    Object getAllUsers();
    Object getUserByUserId(String userId);
    Object updateUserByUserId(String userId, UserUpdateDto userUpdateDto);
    Object getAllProjectUsers(String projectId);
    Object getAllBlockedProjectUsers(String projectId);
    Object addSlackIdToUser(String userId, SlackNotificationDto slackNotificationDto);
    Object updateNotificationStatus(String userId, SlackNotificationDto slackNotificationDto);
}
