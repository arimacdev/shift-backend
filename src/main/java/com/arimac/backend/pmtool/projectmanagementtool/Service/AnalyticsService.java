package com.arimac.backend.pmtool.projectmanagementtool.Service;

import java.util.Set;

public interface AnalyticsService {
    Object getOrgOverview(String userId, String from, String to);
    Object getProjectOverview(String userId, String from, String to);
    Object getProjectSummary(String userId, String from, String to, Set<String> status, String key);
}
