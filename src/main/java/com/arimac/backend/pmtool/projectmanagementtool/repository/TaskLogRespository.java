package com.arimac.backend.pmtool.projectmanagementtool.repository;

import com.arimac.backend.pmtool.projectmanagementtool.dtos.TaskLogUser;
import com.arimac.backend.pmtool.projectmanagementtool.model.TaskLog;

import java.util.List;

public interface TaskLogRespository {
    void addTaskLog(TaskLog taskLog);
    List<TaskLogUser> getAllLogs(String projectId);
}
