package com.arimac.backend.pmtool.projectmanagementtool.Service.Impl;

import com.arimac.backend.pmtool.projectmanagementtool.Response.Response;
import com.arimac.backend.pmtool.projectmanagementtool.Service.ProjectService;
import com.arimac.backend.pmtool.projectmanagementtool.Service.TaskService;
import com.arimac.backend.pmtool.projectmanagementtool.dtos.*;
import com.arimac.backend.pmtool.projectmanagementtool.enumz.ProjectRoleEnum;
import com.arimac.backend.pmtool.projectmanagementtool.enumz.ProjectStatusEnum;
import com.arimac.backend.pmtool.projectmanagementtool.enumz.ResponseMessage;
import com.arimac.backend.pmtool.projectmanagementtool.enumz.TaskTypeEnum;
import com.arimac.backend.pmtool.projectmanagementtool.exception.ErrorMessage;
import com.arimac.backend.pmtool.projectmanagementtool.model.Project;
import com.arimac.backend.pmtool.projectmanagementtool.model.Project_User;
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
public class ProjectServiceImpl implements ProjectService {

    private static final Logger logger = LoggerFactory.getLogger(ProjectServiceImpl.class);

    private final TaskService taskService;
    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final TaskRepository taskRepository;
    private final SubTaskRepository subTaskRepository;
    private final UtilsService utilsService;

    public ProjectServiceImpl(TaskService taskService, ProjectRepository projectRepository, UserRepository userRepository, RoleRepository roleRepository, TaskRepository taskRepository, SubTaskRepository subTaskRepository, UtilsService utilsService) {
        this.taskService = taskService;
        this.projectRepository = projectRepository;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.taskRepository = taskRepository;
        this.subTaskRepository = subTaskRepository;
        this.utilsService = utilsService;
    }

    @Override
    public Object createProject(ProjectDto projectDto) {
        if ( (projectDto.getProjectAlias() == null || projectDto.getProjectAlias().isEmpty()) || (projectDto.getProjectName() == null || projectDto.getProjectName().isEmpty()) )
            return new ErrorMessage(ResponseMessage.INVALID_REQUEST_BODY, HttpStatus.BAD_REQUEST);
        Project project = new Project();
        //TODO check role of user
        String projectId = utilsService.getUUId();
        project.setProjectId(projectId);
        project.setProjectName(projectDto.getProjectName());
        project.setProjectAlias(projectDto.getProjectAlias());
        project.setClientId(projectDto.getClientId());
        project.setProjectStartDate(projectDto.getProjectStartDate());
        project.setProjectEndDate(projectDto.getProjectEndDate());
        project.setProjectStatus(ProjectStatusEnum.presalesPD);
        project.setIsDeleted(false);
        projectRepository.createProject(project);

        Project_User assignment = new Project_User();
        assignment.setProjectId(projectId);
        assignment.setAssigneeId(projectDto.getProjectOwner());
        assignment.setAssignedAt(utilsService.getCurrentTimestamp());
        assignment.setAssigneeJobRole("ADMIN");
        assignment.setAssigneeProjectRole(ProjectRoleEnum.owner.getRoleValue());

        projectRepository.assignUserToProject(projectId,assignment);

        return new Response(ResponseMessage.SUCCESS, project);
    }

    @Override
    public Object getAllProjects(String userId) {
        //TODO check role of a user

        List<ProjectUserResponseDto> projectList;

        projectList = projectRepository.getAllProjectsByUser(userId);
//        projectList = transactionRepository.getAllProjects();

//        if (projectList.isEmpty())
//            return new Response(ResponseMessage.NO_RECORD, HttpStatus.BAD_REQUEST,projectList);
//      return new Response(ResponseMessage.NO_RECORD, HttpStatus.BAD_REQUEST, projectList);
        return new Response(ResponseMessage.SUCCESS, projectList);

    }

    @Override
    public Object getProjectByUser(String projectId, String userId) {
        ProjectUserResponseDto userProject = projectRepository.getProjectByIdAndUserId(projectId, userId);
        if (userProject == null)
            return new ErrorMessage(ResponseMessage.NO_RECORD, HttpStatus.BAD_REQUEST);
        return new Response(ResponseMessage.SUCCESS, userProject);
    }

