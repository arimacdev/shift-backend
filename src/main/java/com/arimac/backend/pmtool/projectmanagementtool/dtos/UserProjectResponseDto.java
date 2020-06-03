package com.arimac.backend.pmtool.projectmanagementtool.dtos;

public class UserProjectResponseDto {
    private String projectId;
    private String assigneeId;
    private String assigneeFirstName;
    private String assigneeLastName;
    private String assigneeProfileImage;
    private String projectRoleName;
    private String projectJobRoleName;
    private String projectRoleId;
    private int tasksCompleted;
    private int totalTasks;
    private boolean isUserBlocked;

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    public String getAssigneeId() {
        return assigneeId;
    }

    public void setAssigneeId(String assigneeId) {
        this.assigneeId = assigneeId;
    }

    public String getAssigneeFirstName() {
        return assigneeFirstName;
    }

    public void setAssigneeFirstName(String assigneeFirstName) {
        this.assigneeFirstName = assigneeFirstName;
    }

    public String getAssigneeLastName() {
        return assigneeLastName;
    }

    public void setAssigneeLastName(String assigneeLastName) {
        this.assigneeLastName = assigneeLastName;
    }

    public String getProjectRoleName() {
        return projectRoleName;
    }

    public void setProjectRoleName(String projectRoleName) {
        this.projectRoleName = projectRoleName;
    }

    public int getTasksCompleted() {
        return tasksCompleted;
    }

    public void setTasksCompleted(int tasksCompleted) {
        this.tasksCompleted = tasksCompleted;
    }

    public int getTotalTasks() {
        return totalTasks;
    }

    public void setTotalTasks(int totalTasks) {
        this.totalTasks = totalTasks;
    }

    public String getProjectJobRoleName() {
        return projectJobRoleName;
    }

    public void setProjectJobRoleName(String projectJobRoleName) {
        this.projectJobRoleName = projectJobRoleName;
    }

    public String getProjectRoleId() {
        return projectRoleId;
    }

    public void setProjectRoleId(String projectRoleId) {
        this.projectRoleId = projectRoleId;
    }

    public String getAssigneeProfileImage() {
        return assigneeProfileImage;
    }

    public void setAssigneeProfileImage(String assigneeProfileImage) {
        this.assigneeProfileImage = assigneeProfileImage;
    }

    public boolean getIsUserBlocked() {
        return isUserBlocked;
    }

    public void setIsUserBlocked(boolean userBlocked) {
        isUserBlocked = userBlocked;
    }
}
