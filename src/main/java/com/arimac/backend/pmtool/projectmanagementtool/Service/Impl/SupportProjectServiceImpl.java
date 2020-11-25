package com.arimac.backend.pmtool.projectmanagementtool.Service.Impl;

import com.arimac.backend.pmtool.projectmanagementtool.Response.Response;
import com.arimac.backend.pmtool.projectmanagementtool.Service.InternalSupportService;
import com.arimac.backend.pmtool.projectmanagementtool.Service.SupportProjectService;
import com.arimac.backend.pmtool.projectmanagementtool.dtos.ServiceDesk.SupportUser;
import com.arimac.backend.pmtool.projectmanagementtool.dtos.SupportProject.AddSupportUserDto;
import com.arimac.backend.pmtool.projectmanagementtool.dtos.SupportProject.ServiceTicketStatus;
import com.arimac.backend.pmtool.projectmanagementtool.enumz.ResponseMessage;
import com.arimac.backend.pmtool.projectmanagementtool.exception.ErrorMessage;
import com.arimac.backend.pmtool.projectmanagementtool.model.Organization;
import com.arimac.backend.pmtool.projectmanagementtool.model.Project;
import com.arimac.backend.pmtool.projectmanagementtool.model.User;
import com.arimac.backend.pmtool.projectmanagementtool.repository.OrganizationRepository;
import com.arimac.backend.pmtool.projectmanagementtool.repository.ProjectRepository;
import com.arimac.backend.pmtool.projectmanagementtool.repository.SupportMemberRepository;
import com.arimac.backend.pmtool.projectmanagementtool.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
public class SupportProjectServiceImpl implements SupportProjectService {
    private final UserRepository userRepository;
    private final ProjectRepository projectRepository;
    private final OrganizationRepository organizationRepository;
    private final SupportMemberRepository supportMemberRepository;
    private final InternalSupportService internalSupportService;

    public SupportProjectServiceImpl(UserRepository userRepository, ProjectRepository projectRepository, OrganizationRepository organizationRepository, SupportMemberRepository supportMemberRepository, InternalSupportService internalSupportService) {
        this.userRepository = userRepository;
        this.projectRepository = projectRepository;
        this.organizationRepository = organizationRepository;
        this.supportMemberRepository = supportMemberRepository;
        this.internalSupportService = internalSupportService;
    }

    @Override
    public Object createAdminForSupportProject(String userId, String project, AddSupportUserDto addSupportUserDto) {
        User user = userRepository.getUserByUserId(userId);
        if (user == null)
            return new ErrorMessage(ResponseMessage.USER_NOT_FOUND, HttpStatus.UNAUTHORIZED);
        Organization organization = organizationRepository.getOrganizationById(addSupportUserDto.getOrganizationId());
        if (organization == null)
            return new ErrorMessage(ResponseMessage.ORGANIZATION_NOT_FOUND, HttpStatus.NOT_FOUND);
        if (!organization.isHasSupportProjects())
            return new ErrorMessage(ResponseMessage.SUPPPORT_SERVICE_NOT_ENABLED, HttpStatus.UNPROCESSABLE_ENTITY);
        internalSupportService.createAdminForSupportProject(project, addSupportUserDto, true);
        return new Response(ResponseMessage.SUCCESS, HttpStatus.OK);
    }

    @Override
    public Object getSupportUserByEmail(String userId, String email){
        User user = userRepository.getUserByUserId(userId);
        if (user == null)
            return new ErrorMessage(ResponseMessage.USER_NOT_FOUND, HttpStatus.UNAUTHORIZED);
        SupportUser supportUser = internalSupportService.getSupportUserByEmail(email,true);
        return new Response(ResponseMessage.SUCCESS, supportUser );
    }

    @Override
    public Object getSupportProjects(String userId) {
        User user = userRepository.getUserByUserId(userId);
        if (user == null)
            return new ErrorMessage(ResponseMessage.USER_NOT_FOUND, HttpStatus.UNAUTHORIZED);
        return new Response(ResponseMessage.SUCCESS, HttpStatus.OK,projectRepository.getSupportProjects());
    }

    @Override
    public Object getSupportUsersByOrganization(String userId, String organizationId) {
        User user = userRepository.getUserByUserId(userId);
        if (user == null)
            return new ErrorMessage(ResponseMessage.USER_NOT_FOUND, HttpStatus.UNAUTHORIZED);
        return new Response(ResponseMessage.SUCCESS, HttpStatus.OK, internalSupportService.getSupportUsersByOrganization(organizationId, true));
    }

    @Override
    public Object getSupportUsersByProject(String userId, String projectId) {
        User user = userRepository.getUserByUserId(userId);
        if (user == null)
            return new ErrorMessage(ResponseMessage.USER_NOT_FOUND, HttpStatus.UNAUTHORIZED);
//        TODO Verify Support Disabling
        Project project = projectRepository.getProjectById(projectId);
        if (project == null)
            return new ErrorMessage(ResponseMessage.PROJECT_NOT_FOUND, HttpStatus.NOT_FOUND);
        if (!project.getIsSupportAdded())
            return new ErrorMessage(ResponseMessage.PROJECT_SUPPORT_NOT_ADDED, HttpStatus.UNPROCESSABLE_ENTITY);
        return new Response(ResponseMessage.SUCCESS, HttpStatus.OK, internalSupportService.getSupportUsersByProject(projectId, true));
    }

    @Override
    public Object getSupportTicketStatusByProject(String userId, String projectId) {
        Project project = projectRepository.getProjectById(projectId);
        if (project == null)
            return new ErrorMessage(ResponseMessage.PROJECT_NOT_FOUND, HttpStatus.NOT_FOUND);
        if (!project.getIsSupportAdded())
            return new ErrorMessage(ResponseMessage.PROJECT_SUPPORT_NOT_ADDED, HttpStatus.UNPROCESSABLE_ENTITY);
        ServiceTicketStatus ticketStatus = internalSupportService.getSupportTicketStatusByProject(userId, projectId, true);
        return new Response(ResponseMessage.SUCCESS, HttpStatus.OK, ticketStatus);
    }

    @Override
    public Object getSupportTicketsByProject(String userId, String projectId,  int startIndex, int endIndex) {
        int limit = endIndex - startIndex;
        if (startIndex < 0 || endIndex < 0 || endIndex < startIndex)
            return new ErrorMessage("Invalid Start/End Index", HttpStatus.BAD_REQUEST);
        if (limit > 10)
            return new ErrorMessage(ResponseMessage.REQUEST_ITEM_LIMIT_EXCEEDED, HttpStatus.UNPROCESSABLE_ENTITY);
        Object projectStatus = checkProjectStatus(projectId);
        if (checkProjectStatus(projectId) instanceof ErrorMessage)
            return projectStatus;
        Object tickets = internalSupportService.getSupportTicketsByProject(projectId, startIndex, limit, true);
        return new Response(ResponseMessage.SUCCESS, HttpStatus.OK, tickets);
    }

    private Object checkProjectStatus(String projectId){
        Project project = projectRepository.getProjectById(projectId);
        if (project == null)
            return new ErrorMessage(ResponseMessage.PROJECT_NOT_FOUND, HttpStatus.NOT_FOUND);
        if (!project.getIsSupportAdded())
            return new ErrorMessage(ResponseMessage.PROJECT_SUPPORT_NOT_ADDED, HttpStatus.UNPROCESSABLE_ENTITY);
        return true;
    }

}
