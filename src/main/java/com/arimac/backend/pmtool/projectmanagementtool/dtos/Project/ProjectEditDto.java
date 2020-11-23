package com.arimac.backend.pmtool.projectmanagementtool.dtos.Project;

import com.arimac.backend.pmtool.projectmanagementtool.enumz.ProjectStatusEnum;

import java.sql.Timestamp;

public class ProjectEditDto {
    private String modifierId;
    private String projectName;
    private String clientId;
    private String projectStatus;
    private String projectAlias;
    private Timestamp projectStartDate;
    private Timestamp projectEndDate;
    private Boolean isSupportEnabled;

    public String getProjectAlias() {
        return projectAlias;
    }

    public void setProjectAlias(String projectAlias) {
        this.projectAlias = projectAlias;
    }

    public String getModifierId() {
        return modifierId;
    }

    public void setModifierId(String modifierId) {
        this.modifierId = modifierId;
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

    public String getProjectStatus() {
        return projectStatus;
    }

    public void setProjectStatus(String projectStatus) {
        this.projectStatus = projectStatus;
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

    public Boolean getIsSupportEnabled() {
        return isSupportEnabled;
    }

    public void setIsSupportEnabled(Boolean supportEnabled) {
        isSupportEnabled = supportEnabled;
    }

    @Override
    public String toString() {
        return "ProjectEditDto{" +
                "modifierId='" + modifierId + '\'' +
                ", projectName='" + projectName + '\'' +
                ", clientId='" + clientId + '\'' +
                ", projectStatus='" + projectStatus + '\'' +
                ", projectAlias='" + projectAlias + '\'' +
                ", projectStartDate=" + projectStartDate +
                ", projectEndDate=" + projectEndDate +
                ", isSupportEnabled=" + isSupportEnabled +
                '}';
    }
}
