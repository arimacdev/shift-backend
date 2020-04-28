package com.arimac.backend.pmtool.projectmanagementtool.Service;

import com.arimac.backend.pmtool.projectmanagementtool.dtos.TaskGroupTask.TaskGroupTaskDto;
import com.arimac.backend.pmtool.projectmanagementtool.dtos.TaskGroupTask.TaskGroupTaskUpdateDto;

public interface TaskGroupTaskService {
    Object addTaskGroupTask(String taskGroupId, TaskGroupTaskDto taskGroupTaskDto);
    Object getTaskGroupTask(String userId, String taskGroupId, String taskId);
    Object updateTaskGroupTask(String userId, String taskGroupId, String taskId, TaskGroupTaskUpdateDto taskGroupTaskUpdateDto);
    Object flagTaskGroupTask(String userId, String taskGroupId, String taskId);
}
