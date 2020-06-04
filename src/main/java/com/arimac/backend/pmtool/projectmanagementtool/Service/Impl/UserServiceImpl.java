package com.arimac.backend.pmtool.projectmanagementtool.Service.Impl;

import com.arimac.backend.pmtool.projectmanagementtool.Response.Response;
import com.arimac.backend.pmtool.projectmanagementtool.Service.IdpUserService;
import com.arimac.backend.pmtool.projectmanagementtool.Service.UserService;
import com.arimac.backend.pmtool.projectmanagementtool.dtos.*;
import com.arimac.backend.pmtool.projectmanagementtool.dtos.User.UserActiveStatusDto;
import com.arimac.backend.pmtool.projectmanagementtool.enumz.ProjectStatusEnum;
import com.arimac.backend.pmtool.projectmanagementtool.enumz.ResponseMessage;
import com.arimac.backend.pmtool.projectmanagementtool.exception.ErrorMessage;
import com.arimac.backend.pmtool.projectmanagementtool.exception.PMException;
import com.arimac.backend.pmtool.projectmanagementtool.model.Project;
import com.arimac.backend.pmtool.projectmanagementtool.model.Project_User;
import com.arimac.backend.pmtool.projectmanagementtool.model.User;
import com.arimac.backend.pmtool.projectmanagementtool.repository.ProjectRepository;
import com.arimac.backend.pmtool.projectmanagementtool.repository.UserRepository;
import com.arimac.backend.pmtool.projectmanagementtool.utils.UtilsService;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    private static final String USERNAME = "username";
    private static final String DEFAULT_PROJECT = "a36e22fa-16c3-49c3-b1fd-b546c0bf1092";
    private static final String DEFAULT_PROJECT_NAME = "My Default Project";
    private static final String CLIENT_ID = "Arimac Lanka";



    private final IdpUserService idpUserService;
    private final UserRepository userRepository;
    private final ProjectRepository projectRepository;
    private final UtilsService utilsService;

    public UserServiceImpl(IdpUserService idpUserService, UserRepository userRepository, ProjectRepository projectRepository, UtilsService utilsService) {
        this.idpUserService = idpUserService;
        this.userRepository = userRepository;
        this.projectRepository = projectRepository;
        this.utilsService = utilsService;
    }

    @Override
    public Object createUser(UserRegistrationDto userRegistrationDto) {
        if ((userRegistrationDto.getFirstName() == null ||userRegistrationDto.getFirstName().isEmpty()) || (userRegistrationDto.getLastName() == null ||userRegistrationDto.getLastName().isEmpty()) ||(userRegistrationDto.getEmail() == null ||userRegistrationDto.getEmail().isEmpty()))
            return new ErrorMessage(ResponseMessage.INVALID_REQUEST_BODY, HttpStatus.BAD_REQUEST);
        String userUUID = utilsService.getUUId();
        JSONObject idpUser = new JSONObject();
        String idpUserId = null;
        String userName = null;
        try {
             idpUser = idpUserService.createUser(userRegistrationDto,  userUUID, true);
             idpUserId= idpUser.getString("id");
             userName = idpUser.getString("username");
        } catch (Exception e){
            return new ErrorMessage(e.getMessage(), HttpStatus.UNPROCESSABLE_ENTITY);
        }
        if (idpUserId == null || userName == null)
            return new PMException("IDP Server Error");
        User user = new User();
        user.setUserId(userUUID);
        user.setIdpUserId(idpUserId);
        user.setUsername(userName);
        user.setFirstName(userRegistrationDto.getFirstName());
        user.setLastName(userRegistrationDto.getLastName());
        user.setEmail(userRegistrationDto.getEmail());

        userRepository.createUser(user);
        Project_User assignToDefault = new Project_User();
        assignToDefault.setProjectId(DEFAULT_PROJECT);
        assignToDefault.setAssigneeId(userUUID);
        assignToDefault.setAssigneeJobRole("Guest");
        assignToDefault.setAssigneeProjectRole(3);
        assignToDefault.setAssignedAt(utilsService.getCurrentTimestamp());
        projectRepository.assignUserToProject(DEFAULT_PROJECT, assignToDefault);

        return new Response(ResponseMessage.SUCCESS);
    }

    @Override
    public Object createFirstUser(UserRegistrationDto userRegistrationDto) {
        String userUUID = utilsService.getUUId();
        JSONObject idpUserId = idpUserService.createUser(userRegistrationDto,  userUUID, true);
        User user = new User();
        user.setUserId(userUUID);
        if (idpUserId != null){
            user.setIdpUserId(idpUserId.getString("userId"));
        } else {
            user.setIdpUserId("idpUserId");
        }
        user.setFirstName(userRegistrationDto.getFirstName());
        user.setLastName(userRegistrationDto.getLastName());
        user.setEmail(userRegistrationDto.getEmail());
        userRepository.createUser(user);

        Project project = new Project();
        String projectId = DEFAULT_PROJECT;
        project.setProjectId(projectId);
        project.setProjectName(DEFAULT_PROJECT_NAME);
        project.setClientId(CLIENT_ID);
        project.setProjectStartDate(utilsService.getCurrentTimestamp());
        project.setProjectEndDate(utilsService.getCurrentTimestamp());
        project.setProjectStatus(ProjectStatusEnum.presales);
        project.setIsDeleted(false);
        projectRepository.createProject(project);

        Project_User assignToDefault = new Project_User();
        assignToDefault.setProjectId(DEFAULT_PROJECT);
        assignToDefault.setAssigneeId(userUUID);
        assignToDefault.setAssigneeJobRole("Admin");
        assignToDefault.setAssigneeProjectRole(2);
        assignToDefault.setAssignedAt(utilsService.getCurrentTimestamp());
        projectRepository.assignUserToProject(DEFAULT_PROJECT, assignToDefault);

        return new Response(ResponseMessage.SUCCESS);
    }

    @Override
    public Object getAllUsers() { //Access for all users
        List<User> userList = userRepository.getAllUsers();
        List<UserListResponseDto> userResponseList = new ArrayList<>();
        for (User user : userList){
//            JSONObject idpUser = idpUserService.getUserByIdpUserId(user.getIdpUserId(), true);
            UserListResponseDto userResponse = new UserListResponseDto();
            userResponse.setUserId(user.getUserId());
            userResponse.setFirstName(user.getFirstName());
            userResponse.setLastName(user.getLastName());
            userResponse.setEmail(user.getEmail());
            userResponse.setIdpUserId(user.getIdpUserId());
            userResponse.setUserName(user.getUsername());
            userResponse.setProfileImage(user.getProfileImage());
            userResponse.setIsActive(user.getIsActive());
            userResponseList.add(userResponse);
        }
        return new Response(ResponseMessage.SUCCESS, userResponseList);
    }

    @Override
    public Object getUserByUserId(String userId) {
        User user = userRepository.getUserByUserId(userId);
        if (user == null)
            return new ErrorMessage(ResponseMessage.USER_NOT_FOUND, HttpStatus.NOT_FOUND);

        //JSONObject IdpUser = idpUserService.getUserByIdpUserId(user.getIdpUserId(), true);
        UserResponseDto userResponseDto = new UserResponseDto();
        userResponseDto.setUserId(user.getUserId());
        userResponseDto.setUserName(user.getUsername());
        userResponseDto.setFirstName(user.getFirstName());
        userResponseDto.setLastName(user.getLastName());
        userResponseDto.setEmail(user.getEmail());
        userResponseDto.setProfileImage(user.getProfileImage());
        userResponseDto.setUserSlackId(user.getUserSlackId());
        userResponseDto.setNotification(user.getNotification());
        userResponseDto.setIsActive(user.getIsActive());

        return new Response(ResponseMessage.SUCCESS, userResponseDto);
    }

    @Override
    public Object updateUserByUserId(String userId, UserUpdateDto userUpdateDto) {
        if (userUpdateDto.getFirstName().isEmpty() || userUpdateDto.getFirstName() == null || userUpdateDto.getLastName().isEmpty() || userUpdateDto.getLastName() == null || userUpdateDto.getEmail().isEmpty() || userUpdateDto.getEmail() == null)
            return new ErrorMessage(ResponseMessage.INVALID_REQUEST_BODY, HttpStatus.BAD_REQUEST);
        User user = userRepository.getUserByUserId(userId);
        if (user == null)
            return new ErrorMessage(ResponseMessage.USER_NOT_FOUND, HttpStatus.NOT_FOUND);
        UserUpdateDto dto = new UserUpdateDto();
        if (userUpdateDto.getEmail() != null){
            dto.setEmail(userUpdateDto.getEmail());
            idpUserService.updateUserEmail(user.getIdpUserId(), userUpdateDto.getEmail(), true);
        } else {
            dto.setEmail(user.getEmail());
        }
        if (userUpdateDto.getFirstName() !=null){
            dto.setFirstName(userUpdateDto.getFirstName());
        } else {
            dto.setFirstName(user.getFirstName());
        }
        if (userUpdateDto.getLastName() !=null){
            dto.setLastName(userUpdateDto.getLastName());
        } else {
            dto.setLastName(user.getLastName());
        }
        if (userUpdateDto.getPassword() != null)
            idpUserService.updateUserPassword(user.getIdpUserId(), true, userUpdateDto.getPassword());
        userRepository.updateUserByUserId(userId, dto);

        return new Response(ResponseMessage.SUCCESS);
    }

    @Override
    public Object getAllProjectUsers(String projectId) {
        List<User> userList = userRepository.getAllProjectUsers(projectId);
        return new Response(ResponseMessage.SUCCESS, HttpStatus.OK, userList);
    }

    @Override
    public Object getAllBlockedProjectUsers(String projectId) {
        userRepository.getAllBlockedProjectUsers(projectId);
        return null;
    }

    @Override
    public Object addSlackIdToUser(String userId, SlackNotificationDto slackNotificationDto) {
        if (!slackNotificationDto.getSlackAssignerId().equals(slackNotificationDto.getSlackAssigneeId()))
            return new ErrorMessage(ResponseMessage.UNAUTHORIZED, HttpStatus.UNAUTHORIZED);
        User user = userRepository.getUserByUserId(slackNotificationDto.getSlackAssigneeId());
        if (user == null)
            return new ErrorMessage(ResponseMessage.USER_NOT_FOUND, HttpStatus.NOT_FOUND);
        userRepository.addSlackIdToUser(userId, slackNotificationDto.getAssigneeSlackId());
        return new Response(ResponseMessage.SUCCESS);
    }

    @Override
    public Object updateNotificationStatus(String userId, SlackNotificationDto slackNotificationDto) {
        User user = userRepository.getUserByUserId(slackNotificationDto.getSlackAssigneeId());
        if (user == null)
            return new ErrorMessage(ResponseMessage.USER_NOT_FOUND, HttpStatus.NOT_FOUND);
        if (!slackNotificationDto.getSlackAssignerId().equals(slackNotificationDto.getSlackAssigneeId()))
            return new ErrorMessage(ResponseMessage.UNAUTHORIZED, HttpStatus.UNAUTHORIZED);
        if (user.getUserSlackId() == null)
            return new ErrorMessage("You haven't activated slack notifications", HttpStatus.UNAUTHORIZED);
        userRepository.updateNotificationStatus(slackNotificationDto.getSlackAssigneeId(), slackNotificationDto);
        return new Response(ResponseMessage.SUCCESS, HttpStatus.OK);
    }

    @Override
    public Object deactivateUser(UserActiveStatusDto userActiveStatusDto) {
        //DO ADMIN Validation
        User Admin = userRepository.getUserByUserId(userActiveStatusDto.getAdminId());
        if (Admin == null)
            return new ErrorMessage(ResponseMessage.ADMIN_USER_NOT_FOUND, HttpStatus.NOT_FOUND);
        User user = userRepository.getUserByUserId(userActiveStatusDto.getUserId());
        if (user == null)
            return new ErrorMessage(ResponseMessage.USER_NOT_FOUND, HttpStatus.NOT_FOUND);
        if (!user.getIsActive())
            return new ErrorMessage(ResponseMessage.ALREADY_DEACTIVATED, HttpStatus.UNPROCESSABLE_ENTITY);
        idpUserService.changeUserActiveSatatus(user.getIdpUserId(), false, true);
        userRepository.changeUserUpdateStatus(userActiveStatusDto.getUserId(), false);
        projectRepository.blockOrUnblockUserFromAllRelatedProjects(true, userActiveStatusDto.getUserId());
        return new Response(ResponseMessage.SUCCESS, HttpStatus.OK);
    }

    @Override
    public Object activateUser(UserActiveStatusDto userActiveStatusDto) {
        //DO ADMIN Validation
        User Admin = userRepository.getUserByUserId(userActiveStatusDto.getAdminId());
        if (Admin == null)
            return new ErrorMessage(ResponseMessage.ADMIN_USER_NOT_FOUND, HttpStatus.NOT_FOUND);
        User user = userRepository.getUserByUserId(userActiveStatusDto.getUserId());
        if (user == null)
            return new ErrorMessage(ResponseMessage.USER_NOT_FOUND, HttpStatus.NOT_FOUND);
        if (user.getIsActive())
            return new ErrorMessage(ResponseMessage.ALREADY_ACTIVATED, HttpStatus.UNPROCESSABLE_ENTITY);
        idpUserService.changeUserActiveSatatus(user.getIdpUserId(), true, true);
        userRepository.changeUserUpdateStatus(userActiveStatusDto.getUserId(), true);
        projectRepository.blockOrUnblockUserFromAllRelatedProjects(false, userActiveStatusDto.getUserId());
        return new Response(ResponseMessage.SUCCESS, HttpStatus.OK);
    }

}
