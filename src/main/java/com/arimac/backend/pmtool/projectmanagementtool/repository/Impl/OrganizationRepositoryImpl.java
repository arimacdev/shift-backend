package com.arimac.backend.pmtool.projectmanagementtool.repository.Impl;

import com.arimac.backend.pmtool.projectmanagementtool.exception.PMException;
import com.arimac.backend.pmtool.projectmanagementtool.model.Organization;
import com.arimac.backend.pmtool.projectmanagementtool.model.Project;
import com.arimac.backend.pmtool.projectmanagementtool.repository.OrganizationRepository;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.sql.PreparedStatement;
import java.util.List;

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

    @Override
    public Organization getOrganizationById(String organizationId) {
        String sql = "SELECT * FROM Organization WHERE organizationId=? AND isDeleted=false";
        try {
            return jdbcTemplate.queryForObject(sql, new Organization(), organizationId);
        } catch (EmptyResultDataAccessException e){
            return null;
        }
        catch (Exception e){
            throw new PMException(e.getMessage());
        }
    }

    @Override
    public List<Organization> getAllOrganizations(int limit, int offset) {
        String sql = "SELECT * FROM Organization WHERE isDeleted=false LIMIT ? OFFSET ?";
        try {
            return jdbcTemplate.query(sql, new Organization(), limit, offset);
        } catch (Exception e){
            throw new PMException(e.getMessage());
        }
    }

    @Override
    public List<Project> getProjectsOfOrganization(String organizationId) {
        String sql = "SELECT * FROM project WHERE clientId=? AND isDeleted=false";
        try {
            return jdbcTemplate.query(sql, new Project(), organizationId);
        } catch (Exception e){
            throw new PMException(e.getMessage());
        }
    }

    @Override
    public void updateOrganization(Organization organization) {
        jdbcTemplate.update(connection -> {
            PreparedStatement preparedStatement = connection.prepareStatement("UPDATE Organization SET organizationName=?, country=?, organizationLogo=? WHERE organizationId=?");
            preparedStatement.setString(1, organization.getOrganizationName());
            preparedStatement.setString(2, organization.getCountry());
            preparedStatement.setString(3, organization.getOrganizationLogo());
            preparedStatement.setString(4, organization.getOrganizationId());

            return preparedStatement;
        });
    }

    @Override
    public void flagOrganization(String organizationId) {
        try {
            String sql = "UPDATE Organization SET isDeleted=true WHERE organizationId=?";
            jdbcTemplate.update(sql, organizationId);
        } catch (Exception e){
            throw new PMException(e.getMessage());
        }
    }
}
