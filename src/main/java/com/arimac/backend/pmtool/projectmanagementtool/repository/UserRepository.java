package com.arimac.backend.pmtool.projectmanagementtool.repository;

import com.arimac.backend.pmtool.projectmanagementtool.dtos.SlackNotificationDto;
import com.arimac.backend.pmtool.projectmanagementtool.dtos.TaskGroup.UserTaskGroupDto;
import com.arimac.backend.pmtool.projectmanagementtool.dtos.UserProjectDto;
import com.arimac.backend.pmtool.projectmanagementtool.dtos.UserUpdateDto;
import com.arimac.backend.pmtool.projectmanagementtool.model.User;

import java.util.List;

public interface UserRepository {
    Object createUser(User user);
    List<User> getAllUsers();
    User getUserByUserId(String userId);
    Object updateUserByUserId(String userId, UserUpdateDto userUpdateDto);
    void updateProfilePicture(String userId, String profilePictureUrl);
    List<User> getAllProjectUsers(String projectId);
    List<UserProjectDto> getUsersProjectDetails(String projectId);
    List<UserTaskGroupDto> getUsersTaskGroupDetails(String taskGroupId);
    Object getAllBlockedProjectUsers(String projectId);
    void addSlackIdToUser(String userId, String slackId);
    void updateNotificationStatus(String userId, SlackNotificationDto slackNotificationDto);
    void changeUserUpdateStatus(String userId, boolean status);
}
