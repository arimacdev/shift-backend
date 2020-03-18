package com.arimac.backend.pmtool.projectmanagementtool.dtos;

public class UserListResponseDto {
    private String userId;
    private String idpUserId;
    private String firstName;
    private String lastName;
    private String email;
    private String userName;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getIdpUserId() {
        return idpUserId;
    }

    public void setIdpUserId(String idpUserId) {
        this.idpUserId = idpUserId;
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

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
