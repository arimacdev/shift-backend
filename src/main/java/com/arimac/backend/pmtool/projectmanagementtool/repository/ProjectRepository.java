package com.arimac.backend.pmtool.projectmanagementtool.repository;

import com.arimac.backend.pmtool.projectmanagementtool.dtos.Analytics.Project.ProjectSummaryDto;
import com.arimac.backend.pmtool.projectmanagementtool.dtos.Analytics.ProjectStatusCountDto;
import com.arimac.backend.pmtool.projectmanagementtool.dtos.Project.ProjectUserResponseDto;
import com.arimac.backend.pmtool.projectmanagementtool.enumz.AnalyticsEnum.ProjectSummaryTypeEnum;
import com.arimac.backend.pmtool.projectmanagementtool.enumz.FilterOrderEnum;
import com.arimac.backend.pmtool.projectmanagementtool.enumz.WeightTypeEnum;
import com.arimac.backend.pmtool.projectmanagementtool.model.Project;
import com.arimac.backend.pmtool.projectmanagementtool.model.Project_User;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public interface ProjectRepository {
    Project createProject(Project project);
    Project getProjectById(String projectId);
    Project findProjectByName(String name);
    List<String> getProjectTaskIds(String projectId);
    ProjectUserResponseDto getProjectByIdAndUserId(String projectId, String userId);
    Project_User getProjectUser(String projectId, String userId);
    Project_User getProjectUserWithBlockedStatus(String projectId, String userId);
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
    //Analytics
    int getActiveProjectCount(String from, String to);
    List<ProjectStatusCountDto> getActiveProjectCountByStatus(String from, String to);
    List<ProjectSummaryDto> getProjectSummary(String from, String to, Set<String> status, String key, ProjectSummaryTypeEnum orderBy,FilterOrderEnum orderType,int startIndex, int limit);


}
