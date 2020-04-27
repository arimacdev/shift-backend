package com.arimac.backend.pmtool.projectmanagementtool.dtos.Task;

public class TaskParentUpdateDto {
    private String previousParent;
    private String newParent;

    public String getPreviousParent() {
        return previousParent;
    }

    public void setPreviousParent(String previousParent) {
        this.previousParent = previousParent;
    }

    public String getNewParent() {
        return newParent;
    }

    public void setNewParent(String newParent) {
        this.newParent = newParent;
    }

    @Override
    public String toString() {
        return "TaskParentUpdateDto{" +
                "previousParent='" + previousParent + '\'' +
                ", newParent='" + newParent + '\'' +
                '}';
    }
}
