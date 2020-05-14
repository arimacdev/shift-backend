package com.arimac.backend.pmtool.projectmanagementtool.repository;

import com.arimac.backend.pmtool.projectmanagementtool.dtos.Task.TaskParentChildUpdateDto;
import com.arimac.backend.pmtool.projectmanagementtool.dtos.TaskGroupTask.TaskGroupTaskUpdateDto;
import com.arimac.backend.pmtool.projectmanagementtool.dtos.TaskGroupTask.TaskGroupTaskUserResponseDto;
import com.arimac.backend.pmtool.projectmanagementtool.model.TaskGroupTask;

import java.util.List;

public interface TaskGroupTaskRepository {
    TaskGroupTask getTaskByTaskGroupId(String TaskGroupId, String taskId);
    List<TaskGroupTask> getAllTaskGroupTasksByUser(String TaskGroupId);
    void addTaskGroupTask(TaskGroupTask taskGroupTask);
    TaskGroupTaskUpdateDto updateTaskGroupTask(String taskId, TaskGroupTaskUpdateDto taskGroupTaskUpdateDto);
    void flagTaskGroupTask(String taskId);
    List<TaskGroupTaskUserResponseDto> getAllParentTasksWithProfile(String taskGroupId);
    List<TaskGroupTaskUserResponseDto> getAllChildTasksWithProfile(String taskGroupId);
    List<TaskGroupTaskUserResponseDto> getAllUserAssignedTasksWithProfile(String userId, String taskGroupId);
    boolean checkChildTasksOfAParentTask(String taskId);
    void transitionFromParentToChild(String taskId, TaskParentChildUpdateDto taskParentChildUpdateDto);
    List<TaskGroupTask> getAllChildrenOfParentTask(String taskId);
    List<TaskGroupTaskUserResponseDto> getAllChildrenOfParentTaskWithProfile(String taskId);


}
