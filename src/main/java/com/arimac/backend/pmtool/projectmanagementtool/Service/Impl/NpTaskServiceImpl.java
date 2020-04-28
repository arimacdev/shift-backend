package com.arimac.backend.pmtool.projectmanagementtool.Service.Impl;

import com.arimac.backend.pmtool.projectmanagementtool.Response.Response;
import com.arimac.backend.pmtool.projectmanagementtool.Service.NpTaskService;
import com.arimac.backend.pmtool.projectmanagementtool.dtos.*;
import com.arimac.backend.pmtool.projectmanagementtool.dtos.PersonalTask.PersonalTask;
import com.arimac.backend.pmtool.projectmanagementtool.dtos.PersonalTask.PersonalTaskDto;
import com.arimac.backend.pmtool.projectmanagementtool.dtos.PersonalTask.PersonalTaskUpdateDto;
import com.arimac.backend.pmtool.projectmanagementtool.enumz.*;
import com.arimac.backend.pmtool.projectmanagementtool.exception.ErrorMessage;
import com.arimac.backend.pmtool.projectmanagementtool.model.SubTask;
import com.arimac.backend.pmtool.projectmanagementtool.model.Task;
import com.arimac.backend.pmtool.projectmanagementtool.model.User;
import com.arimac.backend.pmtool.projectmanagementtool.repository.*;
import com.arimac.backend.pmtool.projectmanagementtool.utils.UtilsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NpTaskServiceImpl implements NpTaskService {

    private static final Logger logger = LoggerFactory.getLogger(NpTaskServiceImpl.class);

    private final UtilsService utilsService;
    private final PersonalTaskRepository personalTaskRepository;
    private final TaskRepository taskRepository;
    private final UserRepository userRepository;
    private final SubTaskRepository subTaskRepository;
    private final TaskFileRepository taskFileRepository;


    public NpTaskServiceImpl(UtilsService utilsService, PersonalTaskRepository personalTaskRepository, TaskRepository taskRepository, UserRepository userRepository, SubTaskRepository subTaskRepository, TaskFileRepository taskFileRepository) {
        this.utilsService = utilsService;
        this.personalTaskRepository = personalTaskRepository;
        this.taskRepository = taskRepository;
        this.userRepository = userRepository;
        this.subTaskRepository = subTaskRepository;
        this.taskFileRepository = taskFileRepository;
    }


    @Override
    public Object addPersonalTask(PersonalTaskDto taskDto) {
        if ( (taskDto.getTaskName() == null || taskDto.getTaskName().isEmpty()) ||  (taskDto.getTaskAssignee()== null || taskDto.getTaskAssignee().isEmpty()) )
            return new ErrorMessage(ResponseMessage.INVALID_REQUEST_BODY, HttpStatus.BAD_REQUEST);
        User user = userRepository.getUserByUserId(taskDto.getTaskAssignee());
        if (user == null)
            return new ErrorMessage(ResponseMessage.USER_NOT_FOUND, HttpStatus.NOT_FOUND);
        PersonalTask personalTask = new PersonalTask();
        personalTask.setTaskId(utilsService.getUUId());
        personalTask.setTaskName(taskDto.getTaskName());
        personalTask.setTaskAssignee(taskDto.getTaskAssignee());
        personalTask.setTaskNote(taskDto.getTaskNotes());
        personalTask.setTaskStatus(PersonalTaskEnum.open);
        personalTask.setTaskCreatedAt(utilsService.getCurrentTimestamp());
        if (taskDto.getTaskDueDate() != null){
            personalTask.setTaskDueDateAt(taskDto.getTaskDueDate());
        }
        if (taskDto.getTaskRemindOnDate() != null){
            personalTask.setTaskReminderAt(personalTask.getTaskReminderAt());
        }
        personalTask.setIsDeleted(false);
        personalTaskRepository.addPersonalTask(personalTask);
        return new Response(ResponseMessage.SUCCESS, HttpStatus.OK);
    }

    @Override
    public Object getAllPersonalTasks(String userId) {
        User user = userRepository.getUserByUserId(userId);
        if (user == null)
            return new ErrorMessage(ResponseMessage.USER_NOT_FOUND, HttpStatus.NOT_FOUND);
        List<PersonalTask> personalTasks = personalTaskRepository.getAllPersonalTasks(userId);
        return new Response(ResponseMessage.SUCCESS, HttpStatus.OK, personalTasks);
    }

    @Override
    public Object updatePersonalTask(String userId, String taskId, PersonalTaskUpdateDto taskUpdateDto) {
        PersonalTask task = personalTaskRepository.getPersonalTaskByUserId(userId, taskId);
        if (task == null)
            return new ErrorMessage(ResponseMessage.NO_RECORD, HttpStatus.NOT_FOUND);
        User user = userRepository.getUserByUserId(userId);
        if (user == null)
            return new ErrorMessage(ResponseMessage.USER_NOT_FOUND, HttpStatus.NOT_FOUND);
        if (taskUpdateDto.getTaskName() == null || taskUpdateDto.getTaskName().isEmpty())
            taskUpdateDto.setTaskName(task.getTaskName());
        if (taskUpdateDto.getTaskNotes() == null || taskUpdateDto.getTaskNotes().isEmpty())
            taskUpdateDto.setTaskNotes(task.getTaskNote());
        if (taskUpdateDto.getTaskStatus() == null)
            taskUpdateDto.setTaskStatus(task.getTaskStatus());
        if (taskUpdateDto.getTaskDueDate() == null)
            taskUpdateDto.setTaskDueDate(task.getTaskDueDateAt());
        if (taskUpdateDto.getTaskRemindOnDate() == null)
            taskUpdateDto.setTaskRemindOnDate(task.getTaskReminderAt());
       personalTaskRepository.updatePersonalTask(taskId, taskUpdateDto);
        return new Response(ResponseMessage.SUCCESS, HttpStatus.OK, taskUpdateDto);
    }

    @Override
    public Object addSubTaskToPersonalTask(String taskId, SubTaskDto subTaskDto) {
        Task task = taskRepository.getProjectTask(taskId);
        if (task == null)
            return new ErrorMessage("Task not found", HttpStatus.NOT_FOUND);
        if (!task.getTaskAssignee().equals(subTaskDto.getSubTaskCreator()))
            return new ErrorMessage(ResponseMessage.UNAUTHORIZED, HttpStatus.UNAUTHORIZED);
        SubTask newSubTask = new SubTask();
        newSubTask.setSubtaskId(utilsService.getUUId());
        newSubTask.setTaskId(taskId);
        newSubTask.setSubtaskName(subTaskDto.getSubtaskName());
        newSubTask.setSubtaskStatus(false);
        newSubTask.setIsDeleted(false);
        Object subTask = subTaskRepository.addSubTaskToProject(newSubTask);
        return new Response(ResponseMessage.SUCCESS, HttpStatus.OK, subTask);
    }

    @Override
    public Object getAllSubTaksOfATask(String userId, String taskId) {
        Task task = taskRepository.getProjectTask(taskId);
        if (task == null)
            return new ErrorMessage("Task not found", HttpStatus.NOT_FOUND);
        if (!task.getTaskAssignee().equals(userId))
            return new ErrorMessage(ResponseMessage.UNAUTHORIZED, HttpStatus.UNAUTHORIZED);
        List<SubTask> subTaskList = subTaskRepository.getAllSubTaksOfATask(taskId);
        return new Response(ResponseMessage.SUCCESS, HttpStatus.OK, subTaskList);
    }

    @Override
    public Object updateSubTaskOfATask(String userId, String taskId, String subTaskId, SubTaskUpdateDto subTaskUpdateDto) {
        Task task = taskRepository.getProjectTask(taskId);
        if (task == null)
            return new ErrorMessage("Task not found!", HttpStatus.NOT_FOUND);
        SubTask subTask = subTaskRepository.getSubTaskById(subTaskId);
        if (subTask == null)
            return new ErrorMessage("SubTask not found", HttpStatus.NOT_FOUND);
       if (!subTaskUpdateDto.getSubTaskEditor().equals(task.getTaskAssignee()))
           return new ErrorMessage(ResponseMessage.UNAUTHORIZED, HttpStatus.UNAUTHORIZED);
        if (subTaskUpdateDto.getSubtaskName() != null)
            subTask.setSubtaskName(subTaskUpdateDto.getSubtaskName());
        if (subTaskUpdateDto.getSubTaskStatus() != null)
            subTask.setSubtaskStatus(subTaskUpdateDto.getSubTaskStatus());
        SubTask updatedSubTask = subTaskRepository.updateSubTaskById(subTask);

        return new Response(ResponseMessage.SUCCESS, HttpStatus.OK, updatedSubTask);
    }

    @Override
    public Object getPersonalTaskFiles(String userId, String taskId) {
        PersonalTask task = personalTaskRepository.getPersonalTaskByUserId(userId, taskId);
        if (task == null)
            return new ErrorMessage(ResponseMessage.NO_RECORD, HttpStatus.NOT_FOUND);
        Object fileList = taskFileRepository.getAllTaskFiles(taskId);
        return new Response(ResponseMessage.SUCCESS, HttpStatus.OK, fileList);
    }

    @Override
    public Object deletePersonalTaskFile(String userId, String taskId, String taskFileId) {
        PersonalTask task = personalTaskRepository.getPersonalTaskByUserId(userId, taskId);
        if (task == null)
            return new ErrorMessage(ResponseMessage.NO_RECORD, HttpStatus.NOT_FOUND);
        taskFileRepository.flagTaskFile(taskFileId);
        return new Response(ResponseMessage.SUCCESS, HttpStatus.OK);
    }

    @Override
    public Object flagPersonalTask(String userId, String taskId) {
        PersonalTask task = personalTaskRepository.getPersonalTaskByUserId(userId, taskId);
        if (task == null)
            return new ErrorMessage(ResponseMessage.NO_RECORD, HttpStatus.NOT_FOUND);
        personalTaskRepository.flagPersonalTask(taskId);
        subTaskRepository.flagTaskBoundSubTasks(taskId);
        return new Response(ResponseMessage.SUCCESS, HttpStatus.OK);
    }

    @Override
    public Object flagPersonalSubTask(String userId, String taskId, String subTaskId) {
        SubTask subTask = subTaskRepository.getSubTaskById(subTaskId);
        if (subTask == null)
            return new ErrorMessage("SubTask not found", HttpStatus.NOT_FOUND);
        Task task = taskRepository.getProjectTask(taskId);
        if (task == null)
            return new ErrorMessage("Task not Found", HttpStatus.NOT_FOUND);
        if (!task.getTaskAssignee().equals(userId))
            return new ErrorMessage(ResponseMessage.UNAUTHORIZED, HttpStatus.UNAUTHORIZED);
        subTaskRepository.flagSubTaskOfATask(subTaskId);

        return new Response(ResponseMessage.SUCCESS, HttpStatus.OK);
    }
}
