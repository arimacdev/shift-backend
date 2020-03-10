package com.arimac.backend.pmtool.projectmanagementtool.repository;

import com.arimac.backend.pmtool.projectmanagementtool.model.Task;

public interface TaskRepository {
    Task addTaskToProject(Task task);
    Object getAllProjectTasksByUser(String userId, String projectId);
}
