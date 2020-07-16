package com.arimac.backend.pmtool.projectmanagementtool.Service.Impl;

import com.arimac.backend.pmtool.projectmanagementtool.Response.Response;
import com.arimac.backend.pmtool.projectmanagementtool.Service.ActivityLogService;
import com.arimac.backend.pmtool.projectmanagementtool.Service.ProjectService;
import com.arimac.backend.pmtool.projectmanagementtool.Service.TaskService;
import com.arimac.backend.pmtool.projectmanagementtool.dtos.*;
import com.arimac.backend.pmtool.projectmanagementtool.enumz.ActivityLog.LogOperationEnum;
import com.arimac.backend.pmtool.projectmanagementtool.enumz.ActivityLog.ProjectUpdateTypeEnum;
import com.arimac.backend.pmtool.projectmanagementtool.enumz.ActivityLog.TaskUpdateTypeEnum;
import com.arimac.backend.pmtool.projectmanagementtool.enumz.ProjectRoleEnum;
import com.arimac.backend.pmtool.projectmanagementtool.enumz.ProjectStatusEnum;
import com.arimac.backend.pmtool.projectmanagementtool.enumz.ResponseMessage;
import com.arimac.backend.pmtool.projectmanagementtool.exception.ErrorMessage;
import com.arimac.backend.pmtool.projectmanagementtool.model.*;
import com.arimac.backend.pmtool.projectmanagementtool.repository.*;
import com.arimac.backend.pmtool.projectmanagementtool.utils.UtilsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.List;

@Service
public class ProjectServiceImpl implements ProjectService {

    private static final Logger logger = LoggerFactory.getLogger(ProjectServiceImpl.class);

    private static final int ISSUE_START = 99;
    private static final String OWNER = "Owner";

    private final TaskService taskService;
    private final ActivityLogService activityLogService;
    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;
    private final TaskRepository taskRepository;
    private final UtilsService utilsService;

    public ProjectServiceImpl(TaskService taskService, ActivityLogService activityLogService, ProjectRepository projectRepository, UserRepository userRepository, TaskRepository taskRepository, UtilsService utilsService) {
        this.taskService = taskService;
        this.activityLogService = activityLogService;
        this.projectRepository = projectRepository;
        this.userRepository = userRepository;
        this.taskRepository = taskRepository;
        this.utilsService = utilsService;
    }

    @Override
    public Object createProject(ProjectDto projectDto) {
        if ( (projectDto.getProjectAlias() == null || projectDto.getProjectAlias().isEmpty()) || (projectDto.getProjectName() == null || projectDto.getProjectName().isEmpty()) )
            return new ErrorMessage(ResponseMessage.INVALID_REQUEST_BODY, HttpStatus.BAD_REQUEST);
        User user = userRepository.getUserByUserId(projectDto.getProjectOwner());
        if (user == null)
            return new ErrorMessage(ResponseMessage.USER_NOT_FOUND, HttpStatus.NOT_FOUND);
        boolean checkAlias = projectRepository.checkProjectAlias(projectDto.getProjectAlias());
        if (checkAlias){
            return new ErrorMessage(ResponseMessage.PROJECT_ALIAS_EXIST, HttpStatus.UNPROCESSABLE_ENTITY);
        }
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
        project.setIssueCount(ISSUE_START);
        project.setWeightMeasure(projectDto.getWeightType().getWeightId());
        projectRepository.createProject(project);
        activityLogService.addTaskLog(utilsService.addProjectAddorFlagLog(LogOperationEnum.CREATE, projectDto.getProjectOwner(), projectId));

        Project_User assignment = new Project_User();
        assignment.setProjectId(projectId);
        assignment.setAssigneeId(projectDto.getProjectOwner());
        assignment.setAssignedAt(utilsService.getCurrentTimestamp());
        assignment.setAssigneeJobRole(OWNER);
        assignment.setAssigneeProjectRole(ProjectRoleEnum.owner.getRoleValue());

        projectRepository.assignUserToProject(projectId,assignment);

        return new Response(ResponseMessage.SUCCESS, project);
    }

