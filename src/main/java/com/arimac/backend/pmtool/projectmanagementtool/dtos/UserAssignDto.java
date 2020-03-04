package com.arimac.backend.pmtool.projectmanagementtool.dtos;

public class UserAssignDto {
    private String assignerId;
    private String assigneeId;
    private String assigneeProjectRole;
    private boolean isAdmin;

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

    public String getAssigneeProjectRole() {
        return assigneeProjectRole;
    }

    public void setAssigneeProjectRole(String assigneeProjectRole) {
        this.assigneeProjectRole = assigneeProjectRole;
    }

    public boolean getAdmin() {
        return isAdmin;
    }

    public void setAdmin(boolean admin) {
        isAdmin = admin;
    }
}
