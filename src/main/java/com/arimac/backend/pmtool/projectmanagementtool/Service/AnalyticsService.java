package com.arimac.backend.pmtool.projectmanagementtool.Service;

public interface AnalyticsService {
    Object getOrgOverview(String userId, String from, String to);
    Object getProjectOverview(String userId, String from, String to);
}
