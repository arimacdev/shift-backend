package com.arimac.backend.pmtool.projectmanagementtool.dtos.Meeting;

public class MeetingUser {
    private String userId;
    private String firstName;
    private String lastName;
    private String profileImage;

    public MeetingUser(String userId, String firstName, String lastName, String profileImage) {
        this.userId = userId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.profileImage = profileImage;
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

    public String getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }
}
