package com.arimac.backend.pmtool.projectmanagementtool.dtos.Sprint;

public class TaskSprintUpdateDto {
    private String previousSprint;
    private String newSprint;

    public String getPreviousSprint() {
        return previousSprint;
    }

    public void setPreviousSprint(String previousSprint) {
        this.previousSprint = previousSprint;
    }

    public String getNewSprint() {
        return newSprint;
    }

    public void setNewSprint(String newSprint) {
        this.newSprint = newSprint;
    }

    @Override
    public String toString() {
        return "TaskSprintUpdateDto{" +
                "previousSprint='" + previousSprint + '\'' +
                ", newSprint='" + newSprint + '\'' +
                '}';
    }
}
