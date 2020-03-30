package com.arimac.backend.pmtool.projectmanagementtool.dtos;

public class ProjectUserDeleteDto {
    private String assignerId;

    public String getAssignerId() {
        return assignerId;
    }

    public void setAssignerId(String assignerId) {
        this.assignerId = assignerId;
    }

    @Override
    public String toString() {
        return "ProjectUserDeleteDto{" +
                "assignerId='" + assignerId + '\'' +
                '}';
    }
}
