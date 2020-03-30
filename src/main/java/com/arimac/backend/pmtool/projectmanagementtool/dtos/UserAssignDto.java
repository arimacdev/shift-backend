package com.arimac.backend.pmtool.projectmanagementtool.dtos;

public class UserAssignDto {
    private String assignerId;
    private String assigneeId;
    private int assigneeProjectRole;
    private String assigneeJobRole;

    public String getAssignerId() {
        return assignerId;
    }

    public void setAssignerId(String assignerId) {
        this.assignerId = assignerId;
    }

    public String getAssigneeId() {
        return assigneeId;
    }

    public void setAssigneeId(String assigneeId) {
        this.assigneeId = assigneeId;
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

    @Override
    public String toString() {
        return "UserAssignDto{" +
                "assignerId='" + assignerId + '\'' +
                ", assigneeId='" + assigneeId + '\'' +
                ", assigneeProjectRole=" + assigneeProjectRole +
                ", assigneeJobRole='" + assigneeJobRole + '\'' +
                '}';
    }
}
