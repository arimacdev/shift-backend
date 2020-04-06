package com.arimac.backend.pmtool.projectmanagementtool.Service;

import com.arimac.backend.pmtool.projectmanagementtool.dtos.TaskDto;
import com.arimac.backend.pmtool.projectmanagementtool.dtos.TaskUpdateDto;

public interface NpTaskService {
    Object addPersonalTask(TaskDto taskDto);
    Object getAllPersonalTasks(String userId);
    Object updatePersonalTask(String userId, String taskId, TaskUpdateDto taskUpdateDto);
}
