package com.arimac.backend.pmtool.projectmanagementtool.Service;

import com.arimac.backend.pmtool.projectmanagementtool.dtos.UserRegistrationDto;

public interface IdpUserService {
    Object createUser(UserRegistrationDto userRegistrationDto);
}
