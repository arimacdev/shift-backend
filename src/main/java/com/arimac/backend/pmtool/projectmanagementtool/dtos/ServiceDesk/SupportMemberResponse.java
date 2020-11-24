package com.arimac.backend.pmtool.projectmanagementtool.dtos.ServiceDesk;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class SupportMemberResponse {
    private String userId;
    private String firstName;
    private String lastName;
    private String email;
    private String profileImage;
    private String clientId;
    private String supportProjectId;
    private boolean isAdmin;
    private boolean isRemoved;

    public SupportMemberResponse() {
    }

    public SupportMemberResponse(String userId, String firstName, String lastName, String email, String profileImage, String clientId, String supportProjectId, boolean isAdmin, boolean isRemoved) {
        this.userId = userId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.profileImage = profileImage;
        this.clientId = clientId;
        this.supportProjectId = supportProjectId;
        this.isAdmin = isAdmin;
        this.isRemoved = isRemoved;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

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

    public String getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getSupportProjectId() {
        return supportProjectId;
    }

    public void setSupportProjectId(String supportProjectId) {
        this.supportProjectId = supportProjectId;
    }

    public boolean isAdmin() {
        return isAdmin;
    }

    public void setAdmin(boolean admin) {
        isAdmin = admin;
    }

    public boolean isRemoved() {
        return isRemoved;
    }

    public void setRemoved(boolean removed) {
        isRemoved = removed;
    }
}
