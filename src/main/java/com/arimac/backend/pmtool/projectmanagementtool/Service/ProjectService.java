package com.arimac.backend.pmtool.projectmanagementtool.Service;

import com.arimac.backend.pmtool.projectmanagementtool.dtos.ProjectDto;

import java.util.List;

public interface ProjectService {
    Object createProject(ProjectDto projectDto);
    Object getAllProjects(String userId);
//    Object getProject(String projectId);
}
