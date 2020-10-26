package com.arimac.backend.pmtool.projectmanagementtool.Service;

import com.arimac.backend.pmtool.projectmanagementtool.dtos.SupportProject.AddSupportUserDto;
import com.fasterxml.jackson.core.JsonProcessingException;

public interface SupportProjectService {
    Object createAdminForSupportProject(String user, String project, AddSupportUserDto addSupportUserDto);
    Object getSupportUserByEmail(String user, String email);
    Object getSupportProjects(String userId);
}
