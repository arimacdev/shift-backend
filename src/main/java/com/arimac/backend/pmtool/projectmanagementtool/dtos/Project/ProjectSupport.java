package com.arimac.backend.pmtool.projectmanagementtool.dtos.Project;

import javax.validation.constraints.NotNull;

public class ProjectSupport {
    @NotNull
    private String projectId;
    @NotNull
    private String defaultAssignee;

    public String getDefaultAssignee() {
        return defaultAssignee;
    }

    public void setDefaultAssignee(String defaultAssignee) {
        this.defaultAssignee = defaultAssignee;
    }

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    @Override
    public String toString() {
        return "ProjectSupport{" +
                "projectId='" + projectId + '\'' +
                ", defaultAssignee='" + defaultAssignee + '\'' +
                '}';
    }
}
