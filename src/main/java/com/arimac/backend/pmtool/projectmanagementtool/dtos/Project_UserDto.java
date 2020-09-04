package com.arimac.backend.pmtool.projectmanagementtool.dtos;

import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class Project_UserDto implements RowMapper<Project_UserDto> {
    private String userId;
    private String firstName;
    private String lastName;
    private String profileImage;
    private boolean isBlocked;

    public Project_UserDto() {
    }

    public Project_UserDto(String userId, String firstName, String lastName, String profileImage, boolean isBlocked) {
        this.userId = userId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.profileImage = profileImage;
        this.isBlocked = isBlocked;
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

    public boolean getIsBlocked() {
        return isBlocked;
    }

    public void setIsBlocked(boolean blocked) {
        isBlocked = blocked;
    }

    public Project_UserDto mapRow(ResultSet resultSet, int i) throws SQLException {
        return new Project_UserDto(
                resultSet.getString("userId"),
                resultSet.getString("firstName"),
                resultSet.getString("lastName"),
                resultSet.getString("profileImage"),
                resultSet.getBoolean("isBlocked")
        );
    }
}
