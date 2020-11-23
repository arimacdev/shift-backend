package com.arimac.backend.pmtool.projectmanagementtool.dtos.SupportProject;

import javax.validation.constraints.NotNull;
import java.sql.Timestamp;

public class AddSupportProject {
    @NotNull
    private String projectId;
    @NotNull
    private String organizationId;
    @NotNull
    private boolean isSupportEnabled;

    private Timestamp supportAddedOn;
    private String supportAddedBy;

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    public String getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(String organizationId) {
        this.organizationId = organizationId;
    }

    public boolean isSupportEnabled() {
        return isSupportEnabled;
    }

    public void setSupportEnabled(boolean supportEnabled) {
        isSupportEnabled = supportEnabled;
    }

    public Timestamp getSupportAddedOn() {
        return supportAddedOn;
    }

    public void setSupportAddedOn(Timestamp supportAddedOn) {
        this.supportAddedOn = supportAddedOn;
    }

    public String getSupportAddedBy() {
        return supportAddedBy;
    }

    public void setSupportAddedBy(String supportAddedBy) {
        this.supportAddedBy = supportAddedBy;
    }
}
