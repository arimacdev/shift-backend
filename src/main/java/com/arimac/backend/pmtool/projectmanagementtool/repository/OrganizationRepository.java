package com.arimac.backend.pmtool.projectmanagementtool.repository;

import com.arimac.backend.pmtool.projectmanagementtool.model.Organization;

import java.util.List;

public interface OrganizationRepository {
    void addOrganization(Organization organization);
    Organization getOrganizationById(String organizationId);
    List<Organization> getAllOrganizations(int limit, int offset);
    void updateOrganization(Organization organization);
}
