package com.arimac.backend.pmtool.projectmanagementtool.dtos.Organization;

import javax.validation.constraints.NotNull;

public class AddOrganization {
    @NotNull
    private String organizationName;
    @NotNull
    private String country;
    private String organizationLogo;


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

    @Override
    public String toString() {
        return "AddOrganization{" +
                "organizationName='" + organizationName + '\'' +
                ", country='" + country + '\'' +
                ", organizationLogo='" + organizationLogo + '\'' +
                '}';
    }
}
