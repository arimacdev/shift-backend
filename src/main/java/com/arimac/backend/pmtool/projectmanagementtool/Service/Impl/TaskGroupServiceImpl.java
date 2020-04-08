package com.arimac.backend.pmtool.projectmanagementtool.Service.Impl;

import com.arimac.backend.pmtool.projectmanagementtool.Response.Response;
import com.arimac.backend.pmtool.projectmanagementtool.Service.TaskGroupService;
import com.arimac.backend.pmtool.projectmanagementtool.dtos.TaskGroup.TaskGroupDto;
import com.arimac.backend.pmtool.projectmanagementtool.dtos.TaskGroup.TaskGroup_MemberResponseDto;
import com.arimac.backend.pmtool.projectmanagementtool.enumz.ResponseMessage;
import com.arimac.backend.pmtool.projectmanagementtool.enumz.TaskGroupRoleEnum;
import com.arimac.backend.pmtool.projectmanagementtool.exception.ErrorMessage;
import com.arimac.backend.pmtool.projectmanagementtool.model.TaskGroup;
import com.arimac.backend.pmtool.projectmanagementtool.model.TaskGroup_Member;
import com.arimac.backend.pmtool.projectmanagementtool.model.User;
import com.arimac.backend.pmtool.projectmanagementtool.repository.TaskGroupRepository;
import com.arimac.backend.pmtool.projectmanagementtool.repository.UserRepository;
import com.arimac.backend.pmtool.projectmanagementtool.utils.UtilsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TaskGroupServiceImpl implements TaskGroupService {
    private static final Logger logger = LoggerFactory.getLogger(TaskGroupServiceImpl.class);

    private final TaskGroupRepository taskGroupRepository;
    private final UserRepository userRepository;
//    private final
    private final UtilsService utilsService;

    public TaskGroupServiceImpl(TaskGroupRepository taskGroupRepository, UserRepository userRepository, UtilsService utilsService) {
        this.taskGroupRepository = taskGroupRepository;
        this.userRepository = userRepository;
        this.utilsService = utilsService;
    }

    @Override
    public Object createTaskGroup(TaskGroupDto taskGroupDto) {
        if (taskGroupDto.getTaskGroupName() == null || taskGroupDto.getTaskGroupCreator() == null)
            return new ErrorMessage(ResponseMessage.INVALID_REQUEST_BODY, HttpStatus.BAD_REQUEST);
        User creator = userRepository.getUserByUserId(taskGroupDto.getTaskGroupCreator());
        if (creator == null)
            return new ErrorMessage(ResponseMessage.NO_RECORD, HttpStatus.NOT_FOUND);
        TaskGroup taskGroup = new TaskGroup();
        taskGroup.setTaskGroupId(utilsService.getUUId());
        taskGroup.setTaskGroupName(taskGroupDto.getTaskGroupName());
        taskGroup.setTaskGroupCreatedAt(utilsService.getCurrentTimestamp());
        taskGroupRepository.createTaskGroup(taskGroup);
        //TODO if req fails
        TaskGroup_Member member = new TaskGroup_Member();
        member.setTaskGroupId(taskGroup.getTaskGroupId());
        member.setTaskGroupMemberId(taskGroupDto.getTaskGroupCreator());
        member.setTaskGroupRole(TaskGroupRoleEnum.owner.getRoleValue());
        member.setBlocked(false);
        member.setMemberAssignedAt(utilsService.getCurrentTimestamp());
        taskGroupRepository.assignMemberToTaskGroup(member);

        return new Response(ResponseMessage.SUCCESS, HttpStatus.OK, taskGroup);
    }

    @Override
    public Object getAllTaskGroupsByUser(String userId) {
        User user = userRepository.getUserByUserId(userId);
        if (user == null)
            return new ErrorMessage(ResponseMessage.NO_RECORD, HttpStatus.NOT_FOUND);
        List<TaskGroup_MemberResponseDto> taskGroups = taskGroupRepository.getAllTaskGroupsWithGroup(userId);
        return new Response(ResponseMessage.SUCCESS, HttpStatus.OK, taskGroups);
    }
}
