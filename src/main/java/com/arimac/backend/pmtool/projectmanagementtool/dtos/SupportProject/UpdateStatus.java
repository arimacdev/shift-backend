package com.arimac.backend.pmtool.projectmanagementtool.dtos.SupportProject;

import javax.validation.constraints.NotNull;

public class UpdateStatus {
    private String projectId;
    private boolean supportStatus;


    public UpdateStatus() {
    }

    public UpdateStatus(String projectId, boolean supportStatus) {
        this.projectId = projectId;
        this.supportStatus = supportStatus;
    }

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    public boolean isSupportStatus() {
        return supportStatus;
    }

    public void setSupportStatus(boolean supportStatus) {
        this.supportStatus = supportStatus;
    }
}