    @Override
    public Object getAllProjects(String userId) {
        //TODO check role of a user
        User user = userRepository.getUserByUserId(userId);
        if (user == null)
            return new ErrorMessage(ResponseMessage.USER_NOT_FOUND, HttpStatus.NOT_FOUND);
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
        User user = userRepository.getUserByUserId(userId);
        if (user == null)
            return new ErrorMessage(ResponseMessage.USER_NOT_FOUND, HttpStatus.NOT_FOUND);
        ProjectUserResponseDto userProject = projectRepository.getProjectByIdAndUserId(projectId, userId);
        if (userProject == null)
            return new ErrorMessage(ResponseMessage.USER_NOT_FOUND, HttpStatus.NOT_FOUND);
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
//        ProjectUserResponseDto assigneeProject = projectRepository.getProjectByIdAndUserId(projectId, userAssignDto.getAssigneeId()); // Check project assignee is vacant
//        if (assigneeProject != null)
//            return new ErrorMessage(ResponseMessage.ALREADY_ASSIGNED, HttpStatus.UNAUTHORIZED);
        Project_User project_user = projectRepository.getProjectUser(projectId, userAssignDto.getAssigneeId());
//        if (project_user != null && !project_user.getIsBlocked())
//            return new ErrorMessage(ResponseMessage.ALREADY_ASSIGNED, HttpStatus.UNPROCESSABLE_ENTITY);

        if (project_user == null) {
            Project_User assignment = new Project_User();
            assignment.setProjectId(projectId);
            assignment.setAssigneeId(userAssignDto.getAssigneeId());
            assignment.setAssignedAt(utilsService.getCurrentTimestamp());
            assignment.setAssigneeJobRole(userAssignDto.getAssigneeJobRole());
            assignment.setAssigneeProjectRole(userAssignDto.getAssigneeProjectRole());
            assignment.setIsBlocked(false);

            projectRepository.assignUserToProject(projectId,assignment);
            activityLogService.addTaskLog(utilsService.addProjectUpdateLog(LogOperationEnum.UPDATE, userAssignDto.getAssignerId(), projectId, ProjectUpdateTypeEnum.ADD_USER, null, userAssignDto.getAssigneeId()));

        } else if (project_user.getIsBlocked()){
                projectRepository.blockOrUnBlockProjectUser(userAssignDto.getAssigneeId(), projectId, false);
            activityLogService.addTaskLog(utilsService.addProjectUpdateLog(LogOperationEnum.UPDATE, userAssignDto.getAssignerId(), projectId, ProjectUpdateTypeEnum.ADD_USER, null, userAssignDto.getAssigneeId()));

        } else if (!project_user.getIsBlocked()){
            return new ErrorMessage(ResponseMessage.ALREADY_ASSIGNED, HttpStatus.UNPROCESSABLE_ENTITY);
        }

       return new Response(ResponseMessage.SUCCESS, HttpStatus.OK);

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
            activityLogService.addTaskLog(utilsService.addProjectUpdateLog(LogOperationEnum.UPDATE, projectEditDto.getModifierId(), projectId, ProjectUpdateTypeEnum.PROJECT_NAME, modifierProject.getProjectName(), projectEditDto.getProjectName()));
        } else {
            updatedProject.setProjectName(modifierProject.getProjectName());
        }
        if (projectEditDto.getClientId() != null && !projectEditDto.getClientId().isEmpty()){
            activityLogService.addTaskLog(utilsService.addProjectUpdateLog(LogOperationEnum.UPDATE, projectEditDto.getModifierId(), projectId, ProjectUpdateTypeEnum.CLIENT, modifierProject.getClientId(), projectEditDto.getClientId()));
            updatedProject.setClientId(projectEditDto.getClientId());
        } else {
            updatedProject.setClientId(modifierProject.getClientId());
        }
        if (projectEditDto.getProjectStatus() != null && !projectEditDto.getProjectStatus().isEmpty()){
            activityLogService.addTaskLog(utilsService.addProjectUpdateLog(LogOperationEnum.UPDATE, projectEditDto.getModifierId(), projectId, ProjectUpdateTypeEnum.STATUS, modifierProject.getProjectStatus(), projectEditDto.getProjectStatus()));
            updatedProject.setProjectStatus(ProjectStatusEnum.valueOf(projectEditDto.getProjectStatus()));
        } else {
            updatedProject.setProjectStatus(ProjectStatusEnum.valueOf(modifierProject.getProjectStatus()));
        }
        if (projectEditDto.getProjectStartDate() != null){
            String previousDate = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS").format(modifierProject.getProjectStartDate());
            String updatedDate = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS").format(projectEditDto.getProjectEndDate());
            activityLogService.addTaskLog(utilsService.addProjectUpdateLog(LogOperationEnum.UPDATE, projectEditDto.getModifierId(), projectId, ProjectUpdateTypeEnum.START_DATE, previousDate, updatedDate));
            updatedProject.setProjectStartDate(projectEditDto.getProjectStartDate());
        } else {
            updatedProject.setProjectStartDate(modifierProject.getProjectStartDate());
        }
        if (projectEditDto.getProjectEndDate() != null){
            String previousDate = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS").format(modifierProject.getProjectEndDate());
            String updatedDate = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS").format(projectEditDto.getProjectEndDate());
            activityLogService.addTaskLog(utilsService.addProjectUpdateLog(LogOperationEnum.UPDATE, projectEditDto.getModifierId(), projectId, ProjectUpdateTypeEnum.END_DATE, previousDate, updatedDate));
            updatedProject.setProjectEndDate(projectEditDto.getProjectEndDate());
        } else {
            updatedProject.setProjectEndDate(modifierProject.getProjectEndDate());
        }
        if (projectEditDto.getProjectAlias() != null && !projectEditDto.getProjectAlias().isEmpty()){
            if (projectRepository.checkProjectAlias(projectEditDto.getProjectAlias()))
                return new ErrorMessage(ResponseMessage.PROJECT_ALIAS_EXIST, HttpStatus.CONFLICT);
            activityLogService.addTaskLog(utilsService.addProjectUpdateLog(LogOperationEnum.UPDATE, projectEditDto.getModifierId(), projectId, ProjectUpdateTypeEnum.PROJECT_ALIAS, modifierProject.getProjectAlias(), projectEditDto.getProjectAlias()));
            updatedProject.setProjectAlias(projectEditDto.getProjectAlias());
        } else {
            updatedProject.setProjectAlias(modifierProject.getProjectAlias());
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
       // activityLogService.addTaskLog(utilsService.addProjectUpdateLog(LogOperationEnum.UPDATE, updateDto.getAssignerId(), projectId, ProjectUpdateTypeEnum.ROLE_UPDATE, String.valueOf(assigneeProject.getAssigneeProjectRole()), String.valueOf(updateDto.getAssigneeProjectRole())));
        return new Response(ResponseMessage.SUCCESS, HttpStatus.OK);

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
        activityLogService.addTaskLog(utilsService.addProjectUpdateLog(LogOperationEnum.UPDATE, deleteDto.getAssignerId(), projectId, ProjectUpdateTypeEnum.REMOVE_USER, assignee, null));

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
                taskService.flagProjectTask(userId, projectId, task.getTaskId());
            }
        activityLogService.addTaskLog(utilsService.addProjectAddorFlagLog(LogOperationEnum.FLAG, userId, projectId));
        activityLogService.flagEntityActivityLogs(projectId);
        return new Response(ResponseMessage.SUCCESS, HttpStatus.OK);
    }

    @Override
    public Object blockOrUnBlockProjectUser(String userId, String projectId, ProjectUserBlockDto projectUserBlockDto) {
        ProjectUserResponseDto executor = projectRepository.getProjectByIdAndUserId(projectId, userId);
        if (executor == null)
            return new ErrorMessage(ResponseMessage.USER_NOT_MEMBER, HttpStatus.NOT_FOUND);
         ProjectUserResponseDto userTobeBlocked = projectRepository.getProjectByIdAndUserId(projectId, projectUserBlockDto.getBlockedUserId());
        if (userTobeBlocked == null)
            return new ErrorMessage(ResponseMessage.USER_NOT_MEMBER, HttpStatus.NOT_FOUND);
        if (userTobeBlocked.getAssigneeProjectRole() == ProjectRoleEnum.owner.getRoleValue())
            return new ErrorMessage(ResponseMessage.UNAUTHORIZED, HttpStatus.UNAUTHORIZED);
        if ( !((executor.getAssigneeProjectRole() == ProjectRoleEnum.admin.getRoleValue()) || (executor.getAssigneeProjectRole() == ProjectRoleEnum.owner.getRoleValue())))
            return new ErrorMessage("User doesn't have Admin privileges", HttpStatus.UNAUTHORIZED);
        if (projectUserBlockDto.getExecutorId().equals(projectUserBlockDto.getBlockedUserId()))
            return new ErrorMessage("You can't block/unblock yourself", HttpStatus.UNAUTHORIZED);
         projectRepository.blockOrUnBlockProjectUser(projectUserBlockDto.getBlockedUserId(), projectId, projectUserBlockDto.getBlockedStatus());
        if (projectUserBlockDto.getBlockedStatus())
            activityLogService.addTaskLog(utilsService.addProjectUpdateLog(LogOperationEnum.UPDATE, userId, projectId, ProjectUpdateTypeEnum.REMOVE_USER, projectUserBlockDto.getBlockedUserId(), null));
        else
            activityLogService.addTaskLog(utilsService.addProjectUpdateLog(LogOperationEnum.UPDATE, userId, projectId, ProjectUpdateTypeEnum.ADD_USER, null, projectUserBlockDto.getBlockedUserId()));
        return new Response(ResponseMessage.SUCCESS, HttpStatus.OK);
    }
}
