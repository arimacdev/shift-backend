package com.arimac.backend.pmtool.projectmanagementtool.repository;

import com.arimac.backend.pmtool.projectmanagementtool.dtos.Analytics.Project.ProjectDetailAnalysis;
import com.arimac.backend.pmtool.projectmanagementtool.dtos.Analytics.Project.ProjectNumberDto;
import com.arimac.backend.pmtool.projectmanagementtool.dtos.Analytics.Project.ProjectSummaryDto;
import com.arimac.backend.pmtool.projectmanagementtool.dtos.Analytics.Project.ProjectStatusCountDto;
import com.arimac.backend.pmtool.projectmanagementtool.dtos.Internal.Support.ProjectDetails;
import com.arimac.backend.pmtool.projectmanagementtool.dtos.Project.ProjectKeys;
import com.arimac.backend.pmtool.projectmanagementtool.dtos.Project.ProjectPinUnPin;
import com.arimac.backend.pmtool.projectmanagementtool.dtos.Project.ProjectUserResponseDto;
import com.arimac.backend.pmtool.projectmanagementtool.dtos.ServiceDesk.SupportProjectResponse;
import com.arimac.backend.pmtool.projectmanagementtool.enumz.AnalyticsEnum.ProjectDetailsEnum;
import com.arimac.backend.pmtool.projectmanagementtool.enumz.AnalyticsEnum.ProjectSummaryTypeEnum;
import com.arimac.backend.pmtool.projectmanagementtool.enumz.FilterOrderEnum;
import com.arimac.backend.pmtool.projectmanagementtool.enumz.WeightTypeEnum;
import com.arimac.backend.pmtool.projectmanagementtool.model.Project;
import com.arimac.backend.pmtool.projectmanagementtool.model.Project_Keys;
import com.arimac.backend.pmtool.projectmanagementtool.model.Project_User;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;

public interface ProjectRepository {
    Project createProject(Project project);
    Project getProjectById(String projectId);
    Project findProjectByName(String name);
    List<String> getProjectTaskIds(String projectId);
    ProjectUserResponseDto getProjectByIdAndUserId(String projectId, String userId);
    Project_User getProjectUser(String projectId, String userId);
    void pinUnpinProjects(ProjectPinUnPin projectPinUnPin);
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
    ProjectNumberDto getProjectNumbers(String from, String to);
    List<ProjectStatusCountDto> getActiveProjectCountByStatus(String from, String to);
    List<ProjectSummaryDto> getProjectSummary(String from, String to, Set<String> status, Set<String> project, ProjectSummaryTypeEnum orderBy,FilterOrderEnum orderType,int startIndex, int limit);
    LinkedHashMap<String, ProjectDetailAnalysis> getDetailedProjectDetails(String from, String to, ProjectDetailsEnum orderBy, FilterOrderEnum orderType, int startIndex, int endIndex);
    //Project Support
    void addProjectKeys(Project_Keys project_keys);
    Project_Keys getProjectKey(String projectKey);
    Project_Keys getProjectKeyByDomain(String projectKey, String domain);
    void updateProjectKeys(ProjectKeys project_keys);
    List<Project_Keys> getProjectKeys(String projectId);
    void addOrRemoveProjectSupport(String projectId, boolean status);
    List<SupportProjectResponse> getSupportProjects();
    HashMap<String, ProjectDetails> getProjectMapByIds(List<String> projectIds);


}
