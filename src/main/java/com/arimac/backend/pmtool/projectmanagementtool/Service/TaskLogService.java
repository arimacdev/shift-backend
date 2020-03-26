package com.arimac.backend.pmtool.projectmanagementtool.Service;

import com.arimac.backend.pmtool.projectmanagementtool.model.Task;
import com.arimac.backend.pmtool.projectmanagementtool.model.TaskLog;

public interface TaskLogService {
    Object addTaskLog(Task task);
    Object getAllLogs(String projectId);
}
