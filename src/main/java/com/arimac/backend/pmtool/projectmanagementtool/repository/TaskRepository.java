package com.arimac.backend.pmtool.projectmanagementtool.repository;

import com.arimac.backend.pmtool.projectmanagementtool.dtos.TaskUpdateDto;
import com.arimac.backend.pmtool.projectmanagementtool.model.Task;

import java.util.List;

public interface TaskRepository {
    Task addTaskToProject(Task task);
    List<Task> getAllProjectTasksByUser(String projectId);
    List<Task> getAllUserAssignedTasks(String userId, String projectId);
    Task getProjectTask(String taskId);
    Object updateProjectTask(String taskId,TaskUpdateDto taskUpdateDto);
    void flagProjectTask(String taskId);
    void flagProjectBoundTasks(String projectId);

}
