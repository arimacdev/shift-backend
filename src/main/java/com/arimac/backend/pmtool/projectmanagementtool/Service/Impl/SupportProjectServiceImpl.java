package com.arimac.backend.pmtool.projectmanagementtool.Service.Impl;

import com.arimac.backend.pmtool.projectmanagementtool.Response.Response;
import com.arimac.backend.pmtool.projectmanagementtool.Service.InternalSupportService;
import com.arimac.backend.pmtool.projectmanagementtool.Service.SupportProjectService;
import com.arimac.backend.pmtool.projectmanagementtool.dtos.ServiceDesk.SupportUser;
import com.arimac.backend.pmtool.projectmanagementtool.dtos.SupportProject.AddSupportUserDto;
import com.arimac.backend.pmtool.projectmanagementtool.enumz.ResponseMessage;
import com.arimac.backend.pmtool.projectmanagementtool.exception.ErrorMessage;
import com.arimac.backend.pmtool.projectmanagementtool.model.Organization;
import com.arimac.backend.pmtool.projectmanagementtool.model.User;
import com.arimac.backend.pmtool.projectmanagementtool.repository.OrganizationRepository;
import com.arimac.backend.pmtool.projectmanagementtool.repository.ProjectRepository;
import com.arimac.backend.pmtool.projectmanagementtool.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
public class SupportProjectServiceImpl implements SupportProjectService {
    private final UserRepository userRepository;
    private final ProjectRepository projectRepository;
    private final OrganizationRepository organizationRepository;
    private final InternalSupportService internalSupportService;

    public SupportProjectServiceImpl(UserRepository userRepository, ProjectRepository projectRepository, OrganizationRepository organizationRepository, InternalSupportService internalSupportService) {
        this.userRepository = userRepository;
        this.projectRepository = projectRepository;
        this.organizationRepository = organizationRepository;
        this.internalSupportService = internalSupportService;
    }

    @Override
    public Object createAdminForSupportProject(String userId, String project, AddSupportUserDto addSupportUserDto) {
        User user = userRepository.getUserByUserId(userId);
        if (user == null)
            return new ErrorMessage(ResponseMessage.USER_NOT_FOUND, HttpStatus.UNAUTHORIZED);
        Organization organization = organizationRepository.getOrganizationById(addSupportUserDto.getClientId());
        if (organization == null)
            return new ErrorMessage(ResponseMessage.ORGANIZATION_NOT_FOUND, HttpStatus.NOT_FOUND);
        if (!organization.isHasSupportProjects())
            return new ErrorMessage(ResponseMessage.SUPPPORT_SERVICE_NOT_ENABLED, HttpStatus.UNPROCESSABLE_ENTITY);
        internalSupportService.createAdminForSupportProject(project, addSupportUserDto);
        return new Response(ResponseMessage.SUCCESS, HttpStatus.OK);
    }

    @Override
    public Object getSupportUserByEmail(String userId, String email){
        User user = userRepository.getUserByUserId(userId);
        if (user == null)
            return new ErrorMessage(ResponseMessage.USER_NOT_FOUND, HttpStatus.UNAUTHORIZED);
        SupportUser supportUser = internalSupportService.getSupportUserByEmail(email);
        return new Response(ResponseMessage.SUCCESS, supportUser );
    }

    @Override
    public Object getSupportProjects(String userId) {
        User user = userRepository.getUserByUserId(userId);
        if (user == null)
            return new ErrorMessage(ResponseMessage.USER_NOT_FOUND, HttpStatus.UNAUTHORIZED);
        return new Response(ResponseMessage.SUCCESS, HttpStatus.OK,projectRepository.getSupportProjects());
    }
}
