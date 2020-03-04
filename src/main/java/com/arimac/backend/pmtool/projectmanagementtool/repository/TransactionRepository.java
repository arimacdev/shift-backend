package com.arimac.backend.pmtool.projectmanagementtool.repository;

import com.arimac.backend.pmtool.projectmanagementtool.dtos.ProjectDto;
import com.arimac.backend.pmtool.projectmanagementtool.dtos.ProjectUserResponseDto;
import com.arimac.backend.pmtool.projectmanagementtool.model.Project;
import com.arimac.backend.pmtool.projectmanagementtool.model.Project_User;

import java.util.List;

public interface TransactionRepository {
    Project createProject(Project project);
    List<ProjectUserResponseDto> getAllProjects();
    List<ProjectUserResponseDto> getAllProjectsByUser(String userId);
}
