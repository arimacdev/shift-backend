package com.arimac.backend.pmtool.projectmanagementtool.dtos;

import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class UserProjectDto implements RowMapper<UserProjectDto> {
    private String projectId;
    private String assigneeId;
    private String assigneeFirstName;
    private String assigneeLastName;
    private String assigneeProfileImage;
    private String projectRoleName;
    private String projectJobRoleName;
    private String projectRoleId;

    public UserProjectDto() {
    }


    public UserProjectDto(String projectId, String assigneeId, String assigneeFirstName, String assigneeLastName, String assigneeProfileImage, String projectRoleName, String projectJobRoleName, String projectRoleId) {
        this.projectId = projectId;
        this.assigneeId = assigneeId;
        this.assigneeFirstName = assigneeFirstName;
        this.assigneeLastName = assigneeLastName;
        this.assigneeProfileImage = assigneeProfileImage;
        this.projectRoleName = projectRoleName;
        this.projectJobRoleName = projectJobRoleName;
        this.projectRoleId = projectRoleId;
    }

    public String getAssigneeProfileImage() {
        return assigneeProfileImage;
    }

    public void setAssigneeProfileImage(String assigneeProfileImage) {
        this.assigneeProfileImage = assigneeProfileImage;
    }

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    public String getAssigneeId() {
        return assigneeId;
    }

    public void setAssigneeId(String assigneeId) {
        this.assigneeId = assigneeId;
    }

    public String getAssigneeFirstName() {
        return assigneeFirstName;
    }

    public void setAssigneeFirstName(String assigneeFirstName) {
        this.assigneeFirstName = assigneeFirstName;
    }

    public String getAssigneeLastName() {
        return assigneeLastName;
    }

    public void setAssigneeLastName(String assigneeLastName) {
        this.assigneeLastName = assigneeLastName;
    }

    public String getProjectRoleName() {
        return projectRoleName;
    }

    public void setProjectRoleName(String projectRoleName) {
        this.projectRoleName = projectRoleName;
    }

    public String getProjectJobRoleName() {
        return projectJobRoleName;
    }

    public void setProjectJobRoleName(String projectJobRoleName) {
        this.projectJobRoleName = projectJobRoleName;
    }

    public String getProjectRoleId() {
        return projectRoleId;
    }

    public void setProjectRoleId(String projectRoleId) {
        this.projectRoleId = projectRoleId;
    }

    @Override
    public UserProjectDto mapRow(ResultSet resultSet, int i) throws SQLException {
        return new UserProjectDto(
          resultSet.getString("projectId"),
          resultSet.getString("assigneeId"),
          resultSet.getString("firstName"),
          resultSet.getString("lastName"),
          resultSet.getString("profileImage"),
          resultSet.getString("projectRoleName"),
          resultSet.getString("assigneeJobRole"),
          resultSet.getString("projectRoleId")
        );
    }
}
