package com.arimac.backend.pmtool.projectmanagementtool.Service;

import com.arimac.backend.pmtool.projectmanagementtool.dtos.SubTaskDto;
import com.arimac.backend.pmtool.projectmanagementtool.dtos.SubTaskUpdateDto;
import com.arimac.backend.pmtool.projectmanagementtool.dtos.TaskUpdateDto;

public interface SubTaskService {
    Object addSubTaskToProject(String projectId, String taskId, SubTaskDto subTaskDto);
    Object getAllSubTaksOfATask(String userId, String projectId, String taskId);
    Object updateSubTaskOfATask(String userId, String projectId, String taskId, String subtaskId, SubTaskUpdateDto subTaskUpdateDto);
}
