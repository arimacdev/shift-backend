package com.arimac.backend.pmtool.projectmanagementtool.Service;

import com.arimac.backend.pmtool.projectmanagementtool.dtos.Task.TaskParentChildUpdateDto;
import com.arimac.backend.pmtool.projectmanagementtool.dtos.TaskGroupTask.TaskGroupTaskDto;
import com.arimac.backend.pmtool.projectmanagementtool.dtos.TaskGroupTask.TaskGroupTaskUpdateDto;

public interface TaskGroupTaskService {
    Object addTaskGroupTask(String taskGroupId, TaskGroupTaskDto taskGroupTaskDto);
    Object getTaskGroupTask(String userId, String taskGroupId, String taskId);
    Object updateTaskGroupTask(String userId, String taskGroupId, String taskId, TaskGroupTaskUpdateDto taskGroupTaskUpdateDto);
    Object flagTaskGroupTask(String userId, String taskGroupId, String taskId);
    Object getAllTaskGroupTasksByUser(String userId, String taskGroupId);
    Object getTaskCompletionUserDetails(String userId, String taskGroupId);
    Object getAllUserAssignedTasks(String userId, String taskGroupId);
    Object getProjectTaskFiles(String userId, String taskGroupId, String taskId);
    Object transitionFromParentToChild(String userId, String taskGroupId, String taskId, TaskParentChildUpdateDto taskParentChildUpdateDto);
    Object getAllChildrenOfParentTask(String userId, String taskGroupId, String taskId);
}
