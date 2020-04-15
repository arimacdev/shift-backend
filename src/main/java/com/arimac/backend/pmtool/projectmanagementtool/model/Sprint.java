package com.arimac.backend.pmtool.projectmanagementtool.model;

import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

public class Sprint implements RowMapper<Sprint> {
    private String sprintId;
    private String projectId;
    private String sprintName;
    private String sprintDescription;
    private String sprintCreatedBy;
    private Timestamp sprintCreatedAt;
    private Timestamp sprintStartDate;
    private Timestamp sprintEndDate;
    private boolean isDeleted;


    public Sprint() {
    }

    public Sprint(String sprintId, String projectId, String sprintName, String sprintDescription, String sprintCreatedBy, Timestamp sprintCreatedAt, Timestamp sprintStartDate, Timestamp sprintEndDate, boolean isDeleted) {
        this.sprintId = sprintId;
        this.projectId = projectId;
        this.sprintName = sprintName;
        this.sprintDescription = sprintDescription;
        this.sprintCreatedBy = sprintCreatedBy;
        this.sprintCreatedAt = sprintCreatedAt;
        this.sprintStartDate = sprintStartDate;
        this.sprintEndDate = sprintEndDate;
        this.isDeleted = isDeleted;
    }

    public String getSprintId() {
        return sprintId;
    }

    public void setSprintId(String sprintId) {
        this.sprintId = sprintId;
    }

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    public String getSprintName() {
        return sprintName;
    }

    public void setSprintName(String sprintName) {
        this.sprintName = sprintName;
    }

    public String getSprintDescription() {
        return sprintDescription;
    }

    public void setSprintDescription(String sprintDescription) {
        this.sprintDescription = sprintDescription;
    }

    public String getSprintCreatedBy() {
        return sprintCreatedBy;
    }

    public void setSprintCreatedBy(String sprintCreatedBy) {
        this.sprintCreatedBy = sprintCreatedBy;
    }

    public Timestamp getSprintCreatedAt() {
        return sprintCreatedAt;
    }

    public void setSprintCreatedAt(Timestamp sprintCreatedAt) {
        this.sprintCreatedAt = sprintCreatedAt;
    }

    public Timestamp getSprintStartDate() {
        return sprintStartDate;
    }

    public void setSprintStartDate(Timestamp sprintStartDate) {
        this.sprintStartDate = sprintStartDate;
    }

    public Timestamp getSprintEndDate() {
        return sprintEndDate;
    }

    public void setSprintEndDate(Timestamp sprintEndDate) {
        this.sprintEndDate = sprintEndDate;
    }

    public boolean getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(boolean deleted) {
        isDeleted = deleted;
    }

    @Override
    public Sprint mapRow(ResultSet resultSet, int i) throws SQLException {
        return new Sprint(
                resultSet.getString("sprintId"),
                resultSet.getString("projectId"),
                resultSet.getString("sprintName"),
                resultSet.getString("sprintDescription"),
                resultSet.getString("sprintCreatedBy"),
                resultSet.getTimestamp("sprintCreatedAt"),
                resultSet.getTimestamp("sprintStartDate"),
                resultSet.getTimestamp("sprintEndDate"),
                resultSet.getBoolean("isDeleted")
                );
    }

}
