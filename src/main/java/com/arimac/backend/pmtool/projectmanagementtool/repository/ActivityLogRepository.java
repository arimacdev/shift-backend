package com.arimac.backend.pmtool.projectmanagementtool.repository;

import com.arimac.backend.pmtool.projectmanagementtool.dtos.ActivityLog.UserActivityLog;
import com.arimac.backend.pmtool.projectmanagementtool.model.ActivityLog;

import java.util.List;

public interface ActivityLogRepository {
    void addActivityLogEntry(ActivityLog activityLog);
    List<UserActivityLog> getTaskActivity(String taskId);
}
