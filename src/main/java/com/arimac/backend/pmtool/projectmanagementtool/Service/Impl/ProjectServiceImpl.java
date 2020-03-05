package com.arimac.backend.pmtool.projectmanagementtool.Service.Impl;

import com.arimac.backend.pmtool.projectmanagementtool.Response.Response;
import com.arimac.backend.pmtool.projectmanagementtool.Service.ProjectService;
import com.arimac.backend.pmtool.projectmanagementtool.dtos.ProjectDto;
import com.arimac.backend.pmtool.projectmanagementtool.dtos.ProjectUserResponseDto;
import com.arimac.backend.pmtool.projectmanagementtool.dtos.UserAssignDto;
import com.arimac.backend.pmtool.projectmanagementtool.enumz.ProjectStatusEnum;
import com.arimac.backend.pmtool.projectmanagementtool.enumz.ResponseMessage;
import com.arimac.backend.pmtool.projectmanagementtool.exception.ErrorMessage;
import com.arimac.backend.pmtool.projectmanagementtool.model.Project;
import com.arimac.backend.pmtool.projectmanagementtool.model.Project_User;
import com.arimac.backend.pmtool.projectmanagementtool.repository.ProjectRepository;
import com.arimac.backend.pmtool.projectmanagementtool.utils.UtilsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProjectServiceImpl implements ProjectService {

    private static final Logger logger = LoggerFactory.getLogger(ProjectServiceImpl.class);

    private final ProjectRepository projectRepository;
    private final UtilsService utilsService;

    public ProjectServiceImpl(ProjectRepository projectRepository, UtilsService utilsService) {
        this.projectRepository = projectRepository;
        this.utilsService = utilsService;
    }

    @Override
    public Object createProject(ProjectDto projectDto) {
        Project project = new Project();

        //TODO check role of user

        project.setProjectId(utilsService.getUUId());
        project.setProjectName(projectDto.getProjectName());
        project.setClientId(projectDto.getClientId());
        project.setProjectStartDate(projectDto.getProjectStartDate());
        project.setProjectEndDate(projectDto.getProjectEndDate());
        project.setProjectStatus(ProjectStatusEnum.presales.toString());
        projectRepository.createProject(project);

        return new Response(ResponseMessage.SUCCESS, project);
    }

    @Override
    public Object getAllProjects(String userId) {
        //TODO check role of a user

        List<ProjectUserResponseDto> projectList;

        projectList = projectRepository.getAllProjectsByUser(userId);
//        projectList = transactionRepository.getAllProjects();

        if (projectList.isEmpty())
            return new ErrorMessage(ResponseMessage.NO_RECORD, HttpStatus.BAD_REQUEST,projectList);
//            return new Response(ResponseMessage.NO_RECORD, HttpStatus.BAD_REQUEST, projectList);
        return new Response(ResponseMessage.SUCCESS, projectList);

    }

    @Override
    public Object assignUserToProject(String projectId, UserAssignDto userAssignDto) {
        //TODO check admin role

        ProjectUserResponseDto assignerProject = projectRepository.getProjectByIdAndUserId(projectId, userAssignDto.getAssignerId()); // Check project assigner is assigned & isAdmin
        if (assignerProject == null)
            return new ErrorMessage(ResponseMessage.NO_RECORD, HttpStatus.BAD_REQUEST);
        if (!assignerProject.isAdmin())
            return new ErrorMessage("Assigner doesn't have Admin privileges", HttpStatus.FORBIDDEN);
        if (userAssignDto.getAssignerId().equals(userAssignDto.getAssigneeId()))
            return new ErrorMessage("You can't assign yourself to this project", HttpStatus.BAD_REQUEST);
        ProjectUserResponseDto assigneeProject = projectRepository.getProjectByIdAndUserId(projectId, userAssignDto.getAssigneeId()); // Check project assignee is vacant
        if (assigneeProject != null)
            return new ErrorMessage(ResponseMessage.ALREADY_ASSIGNED, HttpStatus.BAD_REQUEST);

        Project_User assignment = new Project_User();
        assignment.setProjectId(projectId);
        assignment.setAssigneeId(userAssignDto.getAssigneeId());
        assignment.setAssignedAt(utilsService.getCurrentTimestamp());
        assignment.setIsAdmin(userAssignDto.getAdmin());
        assignment.setAssigneeProjectRole(userAssignDto.getAssigneeProjectRole());

        projectRepository.assignUserToProject(projectId,assignment);

       return new Response(ResponseMessage.SUCCESS);

    }
}