    @Override
    public Object assignUserToProject(String projectId, UserAssignDto userAssignDto) {
        //TODO check admin role
        User assigner = userRepository.getUserByUserId(userAssignDto.getAssigneeId());
        if (assigner == null)
            return new ErrorMessage("Assigner doesn't exist", HttpStatus.NOT_FOUND);
        ProjectUserResponseDto assignerProject = projectRepository.getProjectByIdAndUserId(projectId, userAssignDto.getAssignerId()); // Check project assigner is assigned & isAdmin
        if (assignerProject == null)
            return new ErrorMessage("Assigner doesn't belong to the project", HttpStatus.UNAUTHORIZED);
        if (!((assignerProject.getAssigneeProjectRole() == ProjectRoleEnum.admin.getRoleValue()) || (assignerProject.getAssigneeProjectRole() == ProjectRoleEnum.owner.getRoleValue())))
            return new ErrorMessage("Assigner doesn't have Admin privileges", HttpStatus.UNAUTHORIZED);
        if (userAssignDto.getAssigneeProjectRole() == ProjectRoleEnum.owner.getRoleValue())
            return new ErrorMessage("You can't assign a higher privilege level", HttpStatus.UNAUTHORIZED);
        if (userAssignDto.getAssignerId().equals(userAssignDto.getAssigneeId()))
            return new ErrorMessage("You can't assign yourself to this project", HttpStatus.UNAUTHORIZED);
        ProjectUserResponseDto assigneeProject = projectRepository.getProjectByIdAndUserId(projectId, userAssignDto.getAssigneeId()); // Check project assignee is vacant
        if (assigneeProject != null)
            return new ErrorMessage(ResponseMessage.ALREADY_ASSIGNED, HttpStatus.UNAUTHORIZED);

        Project_User assignment = new Project_User();
        assignment.setProjectId(projectId);
        assignment.setAssigneeId(userAssignDto.getAssigneeId());
        assignment.setAssignedAt(utilsService.getCurrentTimestamp());
        assignment.setAssigneeJobRole(userAssignDto.getAssigneeJobRole());
        assignment.setAssigneeProjectRole(userAssignDto.getAssigneeProjectRole());
        assignment.setIsBlocked(false);

        projectRepository.assignUserToProject(projectId,assignment);

       return new Response(ResponseMessage.SUCCESS);

    }

    @Override
    public Object updateProject(String projectId, ProjectEditDto projectEditDto) {
        ProjectUserResponseDto modifierProject = projectRepository.getProjectByIdAndUserId(projectId, projectEditDto.getModifierId());
        if (modifierProject == null)
            return new ErrorMessage(ResponseMessage.USER_NOT_MEMBER, HttpStatus.UNAUTHORIZED);
        if (!((modifierProject.getAssigneeProjectRole() == ProjectRoleEnum.admin.getRoleValue()) || (modifierProject.getAssigneeProjectRole() == ProjectRoleEnum.owner.getRoleValue())))
            return new ErrorMessage("Assigner doesn't have Admin privileges", HttpStatus.FORBIDDEN);
        Project updatedProject = new Project();
        if (projectEditDto.getProjectName() != null && !projectEditDto.getProjectName().isEmpty()){
            updatedProject.setProjectName(projectEditDto.getProjectName());
        } else {
            updatedProject.setProjectName(modifierProject.getProjectName());
        }
        if (projectEditDto.getClientId() != null && !projectEditDto.getClientId().isEmpty()){
            updatedProject.setClientId(projectEditDto.getClientId());
        } else {
            updatedProject.setClientId(modifierProject.getClientId());
        }
        if (projectEditDto.getProjectStatus() != null && !projectEditDto.getProjectStatus().isEmpty()){
            updatedProject.setProjectStatus(ProjectStatusEnum.valueOf(projectEditDto.getProjectStatus()));
        } else {
            updatedProject.setProjectStatus(ProjectStatusEnum.valueOf(modifierProject.getProjectStatus()));
        }
        if (projectEditDto.getProjectStartDate() != null){
            updatedProject.setProjectStartDate(projectEditDto.getProjectStartDate());
        } else {
            updatedProject.setProjectStartDate(modifierProject.getProjectStartDate());
        }
        if (projectEditDto.getProjectEndDate() != null){
            updatedProject.setProjectEndDate(projectEditDto.getProjectEndDate());
        } else {
            updatedProject.setProjectEndDate(modifierProject.getProjectEndDate());
        }
        projectRepository.updateProject(updatedProject, projectId);

        return new Response(ResponseMessage.SUCCESS, HttpStatus.OK);
    }

