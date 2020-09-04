package com.arimac.backend.pmtool.projectmanagementtool.Service;

import com.arimac.backend.pmtool.projectmanagementtool.dtos.Role.UserRoleDto;
import com.arimac.backend.pmtool.projectmanagementtool.dtos.UserRegistrationDto;
import org.json.JSONArray;
import org.json.JSONObject;

public interface IdpUserService {
    JSONObject createUser(UserRegistrationDto userRegistrationDto, String UUId, boolean firstRequest);
    JSONObject getUserByIdpUserId(String idpUserId, boolean firstRequest);
    void updateUserPassword(String idpUserId, boolean firstRequest, String password);
    void updateUserEmail(String idpUserId, String email, boolean firstRequest);
    void addUserAttributes(String idpUserId, String UUID, boolean firstRequest);
    void changeUserActiveSatatus(String idpUserId, boolean status, boolean firstRequest);
    JSONArray getAllRealmRoles(boolean firstRequest);
    void addRoleToUser(String idpUserId, UserRoleDto userRoleDto, boolean firstRequest);
    void removerUserRole(String idpUserId, UserRoleDto userRoleDto, boolean firstRequest);
    JSONArray getAllUserRoleMappings(String idpUserId, boolean firstRequest);
    void removeAllAssociatedUserSessions(String idpUserId, boolean firstRequest);
    void deleteUserFromIdp(String idpUserId, boolean firstRequest);
}
