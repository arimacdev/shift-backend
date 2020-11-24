package com.arimac.backend.pmtool.projectmanagementtool.Service;

import java.util.List;
import java.util.Set;

public interface InternalService {
    Object updateProjectAlias();
    Object migratePersonalTask();
    Object addUserRole();
    Object addUserNameToUsers();
    Object addUserToAllProjects(String userId);
    Object getProjectById(String projectId);
    Object getUsersByIds(Set<String> users);
    Object getOrganizationById(String organizationId);
    Object getProjectMapByIds(List<String> projectIds);
}
