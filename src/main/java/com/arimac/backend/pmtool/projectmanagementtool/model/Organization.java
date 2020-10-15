package com.arimac.backend.pmtool.projectmanagementtool.model;

import javax.validation.constraints.NotNull;
import java.sql.Timestamp;

public class Organization {
    private String organizationId;
    private String organizationName;
    private String country;
    private String organizationLogo;

    private String createdBy;
    private Timestamp createdAt;


    public Organization() {
    }

    public Organization(String organizationId, String organizationName, String country, String organizationLogo, String createdBy, Timestamp createdAt) {
        this.organizationId = organizationId;
        this.organizationName = organizationName;
        this.country = country;
        this.organizationLogo = organizationLogo;
        this.createdBy = createdBy;
        this.createdAt = createdAt;
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
}
