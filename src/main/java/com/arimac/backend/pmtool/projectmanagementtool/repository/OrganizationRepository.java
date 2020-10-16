package com.arimac.backend.pmtool.projectmanagementtool.repository;

import com.arimac.backend.pmtool.projectmanagementtool.model.Organization;
import com.arimac.backend.pmtool.projectmanagementtool.model.Project;

import java.util.List;

public interface OrganizationRepository {
    void addOrganization(Organization organization);
    Organization getOrganizationById(String organizationId);
    List<Organization> getAllOrganizations(int limit, int offset);
    List<Project> getProjectsOfOrganization(String organizationId);
    void updateOrganization(Organization organization);
    void flagOrganization(String organizationId);
}
