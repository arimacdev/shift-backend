package com.arimac.backend.pmtool.projectmanagementtool.model;

import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

public class ProjectFile implements RowMapper<ProjectFile> {
    private String projectFileId;
    private String projectId;
    private String projectFileName;
    private String projectFileUrl;
    private String projectFileAddedBy;
    private Timestamp projectFileAddedOn;
    private boolean isDeleted;

    public ProjectFile() {
    }

    public ProjectFile(String projectFileId, String projectId, String projectFileName, String projectFileUrl, String projectFileAddedBy, Timestamp projectFileAddedOn, boolean isDeleted) {
        this.projectFileId = projectFileId;
        this.projectId = projectId;
        this.projectFileName = projectFileName;
        this.projectFileUrl = projectFileUrl;
        this.projectFileAddedBy = projectFileAddedBy;
        this.projectFileAddedOn = projectFileAddedOn;
        this.isDeleted = isDeleted;
    }

    public String getProjectFileId() {
        return projectFileId;
    }

    public void setProjectFileId(String projectFileId) {
        this.projectFileId = projectFileId;
    }

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    public String getProjectFileName() {
        return projectFileName;
    }

    public void setProjectFileName(String projectFileName) {
        this.projectFileName = projectFileName;
    }

    public String getProjectFileUrl() {
        return projectFileUrl;
    }

    public void setProjectFileUrl(String projectFileUrl) {
        this.projectFileUrl = projectFileUrl;
    }

    public String getProjectFileAddedBy() {
        return projectFileAddedBy;
    }

    public void setProjectFileAddedBy(String projectFileAddedBy) {
        this.projectFileAddedBy = projectFileAddedBy;
    }

    public Timestamp getProjectFileAddedOn() {
        return projectFileAddedOn;
    }

    public void setProjectFileAddedOn(Timestamp projectFileAddedOn) {
        this.projectFileAddedOn = projectFileAddedOn;
    }

    public boolean getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(boolean deleted) {
        isDeleted = deleted;
    }

    @Override
    public ProjectFile mapRow(ResultSet resultSet, int i) throws SQLException {
        return new ProjectFile(
                resultSet.getString("projectFileId"),
                resultSet.getString("projectId"),
                resultSet.getString("projectFileName"),
                resultSet.getString("projectFileUrl"),
                resultSet.getString("projectFileAddedBy"),
                resultSet.getTimestamp("projectFileAddedOn"),
                resultSet.getBoolean("isDeleted")
        );
    }
}
