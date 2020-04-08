package com.arimac.backend.pmtool.projectmanagementtool.Service;

import com.arimac.backend.pmtool.projectmanagementtool.dtos.TaskGroup.TaskGroupAddDto;
import com.arimac.backend.pmtool.projectmanagementtool.dtos.TaskGroup.TaskGroupDto;

public interface TaskGroupService {
    Object createTaskGroup(TaskGroupDto taskGroup);
    Object getAllTaskGroupsByUser(String userId);
    Object addMembersToTaskGroup(TaskGroupAddDto taskGroupAddDto);
}
