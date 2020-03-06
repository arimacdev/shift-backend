package com.arimac.backend.pmtool.projectmanagementtool.Service.Impl;

import com.arimac.backend.pmtool.projectmanagementtool.Service.IdpUserService;
import com.arimac.backend.pmtool.projectmanagementtool.Service.UserService;
import com.arimac.backend.pmtool.projectmanagementtool.dtos.UserRegistrationDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {
    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    private final IdpUserService idpUserService;

    public UserServiceImpl(IdpUserService idpUserService) {
        this.idpUserService = idpUserService;
    }

    @Override
    public Object createUser(UserRegistrationDto userRegistrationDto) {
        String idpUserId = idpUserService.createUser(userRegistrationDto, true);
        return null;
    }
}
