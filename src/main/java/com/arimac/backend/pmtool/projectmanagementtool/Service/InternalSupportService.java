package com.arimac.backend.pmtool.projectmanagementtool.Service;

import com.arimac.backend.pmtool.projectmanagementtool.dtos.ServiceDesk.SupportUser;
import com.arimac.backend.pmtool.projectmanagementtool.dtos.SupportProject.AddSupportUserDto;
import com.arimac.backend.pmtool.projectmanagementtool.dtos.SupportProject.CreateSupportProject;
import com.arimac.backend.pmtool.projectmanagementtool.dtos.SupportProject.UpdateStatus;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.json.JSONObject;

public interface InternalSupportService {
    void createSupportProject(CreateSupportProject createSupportProject);
    void updateSupportProject(UpdateStatus updateStatus);
    Object createAdminForSupportProject(String project, AddSupportUserDto addSupportUserDto);
    SupportUser getSupportUserByEmail(String email);
}
