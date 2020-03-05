package com.arimac.backend.pmtool.projectmanagementtool.repository;

import com.arimac.backend.pmtool.projectmanagementtool.dtos.ProjectDto;
import com.arimac.backend.pmtool.projectmanagementtool.dtos.ProjectUserResponseDto;
import com.arimac.backend.pmtool.projectmanagementtool.model.Project;
import com.arimac.backend.pmtool.projectmanagementtool.model.Project_User;

import java.util.List;

public interface ProjectRepository {
    Project createProject(Project project);
    ProjectUserResponseDto getProjectByIdAndUserId(String projectId, String userId);
    List<ProjectUserResponseDto> getAllProjects();
    List<ProjectUserResponseDto> getAllProjectsByUser(String userId);
    void assignUserToProject(String projectId, Project_User project_user);
}
