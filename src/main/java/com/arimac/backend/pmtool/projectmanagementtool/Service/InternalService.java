package com.arimac.backend.pmtool.projectmanagementtool.Service;

public interface InternalService {
    Object updateProjectAlias();
    Object migratePersonalTask();
    Object addUserRole();
    Object addUserNameToUsers();
    Object addUserToAllProjects(String userId);
    Object getProjectById(String projectId);
}