    @Override
    public Object updateAssigneeProjectRole(String projectId, String userId, ProjectUserUpdateDto updateDto) {
        ProjectUserResponseDto assignerProject = projectRepository.getProjectByIdAndUserId(projectId, updateDto.getAssignerId());
        if (assignerProject == null)
            return new ErrorMessage(ResponseMessage.NO_RECORD, HttpStatus.NOT_FOUND);
        ProjectUserResponseDto assigneeProject = projectRepository.getProjectByIdAndUserId(projectId, userId);
        if (assigneeProject == null)
            return new ErrorMessage(ResponseMessage.NO_RECORD, HttpStatus.NOT_FOUND);
        if ( !((assignerProject.getAssigneeProjectRole() == ProjectRoleEnum.admin.getRoleValue()) || (assignerProject.getAssigneeProjectRole() == ProjectRoleEnum.owner.getRoleValue())))
            return new ErrorMessage("Assigner doesn't have Admin privileges", HttpStatus.FORBIDDEN);
        if (assignerProject.getAssigneeProjectRole() == ProjectRoleEnum.admin.getRoleValue() && assigneeProject.getAssigneeProjectRole() == ProjectRoleEnum.owner.getRoleValue()){
            return new ErrorMessage("Admin Cannot Edit Project Owner!", HttpStatus.UNAUTHORIZED);
        }
        if (updateDto.getAssigneeProjectRole() == ProjectRoleEnum.owner.getRoleValue())
            return new ErrorMessage("You can't assign a higher privilege level", HttpStatus.UNAUTHORIZED);
        if (updateDto.getAssignerId().equals(userId))
            return new ErrorMessage("You can't assign yourself to this project", HttpStatus.UNAUTHORIZED);
        Project_User assignment = new Project_User();
        assignment.setProjectId(projectId);
        assignment.setAssigneeId(userId);
        assignment.setAssignedAt(utilsService.getCurrentTimestamp());
        assignment.setAssigneeJobRole(updateDto.getAssigneeJobRole());
        assignment.setAssigneeProjectRole(updateDto.getAssigneeProjectRole());

        projectRepository.updateAssigneeProjectRole(assignment);

        return new Response(ResponseMessage.SUCCESS);

    }

    @Override
    public Object removeProjectAssignee(String projectId, String assignee, ProjectUserDeleteDto deleteDto) {
        ProjectUserResponseDto assignerProject = projectRepository.getProjectByIdAndUserId(projectId, deleteDto.getAssignerId());
        if (assignerProject == null)
            return new ErrorMessage(ResponseMessage.NO_RECORD, HttpStatus.NOT_FOUND);
        ProjectUserResponseDto assigneeProject = projectRepository.getProjectByIdAndUserId(projectId, assignee);
        if (assigneeProject == null)
            return new ErrorMessage(ResponseMessage.NO_RECORD, HttpStatus.NOT_FOUND);
        if ( !((assignerProject.getAssigneeProjectRole() == ProjectRoleEnum.admin.getRoleValue()) || (assignerProject.getAssigneeProjectRole() == ProjectRoleEnum.owner.getRoleValue())))
            return new ErrorMessage("User doesn't have Admin privileges", HttpStatus.UNAUTHORIZED);
        if (assignee.equals(deleteDto.getAssignerId()))
            return new ErrorMessage("You can't remove yourself from the project", HttpStatus.UNAUTHORIZED);
        projectRepository.removeProjectAssignee(projectId, assignee);

        return new Response(ResponseMessage.SUCCESS);
    }

    @Override
    public Object flagProject(String userId, String projectId) {
        ProjectUserResponseDto projectUser = projectRepository.getProjectByIdAndUserId(projectId, userId);
        if (projectUser == null)
            return new ErrorMessage(ResponseMessage.USER_NOT_MEMBER, HttpStatus.UNAUTHORIZED);
        if (!(projectUser.getAssigneeProjectRole() == ProjectRoleEnum.owner.getRoleValue()) )
            return new ErrorMessage("You don't have privileges for this operation", HttpStatus.UNAUTHORIZED);
            projectRepository.flagProject(projectId);
            List<Task> taskList = taskRepository.getAllProjectTasksByUser(projectId);
            for(Task task : taskList) {
                taskService.flagProjectTask(userId, projectId, task.getTaskId(), TaskTypeEnum.project);
            }
        return new Response(ResponseMessage.SUCCESS, HttpStatus.OK);
    }

    @Override
    public Object blockOrUnBlockProjectUser(String userId, String projectId, ProjectUserBlockDto projectUserBlockDto) {
        ProjectUserResponseDto executor = projectRepository.getProjectByIdAndUserId(projectId, userId);
        if (executor == null)
            return new ErrorMessage(ResponseMessage.USER_NOT_MEMBER, HttpStatus.UNAUTHORIZED);
        if ( !((executor.getAssigneeProjectRole() == ProjectRoleEnum.admin.getRoleValue()) || (executor.getAssigneeProjectRole() == ProjectRoleEnum.owner.getRoleValue())))
            return new ErrorMessage("User doesn't have Admin privileges", HttpStatus.UNAUTHORIZED);
        if (projectUserBlockDto.getExecutorId().equals(projectUserBlockDto.getBlockedUserId()))
            return new ErrorMessage("You can't block/unblock yourself", HttpStatus.UNAUTHORIZED);
         projectRepository.blockOrUnBlockProjectUser(projectUserBlockDto.getBlockedUserId(), projectId, projectUserBlockDto.getBlockedStatus());

         return new Response(ResponseMessage.SUCCESS, HttpStatus.OK);
    }
}
