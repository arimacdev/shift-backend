package com.arimac.backend.pmtool.projectmanagementtool.model;

import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

public class Organization implements RowMapper<Organization> {
    private String organizationId;
    private String organizationName;
    private String country;
    private String organizationLogo;

    private String createdBy;
    private Timestamp createdAt;

    private boolean hasSupportProjects;


    public Organization() {
    }

    public Organization(String organizationId, String organizationName, String country, String organizationLogo, String createdBy, Timestamp createdAt, boolean hasSupportProjects) {
        this.organizationId = organizationId;
        this.organizationName = organizationName;
        this.country = country;
        this.organizationLogo = organizationLogo;
        this.createdBy = createdBy;
        this.createdAt = createdAt;
        this.hasSupportProjects = hasSupportProjects;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    public String getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(String organizationId) {
        this.organizationId = organizationId;
    }

    public String getOrganizationName() {
        return organizationName;
    }

    public void setOrganizationName(String organizationName) {
        this.organizationName = organizationName;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getOrganizationLogo() {
        return organizationLogo;
    }

    public void setOrganizationLogo(String organizationLogo) {
        this.organizationLogo = organizationLogo;
    }

    public boolean isHasSupportProjects() {
        return hasSupportProjects;
    }

    public void setHasSupportProjects(boolean hasSupportProjects) {
        this.hasSupportProjects = hasSupportProjects;
    }

    @Override
    public Organization mapRow(ResultSet resultSet, int i) throws SQLException {
        return new Organization(
                resultSet.getString("organizationId"),
                resultSet.getString("organizationName"),
                resultSet.getString("country"),
                resultSet.getString("organizationLogo"),
                resultSet.getString("createdBy"),
                resultSet.getTimestamp("createdAt"),
                resultSet.getBoolean("hasSupportProjects"));
    }
}
