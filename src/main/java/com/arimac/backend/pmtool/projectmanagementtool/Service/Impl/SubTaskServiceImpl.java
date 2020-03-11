package com.arimac.backend.pmtool.projectmanagementtool.Service.Impl;

import com.arimac.backend.pmtool.projectmanagementtool.Response.Response;
import com.arimac.backend.pmtool.projectmanagementtool.Service.SubTaskService;
import com.arimac.backend.pmtool.projectmanagementtool.dtos.ProjectUserResponseDto;
import com.arimac.backend.pmtool.projectmanagementtool.dtos.SubTaskDto;
import com.arimac.backend.pmtool.projectmanagementtool.enumz.ProjectRoleEnum;
import com.arimac.backend.pmtool.projectmanagementtool.enumz.ResponseMessage;
import com.arimac.backend.pmtool.projectmanagementtool.exception.ErrorMessage;
import com.arimac.backend.pmtool.projectmanagementtool.model.SubTask;
import com.arimac.backend.pmtool.projectmanagementtool.model.Task;
import com.arimac.backend.pmtool.projectmanagementtool.repository.ProjectRepository;
import com.arimac.backend.pmtool.projectmanagementtool.repository.SubTaskRepository;
import com.arimac.backend.pmtool.projectmanagementtool.repository.TaskRepository;
import com.arimac.backend.pmtool.projectmanagementtool.utils.UtilsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
public class SubTaskServiceImpl implements SubTaskService {
    private static final Logger logger = LoggerFactory.getLogger(SubTaskServiceImpl.class);

    private final ProjectRepository projectRepository;
    private  final TaskRepository taskRepository;
    private final SubTaskRepository subTaskRepository;
    private final UtilsService utilsService;

    public SubTaskServiceImpl(ProjectRepository projectRepository, TaskRepository taskRepository, SubTaskRepository subTaskRepository, UtilsService utilsService) {
        this.projectRepository = projectRepository;
        this.taskRepository = taskRepository;
        this.subTaskRepository = subTaskRepository;
        this.utilsService = utilsService;
    }

    @Override
    public Object addSubTaskToProject(String projectId, String taskId, SubTaskDto subTaskDto) {
        Task task = taskRepository.getProjectTask(taskId);
        if (task == null)
            return new ErrorMessage("Task not found", HttpStatus.NOT_FOUND);
        ProjectUserResponseDto projectUser = projectRepository.getProjectByIdAndUserId(projectId, subTaskDto.getSubTaskCreator());
        if (projectUser == null) // check this
            return new ErrorMessage(ResponseMessage.USER_NOT_MEMBER, HttpStatus.UNAUTHORIZED);
        if (!((task.getTaskAssignee().equals(subTaskDto.getSubTaskCreator())) || (task.getTaskInitiator().equals(subTaskDto.getSubTaskCreator())) ||  (projectUser.getAssigneeProjectRole() == ProjectRoleEnum.owner.getRoleValue())) )
            return new ErrorMessage("You don't have access", HttpStatus.UNAUTHORIZED);
        SubTask newSubTask = new SubTask();
        newSubTask.setSubtaskId(utilsService.getUUId());
        newSubTask.setTaskId(taskId);
        newSubTask.setSubtaskName(subTaskDto.getSubtaskName());
        newSubTask.setSubtaskStatus(false);

        Object subTask = subTaskRepository.addSubTaskToProject(newSubTask);
        return new Response(ResponseMessage.SUCCESS, HttpStatus.OK, subTask);
    }

}
