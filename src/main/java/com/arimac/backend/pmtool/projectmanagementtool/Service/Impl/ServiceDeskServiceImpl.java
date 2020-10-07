package com.arimac.backend.pmtool.projectmanagementtool.Service.Impl;

import com.arimac.backend.pmtool.projectmanagementtool.Response.Response;
import com.arimac.backend.pmtool.projectmanagementtool.Service.ServiceDeskService;
import com.arimac.backend.pmtool.projectmanagementtool.dtos.Project.ProjectKeys;
import com.arimac.backend.pmtool.projectmanagementtool.dtos.ServiceDesk.AddTicket;
import com.arimac.backend.pmtool.projectmanagementtool.dtos.ServiceDesk.RequestKey;
import com.arimac.backend.pmtool.projectmanagementtool.enumz.ResponseMessage;
import com.arimac.backend.pmtool.projectmanagementtool.enumz.ServiceDesk.TicketStatusEnum;
import com.arimac.backend.pmtool.projectmanagementtool.exception.ErrorMessage;
import com.arimac.backend.pmtool.projectmanagementtool.model.Project;
import com.arimac.backend.pmtool.projectmanagementtool.model.Project_Keys;
import com.arimac.backend.pmtool.projectmanagementtool.model.ServiceTicket;
import com.arimac.backend.pmtool.projectmanagementtool.repository.ProjectRepository;
import com.arimac.backend.pmtool.projectmanagementtool.repository.ServiceDeskRepository;
import com.arimac.backend.pmtool.projectmanagementtool.utils.UtilsService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
public class ServiceDeskServiceImpl implements ServiceDeskService {

    private final ServiceDeskRepository serviceDeskRepository;
    private final ProjectRepository projectRepository;
    private final UtilsService utilsService;

    public ServiceDeskServiceImpl(ServiceDeskRepository serviceDeskRepository, ProjectRepository projectRepository, UtilsService utilsService) {
        this.serviceDeskRepository = serviceDeskRepository;
        this.projectRepository = projectRepository;
        this.utilsService = utilsService;
    }

    @Override
    public Object clientValidation(RequestKey requestKey) {
        String domain = requestKey.getEmail().split("@")[1];
        Project_Keys project_keys = projectRepository.getProjectKeyByDomain(requestKey.getProjectKey(), domain);
        if (project_keys == null)
            return new ErrorMessage(ResponseMessage.INVALID_KEY_COMBINAITON, HttpStatus.UNAUTHORIZED);
        Project project = projectRepository.getProjectById(project_keys.getProjectId());
        if (project == null)
            return new ErrorMessage(ResponseMessage.PROJECT_NOT_FOUND, HttpStatus.NOT_FOUND);
        if (!project.getIsSupportEnabled())
            return new ErrorMessage(ResponseMessage.SUPPPORT_SERVICE_NOT_ENABLED, HttpStatus.UNPROCESSABLE_ENTITY);
        return new Response(ResponseMessage.SUCCESS, HttpStatus.OK, project);
    }

    @Override
    public Object createSupportTicket(AddTicket addTicket) {
        Project_Keys project_keys = projectRepository.getProjectKey(addTicket.getProjectKey());
        if (project_keys == null)
            return new ErrorMessage(ResponseMessage.INVALID_KEY_COMBINAITON, HttpStatus.NOT_FOUND);
        String domain = addTicket.getEmail().split("@")[1];
        if (!project_keys.getDomain().equals(domain))
            return new ErrorMessage(ResponseMessage.INVALID_KEY_COMBINAITON, HttpStatus.NOT_FOUND);
        Project project = projectRepository.getProjectById(project_keys.getProjectId());
        if (project == null)
            return new ErrorMessage(ResponseMessage.PROJECT_NOT_FOUND, HttpStatus.NOT_FOUND);
        if (!project.getIsSupportEnabled())
            return new ErrorMessage(ResponseMessage.SUPPPORT_SERVICE_NOT_ENABLED, HttpStatus.UNPROCESSABLE_ENTITY);
        ServiceTicket serviceTicket = new ServiceTicket();
        serviceTicket.setTicketId(utilsService.getUUId());
        serviceTicket.setDescription(addTicket.getIssueTopic());
        serviceTicket.setProjectId(project_keys.getProjectId());
        serviceTicket.setReporter(addTicket.getEmail());
        serviceTicket.setTicketStatus(TicketStatusEnum.PENDING.getStatusId());
        serviceTicket.setSeverity(addTicket.getSeverity());
        serviceTicket.setTicketCreation(utilsService.getCurrentTimestamp());

        serviceDeskRepository.addServiceTicket(serviceTicket);
        return new Response(ResponseMessage.SUCCESS, HttpStatus.OK);
    }

    @Override
    public Object requestNewServiceKey(RequestKey requestKey) {
        String domain = requestKey.getEmail().split("@")[1];
        Project_Keys project_keys = projectRepository.getProjectKeyByDomain(requestKey.getProjectKey(), domain);
        if (project_keys == null)
            return new ErrorMessage(ResponseMessage.INVALID_KEY_COMBINAITON, HttpStatus.NOT_FOUND);
        Project project = projectRepository.getProjectById(project_keys.getProjectId());
        if (project == null)
            return new ErrorMessage(ResponseMessage.PROJECT_NOT_FOUND, HttpStatus.NOT_FOUND);
        if (!project.getIsSupportEnabled())
            return new ErrorMessage(ResponseMessage.SUPPPORT_SERVICE_NOT_ENABLED, HttpStatus.UNPROCESSABLE_ENTITY);
        projectRepository.updateProjectKeys(new ProjectKeys(requestKey.getProjectKey(), domain, false));
        //Email Sending
        return new Response(ResponseMessage.SUCCESS, HttpStatus.OK);
    }
}
