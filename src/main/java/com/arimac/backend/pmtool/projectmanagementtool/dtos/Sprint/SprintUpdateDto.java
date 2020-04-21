package com.arimac.backend.pmtool.projectmanagementtool.dtos.Sprint;

public class SprintUpdateDto {
    private String sprintName;
    private String sprintDescription;

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

    @Override
    public String toString() {
        return "SprintUpdateDto{" +
                "sprintName='" + sprintName + '\'' +
                ", sprintDescription='" + sprintDescription + '\'' +
                '}';
    }
}
