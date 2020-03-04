package com.arimac.backend.pmtool.projectmanagementtool.Service;

import com.arimac.backend.pmtool.projectmanagementtool.dtos.ProjectDto;

public interface ProjectService {
    Object createProject(ProjectDto projectDto);
}
