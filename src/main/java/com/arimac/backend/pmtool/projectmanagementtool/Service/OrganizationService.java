package com.arimac.backend.pmtool.projectmanagementtool.Service;

import com.arimac.backend.pmtool.projectmanagementtool.dtos.Organization.AddOrganization;
import com.arimac.backend.pmtool.projectmanagementtool.dtos.Organization.UpdateOrganization;

public interface OrganizationService {
    Object addOrganization(String userId, AddOrganization addOrganization);
    Object getAllOrganizations(String userId, int startIndex, int endIndex);
    Object updateOrganization(String userId, String organizationId, UpdateOrganization updateOrganization);
}
