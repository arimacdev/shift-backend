package com.arimac.backend.pmtool.projectmanagementtool.Service;

import com.arimac.backend.pmtool.projectmanagementtool.model.ActivityLog;

public interface ActivityLogService {
    void addTaskLog(ActivityLog activityLog);
    Object getTaskActivity(String userId, String taskId, int startIndex, int endIndex,boolean allLogs);
    Object getAllProjectLogsWithTasks(String userId, String projectId, int startIndex, int endIndex);
    void flagEntityActivityLogs(String entityId);
}
