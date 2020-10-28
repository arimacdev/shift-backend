package com.arimac.backend.pmtool.projectmanagementtool.Service;

import com.arimac.backend.pmtool.projectmanagementtool.dtos.ServiceDesk.SupportMemberResponse;
import com.arimac.backend.pmtool.projectmanagementtool.dtos.ServiceDesk.SupportUser;
import com.arimac.backend.pmtool.projectmanagementtool.dtos.SupportProject.AddSupportUserDto;
import com.arimac.backend.pmtool.projectmanagementtool.dtos.SupportProject.CreateSupportProject;
import com.arimac.backend.pmtool.projectmanagementtool.dtos.SupportProject.UpdateStatus;


import java.util.List;

public interface InternalSupportService {
    void createSupportProject(CreateSupportProject createSupportProject);
    void updateSupportProject(UpdateStatus updateStatus);
    Object createAdminForSupportProject(String project, AddSupportUserDto addSupportUserDto);
    SupportUser getSupportUserByEmail(String email);
    List<SupportUser> getSupportUsersByOrganization(String organization);
    List<SupportMemberResponse> getSupportUsersByProject(String projectId);
}
