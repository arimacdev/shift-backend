package com.arimac.backend.pmtool.projectmanagementtool.Service;

import com.arimac.backend.pmtool.projectmanagementtool.dtos.TaskDto;

public interface NpTaskService {
    Object addPersonalTask(TaskDto taskDto);
    Object getAllPersonalTasks(String userId);
}
