package com.arimac.backend.pmtool.projectmanagementtool.Service;

import com.arimac.backend.pmtool.projectmanagementtool.enumz.AnalyticsEnum.ChartCriteriaEnum;

import java.util.Set;

public interface AnalyticsService {
    //Project Analytics
    Object getOrgOverview(String userId, String from, String to);
    Object getProjectOverview(String userId, String from, String to);
    Object getProjectSummary(String userId, String from, String to, Set<String> status, String key);
    //Task Analytics
    Object getTaskRate(String userId, String from, String to, ChartCriteriaEnum criteria);
}
