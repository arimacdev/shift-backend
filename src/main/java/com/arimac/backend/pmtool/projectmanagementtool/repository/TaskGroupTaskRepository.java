package com.arimac.backend.pmtool.projectmanagementtool.repository;

import com.arimac.backend.pmtool.projectmanagementtool.dtos.TaskGroupTask.TaskGroupTaskUpdateDto;
import com.arimac.backend.pmtool.projectmanagementtool.model.TaskGroupTask;

public interface TaskGroupTaskRepository {
    TaskGroupTask getTaskByTaskGroupId(String TaskGroupId, String taskId);
    void addTaskGroupTask(TaskGroupTask taskGroupTask);
    TaskGroupTaskUpdateDto updateTaskGroupTask(String taskId, TaskGroupTaskUpdateDto taskGroupTaskUpdateDto);
    void flagTaskGroupTask(String taskId);
}
