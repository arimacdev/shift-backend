package com.arimac.backend.pmtool.projectmanagementtool.Service.Impl;

import com.arimac.backend.pmtool.projectmanagementtool.Response.Response;
import com.arimac.backend.pmtool.projectmanagementtool.Service.IdpUserService;
import com.arimac.backend.pmtool.projectmanagementtool.Service.InternalService;
import com.arimac.backend.pmtool.projectmanagementtool.Service.NpTaskService;
import com.arimac.backend.pmtool.projectmanagementtool.Service.UserService;
import com.arimac.backend.pmtool.projectmanagementtool.dtos.Internal.UpdateAliasDto;
import com.arimac.backend.pmtool.projectmanagementtool.dtos.PersonalTask.PersonalTask;
import com.arimac.backend.pmtool.projectmanagementtool.dtos.PersonalTask.PersonalTaskDto;
import com.arimac.backend.pmtool.projectmanagementtool.dtos.Role.UserRoleDto;
import com.arimac.backend.pmtool.projectmanagementtool.enumz.ResponseMessage;
import com.arimac.backend.pmtool.projectmanagementtool.model.Project;
import com.arimac.backend.pmtool.projectmanagementtool.model.Task;
import com.arimac.backend.pmtool.projectmanagementtool.model.User;
import com.arimac.backend.pmtool.projectmanagementtool.repository.PersonalTaskRepository;
import com.arimac.backend.pmtool.projectmanagementtool.repository.ProjectRepository;
import com.arimac.backend.pmtool.projectmanagementtool.repository.TaskRepository;
import com.arimac.backend.pmtool.projectmanagementtool.repository.UserRepository;
import org.json.JSONObject;
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
    private final IdpUserService idpUserService;
    private final UserRepository userRepository;

    public InternalServiceImpl(ProjectRepository projectRepository, TaskRepository taskRepository, NpTaskService npTaskService, PersonalTaskRepository personalTaskRepository, IdpUserService idpUserService, UserRepository userRepository) {
        this.projectRepository = projectRepository;
        this.taskRepository = taskRepository;
        this.npTaskService = npTaskService;
        this.personalTaskRepository = personalTaskRepository;
        this.idpUserService = idpUserService;
        this.userRepository = userRepository;
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

    @Override
    public Object addUserRole() {
        List<User> userList = userRepository.getAllUsers();
        int userCount = 0;
        int failCount = 0;
        logger.info("User List Size: {}", userList.size());
        for (User user: userList){
            UserRoleDto userRoleDto = new UserRoleDto();
            userRoleDto.setRoleId("554c46d8-16f6-4ac0-a881-c8636a666a14");
            userRoleDto.setRoleName("USER");
            userRoleDto.setUserId(user.getUserId());
            try {
                idpUserService.addRoleToUser(user.getIdpUserId(), userRoleDto, true);
                userCount += 1;
            } catch (Exception e){
                failCount += 1;
                logger.info("Exception", e);
            }

        }
        return new Response(ResponseMessage.SUCCESS, HttpStatus.OK, userCount);
    }

    @Override
    public Object addUserNameToUsers() {
        List<User> userList = userRepository.getAllUsers();
        int userCount = 0;
        int failCount = 0;
        logger.info("User List Size: {}", userList.size());
        for (User user: userList) {
            try {
                JSONObject userJO = idpUserService.getUserByIdpUserId(user.getIdpUserId(), true);
                userRepository.updateUserName(user.getUserId(), userJO.getString("username"));
                userCount +=1;
            } catch (Exception e){
                logger.info("Exception", e);
            }
        }
        return new Response(ResponseMessage.SUCCESS,HttpStatus.OK,userCount);
    }
}
