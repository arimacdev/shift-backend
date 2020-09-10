package com.arimac.backend.pmtool.projectmanagementtool.Service;

import com.arimac.backend.pmtool.projectmanagementtool.enumz.AnalyticsEnum.ChartCriteriaEnum;
import com.arimac.backend.pmtool.projectmanagementtool.enumz.AnalyticsEnum.ProjectDetailsEnum;
import com.arimac.backend.pmtool.projectmanagementtool.enumz.AnalyticsEnum.ProjectSummaryTypeEnum;
import com.arimac.backend.pmtool.projectmanagementtool.enumz.AnalyticsEnum.UserDetailsEnum;
import com.arimac.backend.pmtool.projectmanagementtool.enumz.FilterOrderEnum;

import java.util.Set;

public interface AnalyticsService {
    //Project Analytics
    Object getOrgOverview(String userId, String from, String to);
    Object getProjectOverview(String userId, String from, String to);
    Object getProjectSummary(String userId, String from, String to, Set<String> status, String key,ProjectSummaryTypeEnum orderBy,FilterOrderEnum orderType,int startIndex, int endIndex);
    Object getDetailedProjectDetails(String userId, String from, String to, ProjectDetailsEnum orderBy, FilterOrderEnum orderType, int startIndex, int endIndex);
    //Task Analytics
    Object getTaskRate(String userId, String from, String to, ChartCriteriaEnum criteria);
    //User Analytics
    Object getDetailedUserDetails(String userId, UserDetailsEnum orderBy, FilterOrderEnum orderType, int startIndex, int endIndex, Set<String> userList);

}
