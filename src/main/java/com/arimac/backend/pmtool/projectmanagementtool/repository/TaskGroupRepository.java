package com.arimac.backend.pmtool.projectmanagementtool.repository;

import com.arimac.backend.pmtool.projectmanagementtool.dtos.TaskGroup.TaskGroup_MemberResponseDto;
import com.arimac.backend.pmtool.projectmanagementtool.model.TaskGroup;
import com.arimac.backend.pmtool.projectmanagementtool.model.TaskGroup_Member;

import java.util.List;

public interface TaskGroupRepository {
    Object createTaskGroup(TaskGroup taskGroup);
    Object assignMemberToTaskGroup(TaskGroup_Member assignment);
    Object getAllTaskGroupsByUser(String userId); // only member
    List<TaskGroup_MemberResponseDto> getAllTaskGroupsWithGroup(String userId); // taskgroup + member
}
