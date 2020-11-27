package com.arimac.backend.pmtool.projectmanagementtool.Service;

import com.arimac.backend.pmtool.projectmanagementtool.dtos.ServiceDesk.SupportMemberResponse;
import com.arimac.backend.pmtool.projectmanagementtool.dtos.ServiceDesk.SupportTicketFile;
import com.arimac.backend.pmtool.projectmanagementtool.dtos.ServiceDesk.SupportUser;
import com.arimac.backend.pmtool.projectmanagementtool.dtos.SupportProject.*;


import java.util.List;

public interface InternalSupportService {
    void createSupportProject(CreateSupportProject createSupportProject, boolean firstRequest);
    void updateSupportProject(UpdateStatus updateStatus, boolean firstRequest);
    Object createAdminForSupportProject(String project, AddSupportUserDto addSupportUserDto, boolean firstRequest);
    SupportUser getSupportUserByEmail(String email, boolean firstRequest);
    List<SupportUser> getSupportUsersByOrganization(String organization, boolean firstRequest);
    List<SupportMemberResponse> getSupportUsersByProject(String projectId, boolean firstRequest);
    Object getSupportTicketById(String projectId, String ticketId, boolean firstRequest);
    ServiceTicketStatus getSupportTicketStatusByProject(String userId, String projectId, boolean firstRequest);
    List<ServiceTicketUser> getSupportTicketsByProject(String projectId, int startIndex, int limit, boolean firstRequest);
    void supportTicketInternalUpdate(String ticketId, ServiceTicketUpdate serviceTicketUpdate, boolean firstRequest);
    List<SupportTicketFile> getFilesOfSupportTicket(String projectId, String ticketId, boolean firstRequest);
}
