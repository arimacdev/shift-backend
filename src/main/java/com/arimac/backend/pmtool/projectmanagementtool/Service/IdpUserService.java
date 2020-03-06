package com.arimac.backend.pmtool.projectmanagementtool.Service;

import com.arimac.backend.pmtool.projectmanagementtool.dtos.UserRegistrationDto;

public interface IdpUserService {
    String createUser(UserRegistrationDto userRegistrationDto, boolean firstRequest);
}
