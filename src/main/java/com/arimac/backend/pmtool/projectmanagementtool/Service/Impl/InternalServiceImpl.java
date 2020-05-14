package com.arimac.backend.pmtool.projectmanagementtool.Service.Impl;

import com.arimac.backend.pmtool.projectmanagementtool.Response.Response;
import com.arimac.backend.pmtool.projectmanagementtool.Service.InternalService;
import com.arimac.backend.pmtool.projectmanagementtool.Service.NpTaskService;
import com.arimac.backend.pmtool.projectmanagementtool.dtos.Internal.UpdateAliasDto;
import com.arimac.backend.pmtool.projectmanagementtool.dtos.PersonalTask.PersonalTask;
import com.arimac.backend.pmtool.projectmanagementtool.dtos.PersonalTask.PersonalTaskDto;
import com.arimac.backend.pmtool.projectmanagementtool.enumz.ResponseMessage;
import com.arimac.backend.pmtool.projectmanagementtool.model.Project;
import com.arimac.backend.pmtool.projectmanagementtool.model.Task;
import com.arimac.backend.pmtool.projectmanagementtool.repository.PersonalTaskRepository;
import com.arimac.backend.pmtool.projectmanagementtool.repository.ProjectRepository;
import com.arimac.backend.pmtool.projectmanagementtool.repository.TaskRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InternalServiceImpl implements InternalService {

    private static final Logger logger = LoggerFactory.getLogger(InternalServiceImpl.class);

    private final NpTaskService npTaskService;
    private final ProjectRepository projectRepository;
    private final TaskRepository taskRepository;
    private final PersonalTaskRepository personalTaskRepository;

    public InternalServiceImpl(ProjectRepository projectRepository, TaskRepository taskRepository, NpTaskService npTaskService, PersonalTaskRepository personalTaskRepository) {
        this.projectRepository = projectRepository;
        this.taskRepository = taskRepository;
        this.npTaskService = npTaskService;
        this.personalTaskRepository = personalTaskRepository;
    }

    @Override
    public Object updateProjectAlias() {
        List<Project> projects = projectRepository.getAllProjects();
        int taskCount = 0;
        int totalProjects = 0;
        for (Project project: projects) {
            logger.info("ProjectId: {} | ProjectName: {}", project.getProjectId(), project.getProjectName());
            List<Task> projectTaskList = taskRepository.getAllProjectTasksByUser(project.getProjectId());
            int issueCount = project.getIssueCount() + 1;
            for (Task task : projectTaskList) {
                String taskAlias = project.getProjectAlias() + "-" + issueCount;
                logger.info("TaskId: {} | TaskName: {} | Alias: {} | Count: {}", task.getTaskId(), task.getTaskName(), taskAlias, taskCount);
                taskRepository.updateProjectAlias(task.getTaskId(), taskAlias);
                issueCount +=1;
                taskCount +=1;
            }
            totalProjects += 1;
        }
        UpdateAliasDto updateAliasDto = new UpdateAliasDto();
        updateAliasDto.setUpdatedProjects(totalProjects);
        updateAliasDto.setUpdatedTasks(taskCount);

        return new Response(ResponseMessage.SUCCESS, updateAliasDto);
    }

    @Override
    public Object migratePersonalTask() {
        List<PersonalTask> allPersonalTasks = personalTaskRepository.getAllPersonalTasksOfAllUsers();
        logger.info("Personal Task List {}", allPersonalTasks.size());
        int count = 0;
        for (PersonalTask personalTask: allPersonalTasks){
            PersonalTaskDto personalTaskDto = new PersonalTaskDto();
            personalTaskDto.setTaskName(personalTask.getTaskName());
            personalTaskDto.setTaskAssignee(personalTask.getTaskAssignee());
            personalTaskDto.setTaskDueDate(personalTask.getTaskDueDateAt());
            personalTaskDto.setTaskRemindOnDate(personalTask.getTaskReminderAt());
            personalTaskDto.setTaskNotes(personalTask.getTaskNote());
            npTaskService.addPersonalTask(personalTaskDto);
        }
        return new Response(ResponseMessage.SUCCESS, HttpStatus.OK, count);
    }
}
