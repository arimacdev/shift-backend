package com.arimac.backend.pmtool.projectmanagementtool.dtos.Organization;

import javax.validation.constraints.NotNull;

public class UpdateOrganization {
    private String organizationName;
    private String country;
    private String organizationLogo;
    private String organizationEmail;
    private String organizationContact;


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

    public String getOrganizationEmail() {
        return organizationEmail;
    }

    public void setOrganizationEmail(String organizationEmail) {
        this.organizationEmail = organizationEmail;
    }

    public String getOrganizationContact() {
        return organizationContact;
    }

    public void setOrganizationContact(String organizationContact) {
        this.organizationContact = organizationContact;
    }

    @Override
    public String toString() {
        return "UpdateOrganization{" +
                "organizationName='" + organizationName + '\'' +
                ", country='" + country + '\'' +
                ", organizationLogo='" + organizationLogo + '\'' +
                ", organizationEmail='" + organizationEmail + '\'' +
                ", organizationContact='" + organizationContact + '\'' +
                '}';
    }
}
