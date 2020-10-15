package com.arimac.backend.pmtool.projectmanagementtool.repository;

import com.arimac.backend.pmtool.projectmanagementtool.model.Organization;

public interface OrganizationRepository {
    void addOrganization(Organization organization);
    Organization getOrganizationById(String organizationId);
    void updateOrganization(Organization organization);
}
