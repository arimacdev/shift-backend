package com.arimac.backend.pmtool.projectmanagementtool.repository;

import com.arimac.backend.pmtool.projectmanagementtool.model.TaskGroup;
import com.arimac.backend.pmtool.projectmanagementtool.model.TaskGroup_Member;

public interface TaskGroupRepository {
    Object createTaskGroup(TaskGroup taskGroup);
    Object assignMemberToTaskGroup(TaskGroup_Member assignment);
}
