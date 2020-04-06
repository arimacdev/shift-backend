package com.arimac.backend.pmtool.projectmanagementtool.Service.Impl;

import com.arimac.backend.pmtool.projectmanagementtool.Response.Response;
import com.arimac.backend.pmtool.projectmanagementtool.Service.NpTaskService;
import com.arimac.backend.pmtool.projectmanagementtool.dtos.SubTaskDto;
import com.arimac.backend.pmtool.projectmanagementtool.dtos.TaskDto;
import com.arimac.backend.pmtool.projectmanagementtool.dtos.TaskUpdateDto;
import com.arimac.backend.pmtool.projectmanagementtool.enumz.ResponseMessage;
import com.arimac.backend.pmtool.projectmanagementtool.enumz.TaskStatusEnum;
import com.arimac.backend.pmtool.projectmanagementtool.enumz.TaskTypeEnum;
import com.arimac.backend.pmtool.projectmanagementtool.exception.ErrorMessage;
import com.arimac.backend.pmtool.projectmanagementtool.model.SubTask;
import com.arimac.backend.pmtool.projectmanagementtool.model.Task;
import com.arimac.backend.pmtool.projectmanagementtool.model.User;
import com.arimac.backend.pmtool.projectmanagementtool.repository.SubTaskRepository;
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
    private final SubTaskRepository subTaskRepository;

    public NpTaskServiceImpl(UtilsService utilsService, TaskRepository taskRepository, UserRepository userRepository, SubTaskRepository subTaskRepository) {
        this.utilsService = utilsService;
        this.taskRepository = taskRepository;
        this.userRepository = userRepository;
        this.subTaskRepository = subTaskRepository;
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

    @Override
    public Object updatePersonalTask(String userId, String taskId, TaskUpdateDto taskUpdateDto) {
        if (taskUpdateDto.getTaskAssignee()!= null){
            return new ErrorMessage(ResponseMessage.UNAUTHORIZED_OPERATION, HttpStatus.UNAUTHORIZED);
        }
//        if (  (  (!taskUpdateDto.getTaskStatus().equals(TaskStatusEnum.closed.toString()) ) || (!taskUpdateDto.getTaskStatus().equals(TaskStatusEnum.open.toString()))   ) )
//            return new ErrorMessage("Invalid Task Status", HttpStatus.BAD_REQUEST);
        Task task = taskRepository.getProjectTask(taskId);
        if (task == null)
            return new ErrorMessage(ResponseMessage.NO_RECORD, HttpStatus.NOT_FOUND);
        User user = userRepository.getUserByUserId(userId);
        if (user == null)
            return new ErrorMessage(ResponseMessage.USER_NOT_FOUND, HttpStatus.NOT_FOUND);
        if (!task.getTaskAssignee().equals(userId))
            return new ErrorMessage(ResponseMessage.UNAUTHORIZED, HttpStatus.UNAUTHORIZED);

        if (taskUpdateDto.getTaskName() == null || taskUpdateDto.getTaskName().isEmpty())
            taskUpdateDto.setTaskName(task.getTaskName());
        if (taskUpdateDto.getTaskNotes() == null || taskUpdateDto.getTaskNotes().isEmpty())
            taskUpdateDto.setTaskNotes(task.getTaskNote());
        if (taskUpdateDto.getTaskStatus() == null || taskUpdateDto.getTaskStatus().isEmpty())
            taskUpdateDto.setTaskStatus(task.getTaskStatus().toString());
        if (taskUpdateDto.getTaskDueDate() == null)
            taskUpdateDto.setTaskDueDate(task.getTaskDueDateAt());
        if (taskUpdateDto.getTaskRemindOnDate() == null)
            taskUpdateDto.setTaskRemindOnDate(task.getTaskReminderAt());
        taskUpdateDto.setTaskAssignee(task.getTaskAssignee());
        Object updateTask = taskRepository.updateProjectTask(taskId, taskUpdateDto);

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
}
