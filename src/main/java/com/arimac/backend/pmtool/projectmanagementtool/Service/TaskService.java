package com.arimac.backend.pmtool.projectmanagementtool.Service;

import com.arimac.backend.pmtool.projectmanagementtool.dtos.TaskDto;
import com.arimac.backend.pmtool.projectmanagementtool.dtos.TaskUpdateDto;

public interface TaskService {
    Object addTaskToProject(String projectId, TaskDto taskDto);
    Object getAllProjectTasksByUser(String userId, String projectId);
    Object getAllUserAssignedTasks(String userId, String projectId);
    Object getProjectTask(String userId, String projectId, String taskId);
    Object getProjectTaskFiles(String userId, String projectId, String taskId);
    Object updateProjectTask(String userId, String projectId, String taskId, TaskUpdateDto taskUpdateDto);
    Object flagProjectTask(String userId, String projectId, String taskId);
    Object getProjectTaskCompletionByUser(String userId, String projectId);
    Object getProjectTaskCompletionUserDetails(String userId, String projectId);
    Object getProjectTaskCompletion(String userId, String projectId);
    Object getAllUsersWithTaskCompletion(String userId);
    Object getAllUserAssignedTaskWithCompletion(String user, String userId, String from, String to);
    @Deprecated
    Object getAllProjectsWithCompletion(String user, String userId);

}
