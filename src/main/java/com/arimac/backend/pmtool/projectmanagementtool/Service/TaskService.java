package com.arimac.backend.pmtool.projectmanagementtool.Service;

import com.arimac.backend.pmtool.projectmanagementtool.dtos.TaskDto;
import com.arimac.backend.pmtool.projectmanagementtool.model.Task;

public interface TaskService {
    Task addTaskToProject(String projectId, TaskDto taskDto);
}
