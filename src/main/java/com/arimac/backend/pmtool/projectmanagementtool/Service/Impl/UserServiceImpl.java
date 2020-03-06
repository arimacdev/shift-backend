package com.arimac.backend.pmtool.projectmanagementtool.Service.Impl;

import com.arimac.backend.pmtool.projectmanagementtool.Response.Response;
import com.arimac.backend.pmtool.projectmanagementtool.Service.IdpUserService;
import com.arimac.backend.pmtool.projectmanagementtool.Service.UserService;
import com.arimac.backend.pmtool.projectmanagementtool.dtos.UserRegistrationDto;
import com.arimac.backend.pmtool.projectmanagementtool.dtos.UserUpdateDto;
import com.arimac.backend.pmtool.projectmanagementtool.enumz.ResponseMessage;
import com.arimac.backend.pmtool.projectmanagementtool.model.User;
import com.arimac.backend.pmtool.projectmanagementtool.repository.UserRepository;
import com.arimac.backend.pmtool.projectmanagementtool.utils.UtilsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

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
    public List<User> getAllUsers() { //Access for all users
        return userRepository.getAllUsers();
    }

    @Override
    public Object getUserByUserId(String userId) {
        return userRepository.getUserByUserId(userId);
    }

    @Override
    public Object updateUserByUserId(String userId, UserUpdateDto userUpdateDto) {
        userRepository.updateUserByUserId(userId, userUpdateDto);
        return null;
    }


}
