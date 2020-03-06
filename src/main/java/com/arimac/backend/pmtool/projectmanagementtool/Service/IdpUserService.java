package com.arimac.backend.pmtool.projectmanagementtool.Service;

import com.arimac.backend.pmtool.projectmanagementtool.dtos.UserRegistrationDto;
import org.json.JSONObject;

public interface IdpUserService {
    String createUser(UserRegistrationDto userRegistrationDto, boolean firstRequest);
    JSONObject getUserByIdpUserId(String idpUserId, boolean firstRequest);
    void updateUserPassword(String idpUserId);
    void updateUserEmail(String idpUserId, String email, boolean firstRequest);
}
