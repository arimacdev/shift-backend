package com.arimac.backend.pmtool.projectmanagementtool.dtos;

import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

public class ProjectUserResponseDto implements RowMapper<ProjectUserResponseDto> {
    private String projectId;
    private String assigneeId;
    private Timestamp assignedAt;
    private boolean isAdmin;
    private String assigneeProjectRole;
    private String projectName;
    private String projectStatus;
    private Timestamp projectStartDate;
    private Timestamp projectEndDate;

    public ProjectUserResponseDto() {
    }

    public ProjectUserResponseDto(String projectId, String assigneeId, Timestamp assignedAt, boolean isAdmin, String assigneeProjectRole, String projectName, String projectStatus, Timestamp projectStartDate, Timestamp projectEndDate) {
        this.projectId = projectId;
        this.assigneeId = assigneeId;
        this.assignedAt = assignedAt;
        this.isAdmin = isAdmin;
        this.assigneeProjectRole = assigneeProjectRole;
        this.projectName = projectName;
        this.projectStatus = projectStatus;
        this.projectStartDate = projectStartDate;
        this.projectEndDate = projectEndDate;
    }

    public boolean isAdmin() {
        return isAdmin;
    }

    public void setAdmin(boolean admin) {
        isAdmin = admin;
    }

    public String getAssigneeProjectRole() {
        return assigneeProjectRole;
    }

    public void setAssigneeProjectRole(String assigneeProjectRole) {
        this.assigneeProjectRole = assigneeProjectRole;
    }

    public Timestamp getProjectStartDate() {
        return projectStartDate;
    }

    public void setProjectStartDate(Timestamp projectStartDate) {
        this.projectStartDate = projectStartDate;
    }

    public Timestamp getProjectEndDate() {
        return projectEndDate;
    }

    public void setProjectEndDate(Timestamp projectEndDate) {
        this.projectEndDate = projectEndDate;
    }

    public String getAssigneeId() {
        return assigneeId;
    }

    public void setAssigneeId(String assigneeId) {
        this.assigneeId = assigneeId;
    }

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    public Timestamp getAssignedAt() {
        return assignedAt;
    }

    public void setAssignedAt(Timestamp assignedAt) {
        this.assignedAt = assignedAt;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getProjectStatus() {
        return projectStatus;
    }

    public void setProjectStatus(String projectStatus) {
        this.projectStatus = projectStatus;
    }

    @Override
    public ProjectUserResponseDto mapRow(ResultSet resultSet, int i) throws SQLException {
        return new ProjectUserResponseDto(
                resultSet.getString("projectId"),
                resultSet.getString("assigneeId"),
                resultSet.getTimestamp("assignedAt"),
                resultSet.getBoolean("isAdmin"),
                resultSet.getString("assigneeProjectRole"),
                resultSet.getString("projectName"),
                resultSet.getString("projectStatus"),
                resultSet.getTimestamp("projectStartDate"),
                resultSet.getTimestamp("projectEndDate")
        );
    }
}
