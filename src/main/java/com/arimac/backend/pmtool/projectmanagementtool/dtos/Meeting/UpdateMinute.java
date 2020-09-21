package com.arimac.backend.pmtool.projectmanagementtool.dtos.Meeting;

import javax.validation.constraints.NotNull;
import java.sql.Timestamp;

public class UpdateMinute {
    @NotNull
    private String projectId;
    private String description;
    private String remarks;
    private String actionBy;
    private Boolean actionByGuest;
    private Timestamp dueDate;

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getActionBy() {
        return actionBy;
    }

    public void setActionBy(String actionBy) {
        this.actionBy = actionBy;
    }

    public Boolean isActionByGuest() {
        return actionByGuest;
    }

    public void setActionByGuest(Boolean actionByGuest) {
        this.actionByGuest = actionByGuest;
    }

    public Timestamp getDueDate() {
        return dueDate;
    }

    public void setDueDate(Timestamp dueDate) {
        this.dueDate = dueDate;
    }

    @Override
    public String toString() {
        return "UpdateMinute{" +
                "description='" + description + '\'' +
                ", remarks='" + remarks + '\'' +
                ", actionBy='" + actionBy + '\'' +
                ", actionByGuest=" + actionByGuest +
                ", dueDate=" + dueDate +
                '}';
    }
}
