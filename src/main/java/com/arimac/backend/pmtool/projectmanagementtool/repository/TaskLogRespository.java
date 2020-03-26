package com.arimac.backend.pmtool.projectmanagementtool.repository;

import com.arimac.backend.pmtool.projectmanagementtool.model.TaskLog;

import java.util.List;

public interface TaskLogRespository {
    void addTaskLog(TaskLog taskLog);
    List<TaskLog> getAllLogs(String projectId);
}
