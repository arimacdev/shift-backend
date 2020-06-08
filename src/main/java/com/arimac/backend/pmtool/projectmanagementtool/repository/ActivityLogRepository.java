package com.arimac.backend.pmtool.projectmanagementtool.repository;

import com.arimac.backend.pmtool.projectmanagementtool.model.ActivityLog;

public interface ActivityLogRepository {
    void addActivityLogEntry(ActivityLog activityLog);
}
