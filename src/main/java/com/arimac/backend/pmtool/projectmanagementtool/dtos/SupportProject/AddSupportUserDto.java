package com.arimac.backend.pmtool.projectmanagementtool.dtos.SupportProject;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

public class AddSupportUserDto {
    @NotNull
    private String firstName;
    @NotNull
    private String lastName;
    @NotNull
    @Email
    private String email;
    @NotNull
    private String organizationId;
    private String supportUserId;

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(String organizationId) {
        this.organizationId = organizationId;
    }

    public String getSupportUserId() {
        return supportUserId;
    }

    public void setSupportUserId(String supportUserId) {
        this.supportUserId = supportUserId;
    }

    @Override
    public String toString() {
        return "AddSupportUserDto{" +
                "firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                ", clientId='" + organizationId + '\'' +
                ", supportUserId='" + supportUserId + '\'' +
                '}';
    }
}