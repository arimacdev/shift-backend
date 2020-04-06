package com.arimac.backend.pmtool.projectmanagementtool.Service;

import com.arimac.backend.pmtool.projectmanagementtool.dtos.SubTaskDto;
import com.arimac.backend.pmtool.projectmanagementtool.dtos.SubTaskUpdateDto;
import com.arimac.backend.pmtool.projectmanagementtool.dtos.TaskDto;
import com.arimac.backend.pmtool.projectmanagementtool.dtos.TaskUpdateDto;

public interface NpTaskService {
    Object addPersonalTask(TaskDto taskDto);
    Object getAllPersonalTasks(String userId);
    Object updatePersonalTask(String userId, String taskId, TaskUpdateDto taskUpdateDto);
    Object addSubTaskToPersonalTask(String taskId, SubTaskDto subTaskDto);
    Object getAllSubTaksOfATask(String userId, String taskId);
    Object updateSubTaskOfATask(String userId, String taskId, String subTaskId, SubTaskUpdateDto subTaskUpdateDto);
}
