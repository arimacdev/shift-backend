package com.arimac.backend.pmtool.projectmanagementtool.Service;

import com.arimac.backend.pmtool.projectmanagementtool.model.ActivityLog;
import com.arimac.backend.pmtool.projectmanagementtool.model.Task;
import com.arimac.backend.pmtool.projectmanagementtool.model.TaskLog;

public interface ActivityLogService {
    void addTaskLog(ActivityLog activityLog);
//    Object getAllLogs(String projectId);
}
