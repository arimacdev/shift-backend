package com.arimac.backend.pmtool.projectmanagementtool.dtos.ServiceDesk;

import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class SupportProjectResponse implements RowMapper<SupportProjectResponse> {
    private String project;
    private String projectName;
    private String projectAlias;
    private String clientId;
    private boolean isSupportEnabled;
    private boolean isSupportAdded;


    public SupportProjectResponse() {
    }

    public SupportProjectResponse(String project, String projectName, String projectAlias, String clientId, boolean isSupportEnabled, boolean isSupportAdded) {
        this.project = project;
        this.projectName = projectName;
        this.projectAlias = projectAlias;
        this.clientId = clientId;
        this.isSupportEnabled = isSupportEnabled;
        this.isSupportAdded = isSupportAdded;
    }

    public String getProject() {
        return project;
    }

    public void setProject(String project) {
        this.project = project;
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

    public boolean isSupportEnabled() {
        return isSupportEnabled;
    }

    public void setSupportEnabled(boolean supportEnabled) {
        isSupportEnabled = supportEnabled;
    }

    public boolean isSupportAdded() {
        return isSupportAdded;
    }

    public void setSupportAdded(boolean supportAdded) {
        isSupportAdded = supportAdded;
    }

    @Override
    public SupportProjectResponse mapRow(ResultSet resultSet, int i) throws SQLException {
        return new SupportProjectResponse(
                resultSet.getString("project"),
                resultSet.getString("projectName"),
                resultSet.getString("projectAlias"),
                resultSet.getString("clientId"),
                resultSet.getBoolean("isSupportEnabled"),
                resultSet.getBoolean("isSupportAdded")
                );
    }
}


