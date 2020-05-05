package com.arimac.backend.pmtool.projectmanagementtool.dtos;

import java.sql.Timestamp;

public class ProjectDto {
    private String projectOwner;
    private String projectName;
    private String clientId;
    private String projectAlias;
    private Timestamp projectStartDate;
    private Timestamp projectEndDate;

    public ProjectDto() {
    }

    public String getProjectAlias() {
        return projectAlias;
    }

    public void setProjectAlias(String projectAlias) {
        this.projectAlias = projectAlias;
    }

    public String getProjectOwner() {
        return projectOwner;
    }

    public void setProjectOwner(String projectOwner) {
        this.projectOwner = projectOwner;
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

    @Override
    public String toString() {
        return "ProjectDto{" +
                "projectOwner='" + projectOwner + '\'' +
                ", projectName='" + projectName + '\'' +
                ", clientId='" + clientId + '\'' +
                ", aliasName='" + projectAlias + '\'' +
                ", projectStartDate=" + projectStartDate +
                ", projectEndDate=" + projectEndDate +
                '}';
    }
}
