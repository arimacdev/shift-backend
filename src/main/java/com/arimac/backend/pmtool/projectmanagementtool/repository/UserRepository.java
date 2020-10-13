package com.arimac.backend.pmtool.projectmanagementtool.repository;

import com.arimac.backend.pmtool.projectmanagementtool.dtos.Analytics.User.UserActivityDto;
import com.arimac.backend.pmtool.projectmanagementtool.dtos.Analytics.User.UserDetailedAnalysis;
import com.arimac.backend.pmtool.projectmanagementtool.dtos.Analytics.User.UserNumberDto;
import com.arimac.backend.pmtool.projectmanagementtool.dtos.Project_UserDto;
import com.arimac.backend.pmtool.projectmanagementtool.dtos.SlackNotificationDto;
import com.arimac.backend.pmtool.projectmanagementtool.dtos.TaskGroup.UserTaskGroupDto;
import com.arimac.backend.pmtool.projectmanagementtool.dtos.UserProjectDto;
import com.arimac.backend.pmtool.projectmanagementtool.dtos.UserUpdateDto;
import com.arimac.backend.pmtool.projectmanagementtool.enumz.AnalyticsEnum.ChartCriteriaEnum;
import com.arimac.backend.pmtool.projectmanagementtool.enumz.AnalyticsEnum.UserDetailsEnum;
import com.arimac.backend.pmtool.projectmanagementtool.enumz.FilterOrderEnum;
import com.arimac.backend.pmtool.projectmanagementtool.model.User;

import java.util.HashMap;
import java.util.List;
import java.util.Set;

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
    List<Project_UserDto> getAllProjectUsers(String projectId);
    List<UserProjectDto> getUsersProjectDetails(String projectId);
    List<UserTaskGroupDto> getUsersTaskGroupDetails(String taskGroupId);
    void addSlackIdToUser(String userId, String slackId);
    void updateNotificationStatus(String userId, SlackNotificationDto slackNotificationDto);
    void changeUserUpdateStatus(String userId, boolean status);
    //Analytics
    UserNumberDto getActiveUserCount(String from , String to);
    List<UserDetailedAnalysis> getDetailedUserDetails(UserDetailsEnum orderBy, FilterOrderEnum orderType, int startIndex, int limit, Set<String> userList);
    HashMap<String,UserActivityDto> getUserActivity(String from, String to, ChartCriteriaEnum criteria);
    //REMOVE
    void updateUserName(String userId, String username);
    HashMap<String, User> getUsersByIds(Set<String> userList);

}
