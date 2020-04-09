package com.arimac.backend.pmtool.projectmanagementtool.Service;

import com.arimac.backend.pmtool.projectmanagementtool.dtos.TaskGroup.TaskGroupAddDto;
import com.arimac.backend.pmtool.projectmanagementtool.dtos.TaskGroup.TaskGroupDto;
import com.arimac.backend.pmtool.projectmanagementtool.dtos.TaskGroup.TaskGroupUpdateDto;

public interface TaskGroupService {
    Object createTaskGroup(TaskGroupDto taskGroup);
    Object getAllTaskGroupsByUser(String userId);
    Object addMembersToTaskGroup(TaskGroupAddDto taskGroupAddDto);
    Object updateTaskGroup(String taskGroupId, TaskGroupUpdateDto taskGroupUpdateDto);
    Object flagTaskGroup(String taskGroupId, String userId);
}
