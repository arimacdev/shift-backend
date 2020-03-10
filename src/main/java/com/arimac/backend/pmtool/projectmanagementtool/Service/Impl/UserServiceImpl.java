package com.arimac.backend.pmtool.projectmanagementtool.Service.Impl;

import com.arimac.backend.pmtool.projectmanagementtool.Response.Response;
import com.arimac.backend.pmtool.projectmanagementtool.Service.IdpUserService;
import com.arimac.backend.pmtool.projectmanagementtool.Service.UserService;
import com.arimac.backend.pmtool.projectmanagementtool.dtos.UserRegistrationDto;
import com.arimac.backend.pmtool.projectmanagementtool.dtos.UserResponseDto;
import com.arimac.backend.pmtool.projectmanagementtool.dtos.UserUpdateDto;
import com.arimac.backend.pmtool.projectmanagementtool.enumz.ResponseMessage;
import com.arimac.backend.pmtool.projectmanagementtool.exception.ErrorMessage;
import com.arimac.backend.pmtool.projectmanagementtool.model.User;
import com.arimac.backend.pmtool.projectmanagementtool.repository.UserRepository;
import com.arimac.backend.pmtool.projectmanagementtool.utils.UtilsService;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    private static final String USERNAME = "username";

    private final IdpUserService idpUserService;
    private final UserRepository userRepository;
    private final UtilsService utilsService;

    public UserServiceImpl(IdpUserService idpUserService, UserRepository userRepository, UtilsService utilsService) {
        this.idpUserService = idpUserService;
        this.userRepository = userRepository;
        this.utilsService = utilsService;
    }

    @Override
    public Object createUser(UserRegistrationDto userRegistrationDto) {
        String idpUserId = idpUserService.createUser(userRegistrationDto, true);
        User user = new User();
        user.setUserId(utilsService.getUUId());
        user.setIdpUserId(idpUserId);
        user.setFirstName(userRegistrationDto.getFirstName());
        user.setLastName(userRegistrationDto.getLastName());
        user.setEmail(userRegistrationDto.getEmail());

        userRepository.createUser(user);

        return new Response(ResponseMessage.SUCCESS);
    }

    @Override
    public Object getAllUsers() { //Access for all users
        List<User> userList = userRepository.getAllUsers();
        return new Response(ResponseMessage.SUCCESS, userList);
    }

    @Override
    public Object getUserByUserId(String userId) {
        User user = userRepository.getUserByUserId(userId);
        if (user == null)
            return new ErrorMessage(ResponseMessage.NO_RECORD, HttpStatus.NOT_FOUND);

        JSONObject IdpUser = idpUserService.getUserByIdpUserId(user.getIdpUserId(), true);
        UserResponseDto userResponseDto = new UserResponseDto();
        userResponseDto.setUserName(IdpUser.getString(USERNAME));
        userResponseDto.setFirstName(user.getFirstName());
        userResponseDto.setLastName(user.getLastName());
        userResponseDto.setEmail(user.getEmail());

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


}
