package com.arimac.backend.pmtool.projectmanagementtool.Service;

import com.arimac.backend.pmtool.projectmanagementtool.dtos.Sprint.SprintUpdateDto;
import com.arimac.backend.pmtool.projectmanagementtool.dtos.Sprint.TaskSprintUpdateDto;
import com.arimac.backend.pmtool.projectmanagementtool.dtos.Task.TaskParentUpdateDto;
import com.arimac.backend.pmtool.projectmanagementtool.dtos.TaskDto;
import com.arimac.backend.pmtool.projectmanagementtool.dtos.TaskUpdateDto;
import com.arimac.backend.pmtool.projectmanagementtool.enumz.TaskTypeEnum;

public interface TaskService {
    Object addTaskToProject(String projectId, TaskDto taskDto);
    Object getAllProjectTasksByUser(String userId, String projectId);
    Object getAllUserAssignedTasks(String userId, String projectId);
    Object getProjectTask(String userId, String projectId, String taskId);
    Object getProjectTaskFiles(String userId, String projectId, String taskId);
    Object updateProjectTask(String userId, String projectId, String taskId, TaskUpdateDto taskUpdateDto);
    Object flagProjectTask(String userId, String projectId, String taskId);
//    Object getProjectTaskCompletionByUser(String userId, String projectId);
    Object getProjectTaskCompletionUserDetails(String userId, String projectId); // people tab
    Object getProjectTaskCompletion(String userId, String projectId); //project tab
    Object getAllUsersWithTaskCompletion(String userId);
    Object getAllUserAssignedTaskWithCompletion(String user, String userId, String from, String to);
    Object updateProjectTaskSprint(String userId, String projectId, String taskId, TaskSprintUpdateDto taskSprintUpdateDto);
    Object updateProjectTaskParent(String userId, String projectId, String taskId, TaskParentUpdateDto taskParentUpdateDto);
    Object getAllChildrenOfParentTask(String userId, String projectId, String taskId);

}
