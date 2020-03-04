package com.arimac.backend.pmtool.projectmanagementtool.model;

import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

public class Project_User implements RowMapper<Project_User> {

    private String projectId;
    private String userId;
    private Timestamp assignedAt;

    public Project_User() {
    }

    public Project_User(String projectId, String userId, Timestamp assignedAt) {
        this.projectId = projectId;
        this.userId = userId;
        this.assignedAt = assignedAt;
    }

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Timestamp getAssignedAt() {
        return assignedAt;
    }

    public void setAssignedAt(Timestamp assignedAt) {
        this.assignedAt = assignedAt;
    }


    @Override
    public Project_User mapRow(ResultSet resultSet, int i) throws SQLException {
        return new Project_User(
                resultSet.getString("projectId"),
                resultSet.getString("userId"),
                resultSet.getTimestamp("assignedAt")
        );
    }
}
