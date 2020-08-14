package com.arimac.backend.pmtool.projectmanagementtool.Service;

import com.arimac.backend.pmtool.projectmanagementtool.dtos.*;
import com.arimac.backend.pmtool.projectmanagementtool.dtos.Project.ProjectDto;
import com.arimac.backend.pmtool.projectmanagementtool.dtos.Project.ProjectEditDto;
import com.arimac.backend.pmtool.projectmanagementtool.dtos.Project.ProjectUserUpdateDto;
import com.arimac.backend.pmtool.projectmanagementtool.dtos.Project.ProjectWeightUpdateDto;


public interface ProjectService {
    Object createProject(ProjectDto projectDto);
    Object getAllUserAssignedProjects(String userId);
    Object getAllProjects(String userId);
    Object getProjectByUser(String projectId, String userId);
    Object assignUserToProject(String projectId, UserAssignDto userAssignDto);
    Object updateProject(String projectId, ProjectEditDto projectEditDto);
    Object updateProjectWeight(String projectId, String userId, ProjectWeightUpdateDto projectWeightUpdateDto);
    Object updateAssigneeProjectRole(String projectId, String userId, ProjectUserUpdateDto updateDto );
    Object removeProjectAssignee(String projectId, String assignee, ProjectUserDeleteDto deleteDto);
    Object flagProject(String userId, String projectId);
    Object blockOrUnBlockProjectUser(String userId, String projectId, ProjectUserBlockDto projectUserBlockDto);

}
