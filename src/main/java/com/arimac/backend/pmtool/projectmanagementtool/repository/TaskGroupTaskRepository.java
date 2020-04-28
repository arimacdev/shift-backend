package com.arimac.backend.pmtool.projectmanagementtool.repository;

import com.arimac.backend.pmtool.projectmanagementtool.model.Task;
import com.arimac.backend.pmtool.projectmanagementtool.model.TaskGroupTask;
import org.springframework.dao.EmptyResultDataAccessException;

public interface TaskGroupTaskRepository {
    TaskGroupTask getTaskByTaskGroupId(String TaskGroupId, String taskId);
    void addTaskGroupTask(TaskGroupTask taskGroupTask);
}
