package com.arimac.backend.pmtool.projectmanagementtool.Service.Impl;

import com.arimac.backend.pmtool.projectmanagementtool.Response.Response;
import com.arimac.backend.pmtool.projectmanagementtool.Service.TaskGroupService;
import com.arimac.backend.pmtool.projectmanagementtool.dtos.TaskGroup.TaskGroupAddDto;
import com.arimac.backend.pmtool.projectmanagementtool.dtos.TaskGroup.TaskGroupDto;
import com.arimac.backend.pmtool.projectmanagementtool.dtos.TaskGroup.TaskGroupUpdateDto;
import com.arimac.backend.pmtool.projectmanagementtool.dtos.TaskGroup.TaskGroup_MemberResponseDto;
import com.arimac.backend.pmtool.projectmanagementtool.enumz.ResponseMessage;
import com.arimac.backend.pmtool.projectmanagementtool.enumz.TaskGroupRoleEnum;
import com.arimac.backend.pmtool.projectmanagementtool.exception.ErrorMessage;
import com.arimac.backend.pmtool.projectmanagementtool.model.Task;
import com.arimac.backend.pmtool.projectmanagementtool.model.TaskGroup;
import com.arimac.backend.pmtool.projectmanagementtool.model.TaskGroup_Member;
import com.arimac.backend.pmtool.projectmanagementtool.model.User;
import com.arimac.backend.pmtool.projectmanagementtool.repository.SubTaskRepository;
import com.arimac.backend.pmtool.projectmanagementtool.repository.TaskGroupRepository;
import com.arimac.backend.pmtool.projectmanagementtool.repository.TaskRepository;
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

//    private final TaskService taskService;
    private final TaskGroupRepository taskGroupRepository;
    private final UserRepository userRepository;
    private final TaskRepository taskRepository;
    private final SubTaskRepository subTaskRepository;
    private final UtilsService utilsService;

    public TaskGroupServiceImpl(TaskGroupRepository taskGroupRepository, UserRepository userRepository, TaskRepository taskRepository, SubTaskRepository subTaskRepository, UtilsService utilsService) {
        this.taskGroupRepository = taskGroupRepository;
        this.userRepository = userRepository;
        this.taskRepository = taskRepository;
        this.subTaskRepository = subTaskRepository;
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

    @Override
    public Object addMembersToTaskGroup(TaskGroupAddDto taskGroupAddDto) {
        TaskGroup_Member owner = taskGroupRepository.getTaskGroupMemberByTaskGroup(taskGroupAddDto.getTaskGroupAssigner(), taskGroupAddDto.getTaskGroupId());
        if (owner == null)
            return new ErrorMessage("Assigner doesnot belong to the Task Group", HttpStatus.BAD_REQUEST);
        if (owner.getTaskGroupRole() != TaskGroupRoleEnum.owner.getRoleValue())
            return new ErrorMessage("Assigner is not Group Admin", HttpStatus.UNAUTHORIZED);
        TaskGroup_Member assignee = taskGroupRepository.getTaskGroupMemberByTaskGroup(taskGroupAddDto.getTaskGroupAssignee(), taskGroupAddDto.getTaskGroupId());
        if (assignee != null)
            return new ErrorMessage("Already a member", HttpStatus.BAD_REQUEST);
        User newUser = userRepository.getUserByUserId(taskGroupAddDto.getTaskGroupAssignee());
        if (newUser == null)
            return new ErrorMessage(ResponseMessage.NO_RECORD, HttpStatus.NOT_FOUND);
        TaskGroup_Member member = new TaskGroup_Member();
        member.setTaskGroupId(taskGroupAddDto.getTaskGroupId());
        member.setTaskGroupMemberId(taskGroupAddDto.getTaskGroupAssignee());
        member.setTaskGroupRole(TaskGroupRoleEnum.member.getRoleValue());
        member.setBlocked(false);
        member.setMemberAssignedAt(utilsService.getCurrentTimestamp());
        taskGroupRepository.assignMemberToTaskGroup(member);
        return new Response(ResponseMessage.SUCCESS, HttpStatus.OK, member);
    }

    @Override
    public Object updateTaskGroup(String taskGroupId, TaskGroupUpdateDto taskGroupUpdateDto) {
        if (taskGroupUpdateDto.getTaskGroupName() == null || taskGroupUpdateDto.getTaskGroupEditor() == null)
            return new ErrorMessage(ResponseMessage.INVALID_REQUEST_BODY, HttpStatus.BAD_REQUEST);
        TaskGroup_Member owner = taskGroupRepository.getTaskGroupMemberByTaskGroup(taskGroupUpdateDto.getTaskGroupEditor(), taskGroupId);
        if (owner == null)
            return new ErrorMessage("User is not the Group Member", HttpStatus.BAD_REQUEST);
        if (owner.getTaskGroupRole() != TaskGroupRoleEnum.owner.getRoleValue())
            return new ErrorMessage("User is not the Group Owner", HttpStatus.UNAUTHORIZED);
        taskGroupRepository.updateTaskGroup(taskGroupId,taskGroupUpdateDto);
        return new Response(ResponseMessage.SUCCESS, HttpStatus.OK, taskGroupUpdateDto);
    }



    @Override
    public Object flagTaskGroup(String taskGroupId, String userId) {
        TaskGroup_Member owner = taskGroupRepository.getTaskGroupMemberByTaskGroup(userId, taskGroupId);
        if (owner == null)
            return new ErrorMessage("User is not the Group Member", HttpStatus.BAD_REQUEST);
        if (owner.getTaskGroupRole() != TaskGroupRoleEnum.owner.getRoleValue())
            return new ErrorMessage("User is not the Group Owner", HttpStatus.UNAUTHORIZED);
        taskGroupRepository.flagTaskGroup(taskGroupId);
        TaskGroup taskGroup = taskGroupRepository.getTaskGroupById(taskGroupId);
        if (taskGroup == null)
            return new ErrorMessage(ResponseMessage.NO_RECORD, HttpStatus.NOT_FOUND);
        List<Task> taskList = taskRepository.getAllProjectTasksByUser(taskGroupId);
        for(Task task : taskList) {
        taskRepository.flagProjectTask(task.getTaskId());
        subTaskRepository.flagTaskBoundSubTasks(task.getTaskId());
        }
        return new Response(ResponseMessage.SUCCESS, HttpStatus.OK);
    }
}
