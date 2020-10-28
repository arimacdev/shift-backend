package com.arimac.backend.pmtool.projectmanagementtool.model;

import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

public class Project_SupportMember implements RowMapper<Project_SupportMember> {
    private String projectId;
    private String assigneeId;
    private Timestamp assignedAt;
    private String assignedBy;
    private boolean isEnabled;

    public Project_SupportMember() {
    }

    public Project_SupportMember(String projectId, String assigneeId, Timestamp assignedAt, String assignedBy, boolean isEnabled) {
        this.projectId = projectId;
        this.assigneeId = assigneeId;
        this.assignedAt = assignedAt;
        this.assignedBy = assignedBy;
        this.isEnabled = isEnabled;
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

    public String getAssignedBy() {
        return assignedBy;
    }

    public void setAssignedBy(String assignedBy) {
        this.assignedBy = assignedBy;
    }

    public boolean getIsEnabled() {
        return isEnabled;
    }

    public void setIsEnabled(boolean enabled) {
        isEnabled = enabled;
    }

    @Override
    public Project_SupportMember mapRow(ResultSet resultSet, int i) throws SQLException {
        return new Project_SupportMember(
                resultSet.getString("projectId"),
                resultSet.getString("assigneeId"),
                resultSet.getTimestamp("assignedAt"),
                resultSet.getString("assignedBy"),
                resultSet.getBoolean("isEnabled")
        );
    }
}
