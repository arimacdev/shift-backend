package com.arimac.backend.pmtool.projectmanagementtool.repository;

import com.arimac.backend.pmtool.projectmanagementtool.model.Task;

import java.util.List;

public interface TaskRepository {
    Task addTaskToProject(Task task);
    List<Task> getAllProjectTasksByUser(String projectId);
    List<Task> getAllUserAssignedTasks(String userId, String projectId);
    Task getProjectTask(String taskId);
}
