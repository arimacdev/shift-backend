package com.arimac.backend.pmtool.projectmanagementtool.dtos.TaskGroup;

public class UserTaskGroupResponseDto {
    private String taskGroupId;
    private String assigneeId;
    private String assigneeFirstName;
    private String assigneeLastName;
    private String assigneeProfileImage;
    private int taskGroupRole;
    private int tasksCompleted;
    private int totalTasks;

    public String getTaskGroupId() {
        return taskGroupId;
    }

    public void setTaskGroupId(String taskGroupId) {
        this.taskGroupId = taskGroupId;
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

    public String getAssigneeProfileImage() {
        return assigneeProfileImage;
    }

    public void setAssigneeProfileImage(String assigneeProfileImage) {
        this.assigneeProfileImage = assigneeProfileImage;
    }

    public int getTaskGroupRole() {
        return taskGroupRole;
    }

    public void setTaskGroupRole(int taskGroupRole) {
        this.taskGroupRole = taskGroupRole;
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
}
