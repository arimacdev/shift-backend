package com.arimac.backend.pmtool.projectmanagementtool.Service;

import com.arimac.backend.pmtool.projectmanagementtool.dtos.ServiceDesk.AddServiceTask;
import com.arimac.backend.pmtool.projectmanagementtool.dtos.ServiceDesk.SupportTaskLink;
import com.arimac.backend.pmtool.projectmanagementtool.dtos.SupportProject.AddSupportUserDto;
import com.arimac.backend.pmtool.projectmanagementtool.dtos.SupportProject.ServiceTicketUpdate;
import com.arimac.backend.pmtool.projectmanagementtool.model.TaskRelationship;
import com.fasterxml.jackson.core.JsonProcessingException;

public interface SupportProjectService {
    Object createAdminForSupportProject(String user, String project, AddSupportUserDto addSupportUserDto);
    Object getSupportUserByEmail(String user, String email);
    Object getSupportProjects(String userId);
    Object getSupportUsersByOrganization(String userId, String organizationId);
    Object getSupportUsersByProject(String userId, String projectId);
    Object getSupportTicketStatusByProject(String userId, String projectId);
    Object getSupportTicketById(String userId, String projectId, String ticketId);
    Object getSupportTicketsByProject(String userId, String projectId, int startIndex, int endIndex);
    Object supportTicketInternalUpdate(String user,String ticketId, ServiceTicketUpdate serviceTicketUpdate);
    Object createTaskFromServiceTicket(String user, String ticketId, AddServiceTask addServiceTask);
    Object getSupportFilesOfSupportTicket(String user, String ticketId, String projectId);
    Object getAssociatedTaskOfTicket(String user, String projectId, String ticketId);
    Object createTaskFromServiceTicket(String user, String ticketId, TaskRelationship taskRelationship);
}
