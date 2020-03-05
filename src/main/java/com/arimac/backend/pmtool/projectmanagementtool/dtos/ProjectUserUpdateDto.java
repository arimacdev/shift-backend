package com.arimac.backend.pmtool.projectmanagementtool.dtos;

public class ProjectUserUpdateDto {
    private String assignerId;
    private int assigneeProjectRole;
    private String assigneeJobRole;

    public String getAssignerId() {
        return assignerId;
    }

    public void setAssignerId(String assignerId) {
        this.assignerId = assignerId;
    }

    public int getAssigneeProjectRole() {
        return assigneeProjectRole;
    }

    public void setAssigneeProjectRole(int assigneeProjectRole) {
        this.assigneeProjectRole = assigneeProjectRole;
    }

    public String getAssigneeJobRole() {
        return assigneeJobRole;
    }

    public void setAssigneeJobRole(String assigneeJobRole) {
        this.assigneeJobRole = assigneeJobRole;
    }
}
