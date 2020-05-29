package com.arimac.backend.pmtool.projectmanagementtool.Service;

import com.arimac.backend.pmtool.projectmanagementtool.dtos.Role.AddUserRoleDto;

public interface AdminService {
    Object getAllRealmRoles(String userId);
    Object getMobileStatus(String platform, int version);
    Object addRoleToUser(String userId, AddUserRoleDto addUserRoleDto);
}
