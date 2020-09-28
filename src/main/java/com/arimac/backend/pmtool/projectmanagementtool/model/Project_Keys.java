package com.arimac.backend.pmtool.projectmanagementtool.model;

import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class Project_Keys implements RowMapper<Project_Keys> {
    private String projectId;
    private String domain;
    private String projectKey;
    private boolean isValid;

    public Project_Keys() {
    }

    public Project_Keys(String projectId, String domain, String projectKey, boolean isValid) {
        this.projectId = projectId;
        this.domain = domain;
        this.projectKey = projectKey;
        this.isValid = isValid;
    }

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public String getProjectKey() {
        return projectKey;
    }

    public void setProjectKey(String projectKey) {
        this.projectKey = projectKey;
    }

    public boolean getIsValid() {
        return isValid;
    }

    public void setIsValid(boolean valid) {
        isValid = valid;
    }

    @Override
    public Project_Keys mapRow(ResultSet resultSet, int i) throws SQLException {
        return new Project_Keys(
                resultSet.getString("projectId"),
                resultSet.getString("domain"),
                resultSet.getString("projectKey"),
                resultSet.getBoolean("isValid")
        );
    }
}
