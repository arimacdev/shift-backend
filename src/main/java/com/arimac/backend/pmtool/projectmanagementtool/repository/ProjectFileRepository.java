package com.arimac.backend.pmtool.projectmanagementtool.repository;

import com.arimac.backend.pmtool.projectmanagementtool.dtos.ProjectFileResponseDto;
import com.arimac.backend.pmtool.projectmanagementtool.model.ProjectFile;

import java.util.List;

public interface ProjectFileRepository {
    void uploadProjectFile(ProjectFile projectFile);
    List<ProjectFileResponseDto> getAllProjectFiles(String projectId);
    void flagProjectFile(String projectFileId);
    ProjectFile getProjectFile(String projectFile);
    ProjectFile getProjectFileWithFlag(String projectFile);
    List<ProjectFile> getMainProjectFiles(String projectId);
}
