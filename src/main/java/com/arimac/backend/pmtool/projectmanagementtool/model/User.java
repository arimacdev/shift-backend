package com.arimac.backend.pmtool.projectmanagementtool.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class User implements RowMapper<User> {
    private String userId;
    private String idpUserId;
    private String username;
    private String firstName;
    private String lastName;
    private String email;
    private String profileImage;
    private String userSlackId;
    @JsonProperty
    private boolean notification;
    @JsonProperty
    private boolean isActive;

    public User() {
    }

    public User(String userId, String idpUserId, String username, String firstName, String lastName, String email, String profileImage, String userSlackId, boolean notification, boolean isActive) {
        this.userId = userId;
        this.idpUserId = idpUserId;
        this.username = username;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.profileImage = profileImage;
        this.userSlackId = userSlackId;
        this.notification = notification;
        this.isActive = isActive;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getIdpUserId() {
        return idpUserId;
    }

    public void setIdpUserId(String idpUserId) {
        this.idpUserId = idpUserId;
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

    public String getUserSlackId() {
        return userSlackId;
    }

    public void setUserSlackId(String userSlackId) {
        this.userSlackId = userSlackId;
    }

    public boolean getNotification() {
        return notification;
    }

    public void setNotification(boolean notification) {
        this.notification = notification;
    }

    public boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(boolean active) {
        isActive = active;
    }

    @Override
    public User mapRow(ResultSet resultSet, int i) throws SQLException {
        return new User(
                resultSet.getString("userId"),
                resultSet.getString("idpUserId"),
                resultSet.getString("username"),
                resultSet.getString("firstName"),
                resultSet.getString("lastName"),
                resultSet.getString("email"),
                resultSet.getString("profileImage"),
                resultSet.getString("userSlackId"),
                resultSet.getBoolean("notification"),
                resultSet.getBoolean("isActive")
        );
    }
}
