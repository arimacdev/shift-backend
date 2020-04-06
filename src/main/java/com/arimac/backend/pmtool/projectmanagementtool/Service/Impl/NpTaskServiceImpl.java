package com.arimac.backend.pmtool.projectmanagementtool.Service.Impl;

import com.arimac.backend.pmtool.projectmanagementtool.Response.Response;
import com.arimac.backend.pmtool.projectmanagementtool.Service.NpTaskService;
import com.arimac.backend.pmtool.projectmanagementtool.dtos.TaskDto;
import com.arimac.backend.pmtool.projectmanagementtool.enumz.ResponseMessage;
import com.arimac.backend.pmtool.projectmanagementtool.enumz.TaskStatusEnum;
import com.arimac.backend.pmtool.projectmanagementtool.enumz.TaskTypeEnum;
import com.arimac.backend.pmtool.projectmanagementtool.exception.ErrorMessage;
import com.arimac.backend.pmtool.projectmanagementtool.model.Task;
import com.arimac.backend.pmtool.projectmanagementtool.model.User;
import com.arimac.backend.pmtool.projectmanagementtool.repository.TaskRepository;
import com.arimac.backend.pmtool.projectmanagementtool.repository.UserRepository;
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
    private final TaskRepository taskRepository;
    private final UserRepository userRepository;

    public NpTaskServiceImpl(UtilsService utilsService, TaskRepository taskRepository, UserRepository userRepository) {
        this.utilsService = utilsService;
        this.taskRepository = taskRepository;
        this.userRepository = userRepository;
    }


    @Override
    public Object addPersonalTask(TaskDto taskDto) {
        if ( (taskDto.getTaskName() == null || taskDto.getTaskName().isEmpty()) ||  (taskDto.getTaskAssignee()== null || taskDto.getTaskAssignee().isEmpty()) )
            return new ErrorMessage(ResponseMessage.INVALID_REQUEST_BODY, HttpStatus.BAD_REQUEST);
        if (!taskDto.getTaskType().equals(TaskTypeEnum.personal)){
            return new ErrorMessage("Task Type Mismatch", HttpStatus.BAD_REQUEST);
        }
        User user = userRepository.getUserByUserId(taskDto.getTaskAssignee());
        if (user == null)
            return new ErrorMessage(ResponseMessage.USER_NOT_FOUND, HttpStatus.NOT_FOUND);
        Task task = new Task();
        task.setTaskId(utilsService.getUUId());
        task.setTaskType(taskDto.getTaskType());
        task.setTaskName(taskDto.getTaskName());
        task.setTaskAssignee(taskDto.getTaskAssignee());
        task.setTaskInitiator(taskDto.getTaskAssignee());
        task.setTaskNote(taskDto.getTaskNotes());
        task.setTaskStatus(TaskStatusEnum.open);
        task.setTaskCreatedAt(utilsService.getCurrentTimestamp());
        task.setProjectId("personalTasks");
        if (taskDto.getTaskDueDate() != null){
            task.setTaskDueDateAt(taskDto.getTaskDueDate());
        }
        if (taskDto.getTaskRemindOnDate() != null){
            task.setTaskReminderAt(task.getTaskReminderAt());
        }
        task.setIsDeleted(false);
        taskRepository.addTaskToProject(task);
        return new Response(ResponseMessage.SUCCESS, HttpStatus.OK);
    }

    @Override
    public Object getAllPersonalTasks(String userId) {
        User user = userRepository.getUserByUserId(userId);
        if (user == null)
            return new ErrorMessage(ResponseMessage.USER_NOT_FOUND, HttpStatus.NOT_FOUND);
        List<Task> personalTasks = taskRepository.getAllPersonalTasks(userId);
        return new Response(ResponseMessage.SUCCESS, HttpStatus.OK, personalTasks);
    }
}
