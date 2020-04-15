package com.arimac.backend.pmtool.projectmanagementtool.dtos;

import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

public class ProjectFileResponseDto implements RowMapper<ProjectFileResponseDto> {
    private String projectFileId;
    private String projectId;
    private String projectFileName;
    private String projectFileUrl;
    private int projectFileSize;
    private String projectFileAddedBy;
    private Timestamp projectFileAddedOn;
    private String firstName;
    private String lastName;

    public ProjectFileResponseDto() {
    }

    public ProjectFileResponseDto(String projectFileId, String projectId, String projectFileName, String projectFileUrl, int projectFileSize, String projectFileAddedBy, Timestamp projectFileAddedOn, String firstName, String lastName) {
        this.projectFileId = projectFileId;
        this.projectId = projectId;
        this.projectFileName = projectFileName;
        this.projectFileUrl = projectFileUrl;
        this.projectFileSize = projectFileSize;
        this.projectFileAddedBy = projectFileAddedBy;
        this.projectFileAddedOn = projectFileAddedOn;
        this.firstName = firstName;
        this.lastName = lastName;
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

    public int getProjectFileSize() {
        return projectFileSize;
    }

    public void setProjectFileSize(int projectFileSize) {
        this.projectFileSize = projectFileSize;
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

    @Override
    public ProjectFileResponseDto mapRow(ResultSet resultSet, int i) throws SQLException {
        return new ProjectFileResponseDto(
                resultSet.getString("projectFileId"),
                resultSet.getString("projectId"),
                resultSet.getString("projectFileName"),
                resultSet.getString("projectFileUrl"),
                resultSet.getInt("projectFileSize"),
                resultSet.getString("projectFileAddedBy"),
                resultSet.getTimestamp("projectFileAddedOn"),
                resultSet.getString("firstName"),
                resultSet.getString("lastName")
        );
    }
}
