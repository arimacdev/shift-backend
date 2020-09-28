package com.arimac.backend.pmtool.projectmanagementtool.model;

import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class Project_Keys implements RowMapper<Project_Keys> {
    private String projectId;
    private String domain;
    private String projectKey;

    public Project_Keys(String projectId, String domain, String projectKey) {
        this.projectId = projectId;
        this.domain = domain;
        this.projectKey = projectKey;
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

    @Override
    public Project_Keys mapRow(ResultSet resultSet, int i) throws SQLException {
        return null;
    }
}
