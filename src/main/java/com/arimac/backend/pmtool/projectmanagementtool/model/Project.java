package com.arimac.backend.pmtool.projectmanagementtool.model;

import org.springframework.jdbc.core.RowMapper;

import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

public class Project implements RowMapper<Project> {
    private String projectId;
    private String projectName;
    private String clientId;
    private Timestamp projectStartDate;
    private Timestamp projectEndDate;
    private String projectStatus;
    private boolean isDeleted;

    public Project() {
    }

    public Project(String projectId, String projectName, String clientId, Timestamp projectStartDate, Timestamp projectEndDate, String projectStatus, boolean isDeleted) {
        this.projectId = projectId;
        this.projectName = projectName;
        this.clientId = clientId;
        this.projectStartDate = projectStartDate;
        this.projectEndDate = projectEndDate;
        this.projectStatus = projectStatus;
        this.isDeleted = isDeleted;
    }

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
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

    public String getProjectStatus() {
        return projectStatus;
    }

    public void setProjectStatus(String projectStatus) {
        this.projectStatus = projectStatus;
    }

    public boolean getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(boolean deleted) {
        isDeleted = deleted;
    }

    @Override
    public Project mapRow(ResultSet resultSet, int i) throws SQLException {
        return new Project(
                resultSet.getString("projectId"),
                resultSet.getString("projectName"),
                resultSet.getString("clientId"),
                resultSet.getTimestamp("projectStartDate"),
                resultSet.getTimestamp("projectEndDate"),
                resultSet.getString("projectStatus"),
                resultSet.getBoolean("isDeleted")
        );
    }
}
