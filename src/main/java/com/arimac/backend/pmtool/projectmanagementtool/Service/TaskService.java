package com.arimac.backend.pmtool.projectmanagementtool.Service;

import com.arimac.backend.pmtool.projectmanagementtool.dtos.TaskDto;
import com.arimac.backend.pmtool.projectmanagementtool.dtos.TaskUpdateDto;
import com.arimac.backend.pmtool.projectmanagementtool.model.Task;

public interface TaskService {
    Object addTaskToProject(String projectId, TaskDto taskDto);
    Object getAllProjectTasksByUser(String userId, String projectId);
    Object getAllUserAssignedTasks(String userId, String projectId);
    Object getProjectTask(String userId, String projectId, String taskId);
    Object updateProjectTask(String userId, String projectId, String taskId, TaskUpdateDto taskUpdateDto);
    Object deleteProjectTask(String userId, String projectId, String taskId);
}
