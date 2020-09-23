package com.arimac.backend.pmtool.projectmanagementtool.model;

import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

public class Project_User implements RowMapper<Project_User> {

    private String projectId;
    private String assigneeId;
    private Timestamp assignedAt;
    private String assigneeJobRole;
    private int assigneeProjectRole;
    private boolean isBlocked;
    private boolean isPinned;

    public Project_User() {
    }

    public Project_User(String projectId, String assigneeId, Timestamp assignedAt, String assigneeJobRole, int assigneeProjectRole, boolean isBlocked, boolean isPinned) {
        this.projectId = projectId;
        this.assigneeId = assigneeId;
        this.assignedAt = assignedAt;
        this.assigneeJobRole = assigneeJobRole;
        this.assigneeProjectRole = assigneeProjectRole;
        this.isBlocked = isBlocked;
        this.isPinned = isPinned;
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

    public String getAssigneeJobRole() {
        return assigneeJobRole;
    }

    public void setAssigneeJobRole(String assigneeJobRole) {
        this.assigneeJobRole = assigneeJobRole;
    }

    public int getAssigneeProjectRole() {
        return assigneeProjectRole;
    }

    public void setAssigneeProjectRole(int assigneeProjectRole) {
        this.assigneeProjectRole = assigneeProjectRole;
    }

    public boolean getIsBlocked() {
        return isBlocked;
    }

    public void setIsBlocked(boolean blocked) {
        isBlocked = blocked;
    }

    public boolean getIsPinned() {
        return isPinned;
    }

    public void setIsPinned(boolean pinned) {
        isPinned = pinned;
    }

    @Override
    public Project_User mapRow(ResultSet resultSet, int i) throws SQLException {
        return new Project_User(
                resultSet.getString("projectId"),
                resultSet.getString("assigneeId"),
                resultSet.getTimestamp("assignedAt"),
                resultSet.getString("assigneeJobRole"),
                resultSet.getInt("assigneeProjectRole"),
                resultSet.getBoolean("isBlocked"),
                resultSet.getBoolean("isPinned")
                );
    }
}
