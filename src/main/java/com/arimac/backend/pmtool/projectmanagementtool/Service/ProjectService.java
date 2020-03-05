package com.arimac.backend.pmtool.projectmanagementtool.Service;

import com.arimac.backend.pmtool.projectmanagementtool.dtos.ProjectDto;
import com.arimac.backend.pmtool.projectmanagementtool.dtos.ProjectUserDeleteDto;
import com.arimac.backend.pmtool.projectmanagementtool.dtos.ProjectUserUpdateDto;
import com.arimac.backend.pmtool.projectmanagementtool.dtos.UserAssignDto;

import java.util.List;

public interface ProjectService {
    Object createProject(ProjectDto projectDto);
    Object getAllProjects(String userId);
//    Object getProjectByUser(String projectId, String userId);
    Object assignUserToProject(String projectId, UserAssignDto userAssignDto);
    Object updateAssigneeProjectRole(String projectId, String userId, ProjectUserUpdateDto updateDto );
    Object removeProjectAssignee(String projectId, String assignee, ProjectUserDeleteDto deleteDto);

}
