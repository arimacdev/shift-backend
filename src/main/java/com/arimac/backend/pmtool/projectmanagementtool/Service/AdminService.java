package com.arimac.backend.pmtool.projectmanagementtool.Service;

import com.arimac.backend.pmtool.projectmanagementtool.dtos.Role.UserRoleDto;

public interface AdminService {
    Object getAllRealmRoles(String userId);
    Object getMobileStatus(String platform, int version);
    Object addRoleToUser(String userId, UserRoleDto userRoleDto);
    Object removerUserRole(String userId, UserRoleDto userRoleDto);
}
