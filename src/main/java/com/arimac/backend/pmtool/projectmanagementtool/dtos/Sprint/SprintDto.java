package com.arimac.backend.pmtool.projectmanagementtool.dtos.Sprint;

public class SprintDto {
    private String projectId;
    private String sprintName;
    private String sprintDescription;
    private String sprintCreatedBy;

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    public String getSprintName() {
        return sprintName;
    }

    public void setSprintName(String sprintName) {
        this.sprintName = sprintName;
    }

    public String getSprintDescription() {
        return sprintDescription;
    }

    public void setSprintDescription(String sprintDescription) {
        this.sprintDescription = sprintDescription;
    }

    public String getSprintCreatedBy() {
        return sprintCreatedBy;
    }

    public void setSprintCreatedBy(String sprintCreatedBy) {
        this.sprintCreatedBy = sprintCreatedBy;
    }

    @Override
    public String toString() {
        return "SprintDto{" +
                "projectId='" + projectId + '\'' +
                ", sprintName='" + sprintName + '\'' +
                ", sprintDescription='" + sprintDescription + '\'' +
                ", sprintCreatedBy='" + sprintCreatedBy + '\'' +
                '}';
    }
}
