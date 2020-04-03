package com.arimac.backend.pmtool.projectmanagementtool.repository;

import com.arimac.backend.pmtool.projectmanagementtool.dtos.TaskAlertDto;
import com.arimac.backend.pmtool.projectmanagementtool.dtos.TaskUpdateDto;
import com.arimac.backend.pmtool.projectmanagementtool.dtos.TaskUserResponseDto;
import com.arimac.backend.pmtool.projectmanagementtool.dtos.WorkLoadTaskStatusDto;
import com.arimac.backend.pmtool.projectmanagementtool.model.Task;

import java.sql.Timestamp;
import java.util.List;

public interface TaskRepository {
    Task addTaskToProject(Task task);
    List<Task> getAllProjectTasksByUser(String projectId);
    List<TaskUserResponseDto> getAllProjectTasksWithProfile(String projectId);
    List<Task> getAllUserAssignedTasks(String userId, String projectId);
    List<TaskUserResponseDto> getAllUserAssignedTasksWithProfile(String userId, String projectId);
    Task getProjectTask(String taskId);
    Task getProjectTaskWithDeleted(String taskId);
    Object updateProjectTask(String taskId,TaskUpdateDto taskUpdateDto);
    void flagProjectTask(String taskId);
    void flagProjectBoundTasks(String projectId);
//    List<TaskAlertDto> getTaskAlertList();
    List<WorkLoadTaskStatusDto> getAllUsersWithTaskCompletion();
    List<WorkLoadTaskStatusDto> getAllUserAssignedTaskWithCompletion(String userId, String from, String to);

}
