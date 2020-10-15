package com.arimac.backend.pmtool.projectmanagementtool.Service;

import com.arimac.backend.pmtool.projectmanagementtool.dtos.Organization.AddOrganization;

public interface OrganizationService {
    Object addOrganization(String userId, AddOrganization addOrganization);
}
