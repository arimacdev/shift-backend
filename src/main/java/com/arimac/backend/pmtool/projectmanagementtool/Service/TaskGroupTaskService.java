package com.arimac.backend.pmtool.projectmanagementtool.Service;

import com.arimac.backend.pmtool.projectmanagementtool.dtos.TaskGroupTask.TaskGroupTaskDto;

public interface TaskGroupTaskService {
    Object addTaskGroupTask(String taskGroupId, TaskGroupTaskDto taskGroupTaskDto);
}
