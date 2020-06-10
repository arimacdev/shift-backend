package com.arimac.backend.pmtool.projectmanagementtool.Service;

import com.arimac.backend.pmtool.projectmanagementtool.model.ActivityLog;
import com.arimac.backend.pmtool.projectmanagementtool.model.Task;
import com.arimac.backend.pmtool.projectmanagementtool.model.TaskLog;

import java.util.List;

public interface ActivityLogService {
    void addTaskLog(ActivityLog activityLog);
    Object getTaskActivity(String userId, String taskId, int startIndex, int endIndex);
    Object getAllProjectLogsWithTasks(String userId, String projectId, int startIndex, int endIndex);
    void flagTaskLogs(String taskId);
}
