package com.arimac.backend.pmtool.projectmanagementtool.Service;

import com.arimac.backend.pmtool.projectmanagementtool.dtos.SupportProject.AddSupportUserDto;
import com.arimac.backend.pmtool.projectmanagementtool.dtos.SupportProject.CreateSupportProject;
import com.arimac.backend.pmtool.projectmanagementtool.dtos.SupportProject.UpdateStatus;

public interface InternalSupportService {
    void createSupportProject(CreateSupportProject createSupportProject);
    void updateSupportProject(UpdateStatus updateStatus);
    Object createAdminForSupportProject(String project, AddSupportUserDto addSupportUserDto);
}
