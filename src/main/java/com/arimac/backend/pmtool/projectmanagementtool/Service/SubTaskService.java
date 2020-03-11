package com.arimac.backend.pmtool.projectmanagementtool.Service;

import com.arimac.backend.pmtool.projectmanagementtool.dtos.SubTaskDto;

public interface SubTaskService {
    Object addSubTaskToProject(String projectId, String taskId, SubTaskDto subTaskDto);
}
