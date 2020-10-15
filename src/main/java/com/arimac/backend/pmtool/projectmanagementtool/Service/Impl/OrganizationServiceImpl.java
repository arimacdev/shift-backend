package com.arimac.backend.pmtool.projectmanagementtool.Service.Impl;

import com.arimac.backend.pmtool.projectmanagementtool.Response.Response;
import com.arimac.backend.pmtool.projectmanagementtool.Service.OrganizationService;
import com.arimac.backend.pmtool.projectmanagementtool.dtos.Organization.AddOrganization;
import com.arimac.backend.pmtool.projectmanagementtool.dtos.Organization.UpdateOrganization;
import com.arimac.backend.pmtool.projectmanagementtool.enumz.ResponseMessage;
import com.arimac.backend.pmtool.projectmanagementtool.exception.ErrorMessage;
import com.arimac.backend.pmtool.projectmanagementtool.model.Organization;
import com.arimac.backend.pmtool.projectmanagementtool.model.User;
import com.arimac.backend.pmtool.projectmanagementtool.repository.OrganizationRepository;
import com.arimac.backend.pmtool.projectmanagementtool.repository.UserRepository;
import com.arimac.backend.pmtool.projectmanagementtool.utils.UtilsService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
public class OrganizationServiceImpl implements OrganizationService {

    private final OrganizationRepository organizationRepository;
    private final UserRepository userRepository;
    private final UtilsService utilsService;

    public OrganizationServiceImpl(OrganizationRepository organizationRepository, UserRepository userRepository, UtilsService utilsService) {
        this.organizationRepository = organizationRepository;
        this.userRepository = userRepository;
        this.utilsService = utilsService;
    }

    @Override
    public Object addOrganization(String userId, AddOrganization addOrganization) {
        User user = userRepository.getUserByUserId(userId);
        if (user == null)
            return new ErrorMessage(ResponseMessage.USER_NOT_FOUND, HttpStatus.NOT_FOUND);
        Organization organization = new Organization();
        organization.setOrganizationId(utilsService.getUUId());
        organization.setOrganizationName(addOrganization.getOrganizationName());
        organization.setCountry(addOrganization.getCountry());
        organization.setOrganizationLogo(addOrganization.getOrganizationLogo());
        organization.setCreatedBy(userId);
        organization.setCreatedAt(utilsService.getCurrentTimestamp());

        organizationRepository.addOrganization(organization);
        return new Response(ResponseMessage.SUCCESS, HttpStatus.OK);
    }

    @Override
    public Object updateOrganization(String userId, String organizationId, UpdateOrganization updateOrganization) {
        User user = userRepository.getUserByUserId(userId);
        if (user == null)
            return new ErrorMessage(ResponseMessage.USER_NOT_FOUND, HttpStatus.NOT_FOUND);
        Organization organization = organizationRepository.getOrganizationById(organizationId);
        if (organization == null)
            return new ErrorMessage(ResponseMessage.ORGANIZATION_NOT_FOUND, HttpStatus.NOT_FOUND);
        if (!updateOrganization.getOrganizationName().isEmpty() || updateOrganization.getOrganizationName() != null)
            organization.setOrganizationName(updateOrganization.getOrganizationName());
        if (!updateOrganization.getCountry().isEmpty() || updateOrganization.getCountry() != null)
            organization.setCountry(updateOrganization.getCountry());
        if (!updateOrganization.getOrganizationLogo().isEmpty() || updateOrganization.getOrganizationLogo() != null)
            organization.setOrganizationLogo(updateOrganization.getOrganizationLogo());

        organizationRepository.updateOrganization(organization);
        return new Response(ResponseMessage.SUCCESS, HttpStatus.OK);

    }
}
