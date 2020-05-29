package com.arimac.backend.pmtool.projectmanagementtool.Service;

public interface AdminService {
    Object getAllRealmRoles(String userId);
    Object getMobileStatus(String platform, int version);
}
