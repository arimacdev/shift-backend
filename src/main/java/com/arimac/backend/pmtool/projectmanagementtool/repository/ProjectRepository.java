package com.arimac.backend.pmtool.projectmanagementtool.repository;

import com.arimac.backend.pmtool.projectmanagementtool.dtos.ProjectDto;
import com.arimac.backend.pmtool.projectmanagementtool.dtos.ProjectUserResponseDto;
import com.arimac.backend.pmtool.projectmanagementtool.model.Project;
import com.arimac.backend.pmtool.projectmanagementtool.model.Project_User;

import java.util.List;

public interface ProjectRepository {
    Project createProject(Project project);
    Project getProjectById(String projectId);
    ProjectUserResponseDto getProjectByIdAndUserId(String projectId, String userId);
    List<ProjectUserResponseDto> getAllProjectsByUser(String userId);
    void updateProject(Project project, String projectId);
    void assignUserToProject(String projectId, Project_User project_user);
    void updateAssigneeProjectRole(Project_User project_user);
    void removeProjectAssignee(String projectId, String assigneeId);
    void flagProject(String projectId);
    void unFlagProject(String projectId);
    void blockOrUnBlockProjectUser(String userId, String projectId, boolean status);
    void updateIssueCount(String projectId, int issueId);
    boolean checkProjectAlias(String alias);

    //INTERNAL
    List<Project> getAllProjects();

}
