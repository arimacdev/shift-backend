package com.arimac.backend.pmtool.projectmanagementtool.Service;

import com.arimac.backend.pmtool.projectmanagementtool.dtos.*;


public interface ProjectService {
    Object createProject(ProjectDto projectDto);
    Object getAllProjects(String userId);
    Object getProjectByUser(String projectId, String userId);
    Object assignUserToProject(String projectId, UserAssignDto userAssignDto);
    Object updateAssigneeProjectRole(String projectId, String userId, ProjectUserUpdateDto updateDto );
    Object removeProjectAssignee(String projectId, String assignee, ProjectUserDeleteDto deleteDto);
    Object flagProject(String userId, String projectId);
    Object blockOrUnBlockProjectUser(String userId, String projectId, ProjectUserBlockDto projectUserBlockDto);

}
