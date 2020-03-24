package com.arimac.backend.pmtool.projectmanagementtool.Service.Impl;

import com.arimac.backend.pmtool.projectmanagementtool.Response.Response;
import com.arimac.backend.pmtool.projectmanagementtool.Service.IdpUserService;
import com.arimac.backend.pmtool.projectmanagementtool.Service.UserService;
import com.arimac.backend.pmtool.projectmanagementtool.dtos.UserListResponseDto;
import com.arimac.backend.pmtool.projectmanagementtool.dtos.UserRegistrationDto;
import com.arimac.backend.pmtool.projectmanagementtool.dtos.UserResponseDto;
import com.arimac.backend.pmtool.projectmanagementtool.dtos.UserUpdateDto;
import com.arimac.backend.pmtool.projectmanagementtool.enumz.ProjectStatusEnum;
import com.arimac.backend.pmtool.projectmanagementtool.enumz.ResponseMessage;
import com.arimac.backend.pmtool.projectmanagementtool.exception.ErrorMessage;
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
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Calendar;
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
        String userUUID = utilsService.getUUId();
        String idpUserId = idpUserService.createUser(userRegistrationDto,  userUUID, true);
        User user = new User();
        user.setUserId(userUUID);
        if (idpUserId != null){
            user.setIdpUserId(idpUserId);
        } else {
            user.setIdpUserId("idpUserId");
        }
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
        String idpUserId = idpUserService.createUser(userRegistrationDto,  userUUID, true);
        User user = new User();
        user.setUserId(userUUID);
        if (idpUserId != null){
            user.setIdpUserId(idpUserId);
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
            userResponse.setIdpUserId(user.getIdpUserId());
            userResponse.setUserName(user.getIdpUserId());
            userResponse.setProfileImage(user.getProfileImage());
            userResponseList.add(userResponse);
        }
        return new Response(ResponseMessage.SUCCESS, userResponseList);
    }

    @Override
    public Object getUserByUserId(String userId) {
        User user = userRepository.getUserByUserId(userId);
        if (user == null)
            return new ErrorMessage(ResponseMessage.NO_RECORD, HttpStatus.NOT_FOUND);

        JSONObject IdpUser = idpUserService.getUserByIdpUserId(user.getIdpUserId(), true);
        UserResponseDto userResponseDto = new UserResponseDto();
        userResponseDto.setUserId(user.getUserId());
        userResponseDto.setUserName(IdpUser.getString(USERNAME));
        userResponseDto.setFirstName(user.getFirstName());
        userResponseDto.setLastName(user.getLastName());
        userResponseDto.setEmail(user.getEmail());
        userResponseDto.setProfileImage(user.getProfileImage());

        return new Response(ResponseMessage.SUCCESS, userResponseDto);
    }

    @Override
    public Object updateUserByUserId(String userId, UserUpdateDto userUpdateDto) {
        User user = userRepository.getUserByUserId(userId);
        if (user == null)
            return new ErrorMessage(ResponseMessage.NO_RECORD, HttpStatus.NOT_FOUND);
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
        if (userUpdateDto.getPassword() !=null)
            idpUserService.updateUserPassword(user.getIdpUserId());
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

}
