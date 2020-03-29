package com.arimac.backend.pmtool.projectmanagementtool.model;

import com.arimac.backend.pmtool.projectmanagementtool.enumz.ProjectStatusEnum;
import org.springframework.jdbc.core.RowMapper;

import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;

public class Project implements RowMapper<Project> {
    private String projectId;
    private String projectName;
    private String clientId;
    private Date projectStartDate;
    private Date projectEndDate;
    private ProjectStatusEnum projectStatus;
    private boolean isDeleted;

    public Project() {
    }

    public Project(String projectId, String projectName, String clientId, Date projectStartDate, Date projectEndDate, ProjectStatusEnum projectStatus, boolean isDeleted) {
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

    public Date getProjectStartDate() {
        return projectStartDate;
    }

    public void setProjectStartDate(Date projectStartDate) {
        this.projectStartDate = projectStartDate;
    }

    public Date getProjectEndDate() {
        return projectEndDate;
    }

    public void setProjectEndDate(Date projectEndDate) {
        this.projectEndDate = projectEndDate;
    }

    public boolean isDeleted() {
        return isDeleted;
    }

    public void setDeleted(boolean deleted) {
        isDeleted = deleted;
    }

    public ProjectStatusEnum getProjectStatus() {
        return projectStatus;
    }

    public void setProjectStatus(ProjectStatusEnum projectStatus) {
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
                ProjectStatusEnum.valueOf(resultSet.getString("projectStatus")),
                resultSet.getBoolean("isDeleted")
        );
    }
}
