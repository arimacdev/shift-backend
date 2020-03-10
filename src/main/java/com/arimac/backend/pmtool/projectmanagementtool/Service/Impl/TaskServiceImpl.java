package com.arimac.backend.pmtool.projectmanagementtool.Service.Impl;

import com.arimac.backend.pmtool.projectmanagementtool.Service.TaskService;
import com.arimac.backend.pmtool.projectmanagementtool.dtos.TaskDto;
import com.arimac.backend.pmtool.projectmanagementtool.enumz.TaskStatusEnum;
import com.arimac.backend.pmtool.projectmanagementtool.model.Task;
import com.arimac.backend.pmtool.projectmanagementtool.repository.TaskRepository;
import com.arimac.backend.pmtool.projectmanagementtool.utils.UtilsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class TaskServiceImpl implements TaskService {
    private static final Logger logger = LoggerFactory.getLogger(TaskServiceImpl.class);

    private final TaskRepository taskRepository;
    private final UtilsService utilsService;

    public TaskServiceImpl(TaskRepository taskRepository, UtilsService utilsService) {
        this.taskRepository = taskRepository;
        this.utilsService = utilsService;
    }

    @Override
    public Task addTaskToProject(String projectId, TaskDto taskDto) {
        Task task = new Task();
        task.setTaskId(utilsService.getUUId());
        task.setProjectId(taskDto.getProjectId());
        task.setTaskName(taskDto.getTaskName());
        task.setTaskInitiator(taskDto.getTaskInitiator());
        task.setTaskAssignee(taskDto.getTaskAssignee());
        task.setTaskNote(taskDto.getTaskNotes());
        task.setTaskStatus(TaskStatusEnum.pending.toString());
        task.setTaskCreatedAt(utilsService.getCurrentTimestamp());
        task.setTaskDueDateAt(taskDto.getTaskDueDate());
        task.setTaskReminderAt(taskDto.getTaskRemindOnDate());

        taskRepository.addTaskToProject(task);

        return task;

    }
}
