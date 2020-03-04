package com.arimac.backend.pmtool.projectmanagementtool.model;

import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

public class Project_User implements RowMapper<Project_User> {

    private String projectId;
    private String assigneeId;
    private Timestamp assignedAt;
    private Boolean isAdmin;
    private String assigneeProjectRole;

    public Project_User() {
    }

    public Project_User(String projectId, String assigneeId, Timestamp assignedAt, Boolean isAdmin, String assigneeProjectRole) {
        this.projectId = projectId;
        this.assigneeId = assigneeId;
        this.assignedAt = assignedAt;
        this.isAdmin = isAdmin;
        this.assigneeProjectRole = assigneeProjectRole;
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

    public Timestamp getAssignedAt() {
        return assignedAt;
    }

    public void setAssignedAt(Timestamp assignedAt) {
        this.assignedAt = assignedAt;
    }

    public Boolean getIsAdmin() {
        return isAdmin;
    }

    public void setIsAdmin(Boolean admin) {
        isAdmin = admin;
    }

    public String getAssigneeProjectRole() {
        return assigneeProjectRole;
    }

    public void setAssigneeProjectRole(String assigneeProjectRole) {
        this.assigneeProjectRole = assigneeProjectRole;
    }

    @Override
    public Project_User mapRow(ResultSet resultSet, int i) throws SQLException {
        return new Project_User(
                resultSet.getString("projectId"),
                resultSet.getString("userId"),
                resultSet.getTimestamp("assignedAt"),
                resultSet.getBoolean("isAdmin"),
                resultSet.getString("userProjectRole")
        );
    }
}
