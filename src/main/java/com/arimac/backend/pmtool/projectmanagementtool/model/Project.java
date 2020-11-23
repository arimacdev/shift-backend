package com.arimac.backend.pmtool.projectmanagementtool.model;

import com.arimac.backend.pmtool.projectmanagementtool.enumz.ProjectStatusEnum;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

public class Project implements RowMapper<Project> {
    private String project;
    private String projectName;
    private String projectAlias;
    private String clientId;
    private Date projectStartDate;
    private Date projectEndDate;
    private ProjectStatusEnum projectStatus;
    private boolean isDeleted;
    private int issueCount;
    private int weightMeasure;
    private boolean isSupportEnabled;
    private boolean isSupportAdded;

    public Project() {
    }

    public Project(String project, String projectName, String projectAlias, String clientId, Date projectStartDate, Date projectEndDate, ProjectStatusEnum projectStatus, boolean isDeleted, int issueCount, int weightMeasure, boolean isSupportEnabled, boolean isSupportAdded) {
        this.project = project;
        this.projectName = projectName;
        this.projectAlias = projectAlias;
        this.clientId = clientId;
        this.projectStartDate = projectStartDate;
        this.projectEndDate = projectEndDate;
        this.projectStatus = projectStatus;
        this.isDeleted = isDeleted;
        this.issueCount = issueCount;
        this.weightMeasure = weightMeasure;
        this.isSupportEnabled = isSupportEnabled;
        this.isSupportAdded = isSupportAdded;
    }

    public String getProjectId() {
        return project;
    }

    public void setProjectId(String projectId) {
        this.project = projectId;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getProjectAlias() {
        return projectAlias;
    }

    public void setProjectAlias(String projectAlias) {
        this.projectAlias = projectAlias;
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


    public int getIssueCount() {
        return issueCount;
    }

    public void setIssueCount(int issueCount) {
        this.issueCount = issueCount;
    }


    public int getWeightMeasure() {
        return weightMeasure;
    }

    public void setWeightMeasure(int weightMeasure) {
        this.weightMeasure = weightMeasure;
    }

    public boolean getIsSupportEnabled() {
        return isSupportEnabled;
    }

    public void setIsSupportEnabled(boolean supportEnabled) {
        this.isSupportEnabled = supportEnabled;
    }

    public boolean getIsSupportAdded() {
        return isSupportAdded;
    }

    public void setIsSupportAdded(boolean supportAdded) {
        isSupportAdded = supportAdded;
    }

    @Override
    public Project mapRow(ResultSet resultSet, int i) throws SQLException {
        return new Project(
                resultSet.getString("project"),
                resultSet.getString("projectName"),
                resultSet.getString("projectAlias"),
                resultSet.getString("clientId"),
                resultSet.getTimestamp("projectStartDate"),
                resultSet.getTimestamp("projectEndDate"),
                ProjectStatusEnum.valueOf(resultSet.getString("projectStatus")),
                resultSet.getBoolean("isDeleted"),
                resultSet.getInt("issueCount"),
                resultSet.getInt("weightMeasure"),
                resultSet.getBoolean("isSupportEnabled"),
                resultSet.getBoolean("isSupportAdded"));
    }

    @Override
    public String toString() {
        return "Project{" +
                "project='" + project + '\'' +
                ", projectName='" + projectName + '\'' +
                ", projectAlias='" + projectAlias + '\'' +
                ", clientId='" + clientId + '\'' +
                ", projectStartDate=" + projectStartDate +
                ", projectEndDate=" + projectEndDate +
                ", projectStatus=" + projectStatus +
                ", isDeleted=" + isDeleted +
                ", issueCount=" + issueCount +
                ", weightMeasure=" + weightMeasure +
                '}';
    }
}
