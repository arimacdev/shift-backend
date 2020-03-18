package com.arimac.backend.pmtool.projectmanagementtool.Service.Impl;

import com.arimac.backend.pmtool.projectmanagementtool.Response.Response;
import com.arimac.backend.pmtool.projectmanagementtool.Service.TaskService;
import com.arimac.backend.pmtool.projectmanagementtool.dtos.*;
import com.arimac.backend.pmtool.projectmanagementtool.enumz.ProjectRoleEnum;
import com.arimac.backend.pmtool.projectmanagementtool.enumz.ResponseMessage;
import com.arimac.backend.pmtool.projectmanagementtool.enumz.TaskStatusEnum;
import com.arimac.backend.pmtool.projectmanagementtool.exception.ErrorMessage;
import com.arimac.backend.pmtool.projectmanagementtool.model.Task;
import com.arimac.backend.pmtool.projectmanagementtool.model.User;
import com.arimac.backend.pmtool.projectmanagementtool.repository.ProjectRepository;
import com.arimac.backend.pmtool.projectmanagementtool.repository.SubTaskRepository;
import com.arimac.backend.pmtool.projectmanagementtool.repository.TaskRepository;
import com.arimac.backend.pmtool.projectmanagementtool.repository.UserRepository;
import com.arimac.backend.pmtool.projectmanagementtool.utils.UtilsService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class TaskServiceImpl implements TaskService {
    private static final Logger logger = LoggerFactory.getLogger(TaskServiceImpl.class);

    private final SubTaskRepository subTaskRepository;
    private final TaskRepository taskRepository;
    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;
    private final UtilsService utilsService;

    public TaskServiceImpl(SubTaskRepository subTaskRepository, TaskRepository taskRepository, ProjectRepository projectRepository, UserRepository userRepository, UtilsService utilsService) {
        this.subTaskRepository = subTaskRepository;
        this.taskRepository = taskRepository;
        this.projectRepository = projectRepository;
        this.userRepository = userRepository;
        this.utilsService = utilsService;
    }

    @Override
    public Object addTaskToProject(String projectId, TaskDto taskDto) {
        if (taskDto.getTaskName() == null || taskDto.getProjectId() == null || taskDto.getTaskInitiator()== null)
            return new ErrorMessage(ResponseMessage.INVALID_REQUEST_BODY, HttpStatus.BAD_REQUEST);
        ProjectUserResponseDto taskInitiator = projectRepository.getProjectByIdAndUserId(projectId, taskDto.getTaskInitiator());
        if (taskInitiator == null)
            return new ErrorMessage(ResponseMessage.ASSIGNER_NOT_MEMBER, HttpStatus.NOT_FOUND);
//        if (taskInitiator.getIsDeleted())
//            return new ErrorMessage(ResponseMessage.NO_ACCESS, HttpStatus.BAD_REQUEST);
        ProjectUserResponseDto taskAssignee = null;
        if (taskDto.getTaskAssignee() != null)
            taskAssignee = projectRepository.getProjectByIdAndUserId(projectId, taskDto.getTaskInitiator());
        if (taskAssignee == null)
            return new ErrorMessage(ResponseMessage.ASSIGNEE_NOT_MEMBER, HttpStatus.NOT_FOUND);
        Task task = new Task();
        task.setTaskId(utilsService.getUUId());
        task.setProjectId(taskDto.getProjectId());
        task.setTaskName(taskDto.getTaskName());
        task.setTaskInitiator(taskDto.getTaskInitiator());
        if (task.getTaskAssignee() == null){
            task.setTaskAssignee(taskDto.getTaskInitiator());
        } else {
            task.setTaskAssignee(taskDto.getTaskAssignee());
        }
        task.setTaskNote(taskDto.getTaskNotes());
        if (taskDto.getTaskStatus() == null){
            task.setTaskStatus(TaskStatusEnum.pending);
        } else {
            task.setTaskStatus(taskDto.getTaskStatus());
        }
        task.setTaskCreatedAt(utilsService.getCurrentTimestamp());
        task.setTaskDueDateAt(taskDto.getTaskDueDate());
        task.setTaskReminderAt(taskDto.getTaskRemindOnDate());
        task.setIsDeleted(false);
        taskRepository.addTaskToProject(task);
        return new Response(ResponseMessage.SUCCESS, HttpStatus.OK, task);
    }

    @Override
    public Object getAllProjectTasksByUser(String userId, String projectId) {
        ProjectUserResponseDto projectUser = projectRepository.getProjectByIdAndUserId(projectId, userId);
        if (projectUser == null)
            return new ErrorMessage(ResponseMessage.USER_NOT_MEMBER, HttpStatus.UNAUTHORIZED);
//        if (projectUser.getIsDeleted())
//            return new ErrorMessage(ResponseMessage.NO_ACCESS, HttpStatus.BAD_REQUEST);
       List<Task> taskList = taskRepository.getAllProjectTasksByUser(projectId);
       return new Response(ResponseMessage.SUCCESS, HttpStatus.OK, taskList);
    }

    @Override
    public Object getAllUserAssignedTasks(String userId, String projectId) {
        ProjectUserResponseDto projectUser = projectRepository.getProjectByIdAndUserId(projectId, userId);
        if (projectUser == null)
            return new ErrorMessage(ResponseMessage.USER_NOT_MEMBER, HttpStatus.NOT_FOUND);
//        if (projectUser.getIsDeleted())
//            return new ErrorMessage(ResponseMessage.NO_ACCESS, HttpStatus.BAD_REQUEST);
        List<Task> taskList = taskRepository.getAllUserAssignedTasks(userId, projectId);
        return new Response(ResponseMessage.SUCCESS, HttpStatus.OK, taskList);
    }

    @Override
    public Object getProjectTask(String userId, String projectId, String taskId) {
        ProjectUserResponseDto projectUser = projectRepository.getProjectByIdAndUserId(projectId, userId);
        if (projectUser == null)
            return new ErrorMessage(ResponseMessage.USER_NOT_MEMBER, HttpStatus.NOT_FOUND);
//        if (projectUser.getIsDeleted())
//            return new ErrorMessage(ResponseMessage.NO_ACCESS, HttpStatus.BAD_REQUEST);
        Task task = taskRepository.getProjectTask(taskId);
        if (task == null)
            return new ErrorMessage(ResponseMessage.NO_RECORD, HttpStatus.NOT_FOUND);
        return new Response(ResponseMessage.SUCCESS, HttpStatus.OK, task);
    }

    @Override
    public Object updateProjectTask(String userId, String projectId, String taskId, TaskUpdateDto taskUpdateDto) {
        Task task = taskRepository.getProjectTask(taskId);
        if (task == null)
            return new ErrorMessage(ResponseMessage.NO_RECORD, HttpStatus.NOT_FOUND);
        ProjectUserResponseDto projectUser = projectRepository.getProjectByIdAndUserId(projectId, userId);
        if (projectUser == null)
            return new ErrorMessage(ResponseMessage.USER_NOT_MEMBER, HttpStatus.NOT_FOUND);
//        if (projectUser.getIsDeleted())
//            return new ErrorMessage(ResponseMessage.NO_ACCESS, HttpStatus.BAD_REQUEST);
        if (!((task.getTaskAssignee().equals(userId)) || (projectUser.getAssigneeProjectRole() == ProjectRoleEnum.owner.getRoleValue()))) // check for super admin
            return new ErrorMessage("User doesn't have privileges", HttpStatus.FORBIDDEN);
        if (taskUpdateDto.getTaskName() == null)
            taskUpdateDto.setTaskName(task.getTaskName());
        if (taskUpdateDto.getTaskAssignee() == null)
            taskUpdateDto.setTaskAssignee(task.getTaskAssignee());
        if (taskUpdateDto.getTaskNotes() == null)
            taskUpdateDto.setTaskNotes(task.getTaskNote());
        if (taskUpdateDto.getTaskStatus() == null)
            taskUpdateDto.setTaskStatus(task.getTaskStatus());
        if (taskUpdateDto.getTaskDueDate() == null)
            taskUpdateDto.setTaskDueDate(task.getTaskDueDateAt());
        if (taskUpdateDto.getTaskRemindOnDate() == null)
            taskUpdateDto.setTaskRemindOnDate(task.getTaskReminderAt());

        Object updateTask = taskRepository.updateProjectTask(taskId, taskUpdateDto);

        return new Response(ResponseMessage.SUCCESS, HttpStatus.OK, updateTask);
    }

    @Override
    public Object flagProjectTask(String userId, String projectId, String taskId) {
        ProjectUserResponseDto projectUser = projectRepository.getProjectByIdAndUserId(projectId, userId);
//        if (projectUser.getIsDeleted())
//            return new ErrorMessage(ResponseMessage.NO_ACCESS, HttpStatus.BAD_REQUEST);
        if (projectUser == null)
            return new ErrorMessage(ResponseMessage.USER_NOT_MEMBER, HttpStatus.UNAUTHORIZED);
        Task task = taskRepository.getProjectTask(taskId);
        if (task == null)
            return new ErrorMessage(ResponseMessage.NO_RECORD, HttpStatus.NOT_FOUND);
        if (!((task.getTaskAssignee().equals(userId)) || (projectUser.getAssigneeProjectRole() == ProjectRoleEnum.owner.getRoleValue()))) // check for super admin privileges about delete
            return new ErrorMessage("User doesn't have privileges", HttpStatus.FORBIDDEN);
        taskRepository.flagProjectTask(taskId);
        subTaskRepository.flagTaskBoundSubTasks(taskId);

        return new Response(ResponseMessage.SUCCESS);
    }

    @Override
    public Object getProjectTaskCompletionByUser(String userId, String projectId) {
        ProjectUserResponseDto projectUser = projectRepository.getProjectByIdAndUserId(projectId, userId);
        if (projectUser == null)
            return new ErrorMessage(ResponseMessage.USER_NOT_MEMBER, HttpStatus.UNAUTHORIZED);
//        if (projectUser.getIsDeleted())
//            return new ErrorMessage(ResponseMessage.NO_ACCESS, HttpStatus.BAD_REQUEST);
        List<Task> taskList = taskRepository.getAllProjectTasksByUser(projectId);
        Map<String, TaskCompletionDto> userTaskCompletionMap = getTaskCompletionMap(taskList);
        return new Response(ResponseMessage.SUCCESS, HttpStatus.OK, userTaskCompletionMap);
    }

    private Map<String, TaskCompletionDto> getTaskCompletionMap(List<Task> taskList){
        Map<String, TaskCompletionDto> userTaskCompletionMap = new HashMap<>();
        String user = null;
        for (int i = 0 ; i < taskList.size(); i++){
            Task task = taskList.get(i);
            user = task.getTaskAssignee();
            TaskCompletionDto taskCompletionDto = new TaskCompletionDto();
            if (userTaskCompletionMap.get(user) != null){
                taskCompletionDto = userTaskCompletionMap.get(user);
                int completed = taskCompletionDto.getCompleted();
                int total = taskCompletionDto.getTotalTasks();
                if (task.getTaskStatus().equals(TaskStatusEnum.closed))
                    completed += 1;
                total += 1;
                taskCompletionDto.setCompleted(completed);
                taskCompletionDto.setTotalTasks(total);
                userTaskCompletionMap.put(user, taskCompletionDto);
            } else {
                if (task.getTaskStatus().equals(TaskStatusEnum.closed)){
                    taskCompletionDto.setCompleted(1);
                } else {
                    taskCompletionDto.setCompleted(0);
                }
                taskCompletionDto.setTotalTasks(1);
                userTaskCompletionMap.put(user, taskCompletionDto);
            }
        }

        return userTaskCompletionMap;
    }


    @Override
    public Object getProjectTaskCompletionUserDetails(String userId, String projectId) {
        ProjectUserResponseDto projectUser = projectRepository.getProjectByIdAndUserId(projectId, userId);
        if (projectUser == null)
            return new ErrorMessage(ResponseMessage.USER_NOT_MEMBER, HttpStatus.UNAUTHORIZED);
//        List<User> userList = userRepository.getAllProjectUsers(projectId);
        List<Task> taskList = taskRepository.getAllProjectTasksByUser(projectId);
        Map<String, TaskCompletionDto> userTaskCompletionMap = getTaskCompletionMap(taskList);
        List<UserProjectDto> userProjectDtoList = userRepository.getUsersProjectDetails(projectId);
        List<UserProjectResponseDto> userTaskStatusList = new ArrayList<>();
        for (UserProjectDto projectUserDetails : userProjectDtoList){
            TaskCompletionDto taskStatus = userTaskCompletionMap.get(projectUserDetails.getAssigneeId());
            UserProjectResponseDto userTaskStatus = new UserProjectResponseDto();
            userTaskStatus.setProjectId(projectId);
            userTaskStatus.setAssigneeId(projectUserDetails.getAssigneeId());
            userTaskStatus.setAssigneeFirstName(projectUserDetails.getAssigneeFirstName());
            userTaskStatus.setAssigneeLastName(projectUserDetails.getAssigneeLastName());
            userTaskStatus.setProjectRoleId(projectUserDetails.getProjectRoleId());
            userTaskStatus.setProjectRoleName(projectUserDetails.getProjectRoleName());
            userTaskStatus.setProjectJobRoleName(projectUserDetails.getProjectJobRoleName());
            userTaskStatus.setTasksCompleted(taskStatus.getCompleted());
            userTaskStatus.setTotalTasks(taskStatus.getTotalTasks());
            userTaskStatusList.add(userTaskStatus);
        }
        return new Response(ResponseMessage.SUCCESS, HttpStatus.OK, userTaskStatusList);
    }
}
