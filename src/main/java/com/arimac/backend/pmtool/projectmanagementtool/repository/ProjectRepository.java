package com.arimac.backend.pmtool.projectmanagementtool.repository;

import com.arimac.backend.pmtool.projectmanagementtool.dtos.Project.ProjectUserResponseDto;
import com.arimac.backend.pmtool.projectmanagementtool.dtos.Project.ProjectWeightUpdateDto;
import com.arimac.backend.pmtool.projectmanagementtool.enumz.WeightTypeEnum;
import com.arimac.backend.pmtool.projectmanagementtool.model.Project;
import com.arimac.backend.pmtool.projectmanagementtool.model.Project_User;

import java.util.List;

public interface ProjectRepository {
    Project createProject(Project project);
    Project getProjectById(String projectId);
    List<String> getProjectTaskIds(String projectId);
    ProjectUserResponseDto getProjectByIdAndUserId(String projectId, String userId);
    Project_User getProjectUser(String projectId, String userId);
    List<ProjectUserResponseDto> getAllUserAssignedProjects(String userId);
    void updateProject(Project project, String projectId);
    void assignUserToProject(String projectId, Project_User project_user);
    void updateAssigneeProjectRole(Project_User project_user);
    void removeProjectAssignee(String projectId, String assigneeId);
    void flagProject(String projectId);
    void unFlagProject(String projectId);
    void blockOrUnBlockProjectUser(String userId, String projectId, boolean status);
    void blockOrUnblockUserFromAllRelatedProjects(boolean status, String userId);
    void updateIssueCount(String projectId, int issueId);
    boolean checkProjectAlias(String alias);
    void updateProjectWeight(String projectId, WeightTypeEnum weightTypeEnum);

    List<Project> getAllProjects();

}
