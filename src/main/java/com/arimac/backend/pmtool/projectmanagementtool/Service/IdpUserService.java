package com.arimac.backend.pmtool.projectmanagementtool.Service;

import com.arimac.backend.pmtool.projectmanagementtool.dtos.Role.AddUserRoleDto;
import com.arimac.backend.pmtool.projectmanagementtool.dtos.UserRegistrationDto;
import org.json.JSONArray;
import org.json.JSONObject;

public interface IdpUserService {
    String createUser(UserRegistrationDto userRegistrationDto, String UUId, boolean firstRequest);
    JSONObject getUserByIdpUserId(String idpUserId, boolean firstRequest);
    void updateUserPassword(String idpUserId);
    void updateUserEmail(String idpUserId, String email, boolean firstRequest);
    void changeUserActiveSatatus(String idpUserId, boolean status, boolean firstRequest);
    JSONArray getAllRealmRoles(boolean firstRequest);
    void addRoleToUser(String idpUserId, AddUserRoleDto addUserRoleDto, boolean firstRequest);
}
