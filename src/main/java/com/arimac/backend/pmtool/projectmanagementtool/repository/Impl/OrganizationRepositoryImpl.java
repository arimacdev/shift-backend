package com.arimac.backend.pmtool.projectmanagementtool.repository.Impl;

import com.arimac.backend.pmtool.projectmanagementtool.exception.PMException;
import com.arimac.backend.pmtool.projectmanagementtool.model.Organization;
import com.arimac.backend.pmtool.projectmanagementtool.repository.OrganizationRepository;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.sql.PreparedStatement;

@Service
public class OrganizationRepositoryImpl implements OrganizationRepository {

    private final JdbcTemplate jdbcTemplate;

    public OrganizationRepositoryImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void addOrganization(Organization organization) {
        try {
            jdbcTemplate.update(connection -> {
                PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO Organization(organizationId,organizationName,country,organizationLogo,createdBy,createdAt) VALUES(?,?,?,?,?,?)");
                preparedStatement.setString(1, organization.getOrganizationId());
                preparedStatement.setString(2, organization.getOrganizationName());
                preparedStatement.setString(3, organization.getCountry());
                preparedStatement.setString(4, organization.getOrganizationLogo());
                preparedStatement.setString(5, organization.getCreatedBy());
                preparedStatement.setTimestamp(6, organization.getCreatedAt());
               return preparedStatement;
            });

        } catch (Exception e){
            throw new PMException(e.getMessage());
        }
    }
}
