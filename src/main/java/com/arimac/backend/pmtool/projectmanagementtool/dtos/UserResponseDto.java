package com.arimac.backend.pmtool.projectmanagementtool.dtos;

public class UserResponseDto {
    private String userId;
    private  String firstName;
    private String lastName;
    private String userName;
    private String email;
    private String profileImage;
    private String userSlackId;
    private boolean notification;

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

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
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

    public String getUserSlackId() {
        return userSlackId;
    }

    public void setUserSlackId(String userSlackId) {
        this.userSlackId = userSlackId;
    }

    public boolean isNotification() {
        return notification;
    }

    public void setNotification(boolean notification) {
        this.notification = notification;
    }
}
