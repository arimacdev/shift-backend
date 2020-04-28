package com.arimac.backend.pmtool.projectmanagementtool.Service.Impl;

import com.arimac.backend.pmtool.projectmanagementtool.Response.Response;
import com.arimac.backend.pmtool.projectmanagementtool.Service.TaskGroupTaskService;
import com.arimac.backend.pmtool.projectmanagementtool.dtos.TaskGroupTask.TaskGroupTaskDto;
import com.arimac.backend.pmtool.projectmanagementtool.dtos.TaskGroupTask.TaskGroupTaskUpdateDto;
import com.arimac.backend.pmtool.projectmanagementtool.enumz.*;
import com.arimac.backend.pmtool.projectmanagementtool.exception.ErrorMessage;
import com.arimac.backend.pmtool.projectmanagementtool.model.TaskFile;
import com.arimac.backend.pmtool.projectmanagementtool.model.TaskGroupTask;
import com.arimac.backend.pmtool.projectmanagementtool.model.TaskGroup_Member;
import com.arimac.backend.pmtool.projectmanagementtool.repository.TaskFileRepository;
import com.arimac.backend.pmtool.projectmanagementtool.repository.TaskGroupRepository;
import com.arimac.backend.pmtool.projectmanagementtool.repository.TaskGroupTaskRepository;
import com.arimac.backend.pmtool.projectmanagementtool.utils.UtilsService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TaskGroupTaskServiceImpl implements TaskGroupTaskService {

    private final TaskGroupRepository taskGroupRepository;
    private final TaskGroupTaskRepository taskGroupTaskRepository;
    private final TaskFileRepository taskFileRepository;
    private final UtilsService utilsService;

    public TaskGroupTaskServiceImpl(TaskGroupRepository taskGroupRepository, TaskGroupTaskRepository taskGroupTaskRepository, TaskFileRepository taskFileRepository, UtilsService utilsService) {
        this.taskGroupRepository = taskGroupRepository;
        this.taskGroupTaskRepository = taskGroupTaskRepository;
        this.taskFileRepository = taskFileRepository;
        this.utilsService = utilsService;
    }


    @Override
    public Object addTaskGroupTask(String taskGroupId, TaskGroupTaskDto taskDto) {
        if ( (taskDto.getTaskName() == null || taskDto.getTaskName().isEmpty()) || (taskDto.getTaskGroupId() == null || taskDto.getTaskGroupId().isEmpty()) || (taskDto.getTaskInitiator()== null || taskDto.getTaskInitiator().isEmpty()) )
            return new ErrorMessage(ResponseMessage.INVALID_REQUEST_BODY, HttpStatus.BAD_REQUEST);
        TaskGroup_Member member = taskGroupRepository.getTaskGroupMemberByTaskGroup(taskDto.getTaskInitiator(), taskDto.getTaskGroupId());
        if (member == null)
            return new ErrorMessage(ResponseMessage.USER_NOT_GROUP_MEMBER, HttpStatus.NOT_FOUND);
        if (taskDto.getTaskAssignee() != null)
            if (taskGroupRepository.getTaskGroupMemberByTaskGroup(taskDto.getTaskAssignee(), taskDto.getTaskGroupId()) == null)
                return new ErrorMessage(ResponseMessage.USER_NOT_GROUP_MEMBER, HttpStatus.NOT_FOUND);
        TaskGroupTask task = new TaskGroupTask();
        if((taskDto.getParentTaskId() != null) && !(taskDto.getParentTaskId().isEmpty())){
            TaskGroupTask parentTask = taskGroupTaskRepository.getTaskByTaskGroupId(taskGroupId, taskDto.getParentTaskId());
            if (parentTask == null)
                return new ErrorMessage("No Such Parent Task", HttpStatus.NOT_FOUND);
            if (!parentTask.getIsParent())
                return new ErrorMessage("Task is not a Parent Task", HttpStatus.BAD_REQUEST);
            task.setIsParent(false);
            task.setParentId(taskDto.getParentTaskId());
        } else {
            task.setIsParent(true);
        }
        task.setTaskId(utilsService.getUUId());
        task.setTaskGroupId(taskDto.getTaskGroupId());
        task.setTaskName(taskDto.getTaskName());
        task.setTaskInitiator(taskDto.getTaskInitiator());
        if (taskDto.getTaskAssignee() == null || taskDto.getTaskAssignee().isEmpty()){
            task.setTaskAssignee(taskDto.getTaskInitiator());
        } else {
            task.setTaskAssignee(taskDto.getTaskAssignee());
        }
        task.setTaskNote(taskDto.getTaskNotes());
        if (taskDto.getTaskStatus() == null){
            task.setTaskStatus(TaskGroupTaskStatusEnum.open);
        } else {
            task.setTaskStatus(taskDto.getTaskStatus());
        }
        task.setTaskCreatedAt(utilsService.getCurrentTimestamp());
        if (taskDto.getTaskDueDate() != null){
            task.setTaskDueDateAt(taskDto.getTaskDueDate());
        }
        if (taskDto.getTaskRemindOnDate() != null){
            task.setTaskReminderAt(task.getTaskReminderAt());
        }
        task.setTaskReminderAt(taskDto.getTaskRemindOnDate());
        task.setIsDeleted(false);
        taskGroupTaskRepository.addTaskGroupTask(task);

        return new Response(ResponseMessage.SUCCESS, HttpStatus.OK, task);
    }

    @Override
    public Object getTaskGroupTask(String userId, String taskGroupId, String taskId) {
        TaskGroup_Member member = taskGroupRepository.getTaskGroupMemberByTaskGroup(userId, taskGroupId);
        if (member == null)
            return new ErrorMessage(ResponseMessage.USER_NOT_GROUP_MEMBER, HttpStatus.NOT_FOUND);
        TaskGroupTask task = taskGroupTaskRepository.getTaskByTaskGroupId(taskGroupId, taskId);
        if (task == null)
            return new ErrorMessage(ResponseMessage.NO_RECORD, HttpStatus.NOT_FOUND);
        return new Response(ResponseMessage.SUCCESS, HttpStatus.OK, task);
    }

    @Override
    public Object updateTaskGroupTask(String userId, String taskGroupId, String taskId, TaskGroupTaskUpdateDto taskUpdateDto) {
        TaskGroup_Member member = taskGroupRepository.getTaskGroupMemberByTaskGroup(userId, taskGroupId);
        if (member == null)
            return new ErrorMessage(ResponseMessage.USER_NOT_GROUP_MEMBER, HttpStatus.UNAUTHORIZED);
        TaskGroupTask task = taskGroupTaskRepository.getTaskByTaskGroupId(taskGroupId, taskId);
        if (task == null)
            return new ErrorMessage(ResponseMessage.NO_RECORD, HttpStatus.NOT_FOUND);
        if (taskUpdateDto.getTaskAssignee() != null){
            TaskGroup_Member assignee = taskGroupRepository.getTaskGroupMemberByTaskGroup(taskUpdateDto.getTaskAssignee(), taskGroupId);
            if (assignee == null){
                return new ErrorMessage(ResponseMessage.USER_NOT_GROUP_MEMBER, HttpStatus.NOT_FOUND);
            }
        }
        TaskGroupTaskUpdateDto updateDto = new TaskGroupTaskUpdateDto();

        if (taskUpdateDto.getTaskName() == null || taskUpdateDto.getTaskName().isEmpty()) {
            updateDto.setTaskName(task.getTaskName());
        } else {
            updateDto.setTaskName(taskUpdateDto.getTaskName());
        }
        if (taskUpdateDto.getTaskAssignee() == null || taskUpdateDto.getTaskAssignee().isEmpty()) {
            updateDto.setTaskAssignee(task.getTaskAssignee());
        } else {
            updateDto.setTaskAssignee(taskUpdateDto.getTaskAssignee());
        }
        if (taskUpdateDto.getTaskNotes() == null || taskUpdateDto.getTaskNotes().isEmpty()){
            updateDto.setTaskNotes(task.getTaskNote());
        } else {
            updateDto.setTaskNotes(taskUpdateDto.getTaskNotes());
        }
        if (taskUpdateDto.getTaskStatus() == null) {
            updateDto.setTaskStatus(task.getTaskStatus());
        } else {
            updateDto.setTaskStatus(taskUpdateDto.getTaskStatus());
        }
        if (taskUpdateDto.getTaskDueDate() == null) {
            updateDto.setTaskDueDate(task.getTaskDueDateAt());
        } else {
            updateDto.setTaskDueDate(taskUpdateDto.getTaskDueDate());
        }
        if (taskUpdateDto.getTaskRemindOnDate() == null) {
            updateDto.setTaskRemindOnDate(task.getTaskReminderAt());
        } else {
            updateDto.setTaskRemindOnDate(taskUpdateDto.getTaskRemindOnDate());
        }
        Object updateTask = taskGroupTaskRepository.updateTaskGroupTask(taskId, updateDto);
        return new Response(ResponseMessage.SUCCESS, HttpStatus.OK, updateTask);
    }

    @Override
    public Object flagTaskGroupTask(String userId, String taskGroupId, String taskId) {
        TaskGroupTask task = taskGroupTaskRepository.getTaskByTaskGroupId(taskGroupId, taskId);
        if (task == null)
            return new ErrorMessage(ResponseMessage.NO_RECORD, HttpStatus.NOT_FOUND);
            TaskGroup_Member member = taskGroupRepository.getTaskGroupMemberByTaskGroup(userId, taskGroupId);
            if (member == null)
                return new ErrorMessage(ResponseMessage.USER_NOT_GROUP_MEMBER, HttpStatus.UNAUTHORIZED);
            if (member.getTaskGroupRole() != TaskGroupRoleEnum.owner.getRoleValue())
                return new ErrorMessage(ResponseMessage.UNAUTHORIZED_OPERATION, HttpStatus.UNAUTHORIZED);
        taskGroupTaskRepository.flagTaskGroupTask(taskId);
        //subTaskRepository.flagTaskBoundSubTasks(taskId); //TODO Flag Child Tasks
        List<TaskFile> taskFileList = taskFileRepository.getAllTaskFiles(taskId);
        for (TaskFile taskFile: taskFileList) {
            taskFileRepository.flagTaskFile(taskFile.getTaskFileId());
        }
        return new Response(ResponseMessage.SUCCESS, HttpStatus.OK);
    }
}
