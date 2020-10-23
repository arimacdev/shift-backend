package com.arimac.backend.pmtool.projectmanagementtool.Service;

import com.arimac.backend.pmtool.projectmanagementtool.dtos.SupportProject.AddSupportUserDto;

public interface SupportProjectService {
    Object createAdminForSupportProject(String user, String project, AddSupportUserDto addSupportUserDto);
}
