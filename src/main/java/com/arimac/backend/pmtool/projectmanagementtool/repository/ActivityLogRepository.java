package com.arimac.backend.pmtool.projectmanagementtool.repository;

import com.arimac.backend.pmtool.projectmanagementtool.dtos.ActivityLog.UserActivityLog;
import com.arimac.backend.pmtool.projectmanagementtool.model.ActivityLog;

import java.util.List;

public interface ActivityLogRepository {
    void addActivityLogEntry(ActivityLog activityLog);
    List<UserActivityLog> getTaskActivity(String taskId, int limit, int offset);
    List<UserActivityLog> getProjectActivity(String projectId, List<String> entityIds, int limit, int offset);
    int projectActivityLogCount(String projectId, List<String> entityIds);
    int taskActivityLogCount(String taskId);
    void flagEntityActivityLogs(String taskId);
}
