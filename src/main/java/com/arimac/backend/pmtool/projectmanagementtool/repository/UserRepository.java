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
    List<User> getUserListById(List<String> userId);
    List<User> getAllUsersWithPagination(int limit, int offset);
    User getUserByUserId(String userId);
    User getUserByIdpUserId(String idpUserId);
    User getUserWithFlag(String userId);
    Object updateUserByUserId(String userId, UserUpdateDto userUpdateDto);
    void updateProfilePicture(String userId, String profilePictureUrl);
    List<User> getAllProjectUsers(String projectId);
    List<UserProjectDto> getUsersProjectDetails(String projectId);
    List<UserTaskGroupDto> getUsersTaskGroupDetails(String taskGroupId);
    Object getAllBlockedProjectUsers(String projectId);
    void addSlackIdToUser(String userId, String slackId);
    void updateNotificationStatus(String userId, SlackNotificationDto slackNotificationDto);
    void changeUserUpdateStatus(String userId, boolean status);
    //Analytics
    int getActiveUserCount(String from ,String to);
    //REMOVE
    void updateUserName(String userId, String username);

}
