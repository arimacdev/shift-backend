package com.arimac.backend.pmtool.projectmanagementtool.dtos.ServiceDesk;

import javax.validation.constraints.NotNull;

public class SupportTaskLink {
    @NotNull
    private String projectId;
    @NotNull
    private String fromTask;
    @NotNull
    private String toTask;

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    public String getFromTask() {
        return fromTask;
    }

    public void setFromTask(String fromTask) {
        this.fromTask = fromTask;
    }

    public String getToTask() {
        return toTask;
    }

    public void setToTask(String toTask) {
        this.toTask = toTask;
    }

    @Override
    public String toString() {
        return "SupportTaskLink{" +
                "projectId='" + projectId + '\'' +
                ", fromTask='" + fromTask + '\'' +
                ", toTask='" + toTask + '\'' +
                '}';
    }
}
