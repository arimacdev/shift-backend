package com.arimac.backend.pmtool.projectmanagementtool.dtos;

import java.sql.Timestamp;

public class TaskAssignNotificationDto {
    private String slackUserId;
    private String assignerId;
    private String assigneeId;
    private String taskName;
    private String projectName;
    private Timestamp assignedAt;

    public String getSlackUserId() {
        return slackUserId;
    }

    public void setSlackUserId(String slackUserId) {
        this.slackUserId = slackUserId;
    }

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

    public Timestamp getAssignedAt() {
        return assignedAt;
    }

    public void setAssignedAt(Timestamp assignedAt) {
        this.assignedAt = assignedAt;
    }


    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    @Override
    public String toString() {
        return "TaskAssignNotificationDto{" +
                "slackUserId='" + slackUserId + '\'' +
                ", assignerId='" + assignerId + '\'' +
                ", assigneeId='" + assigneeId + '\'' +
                ", taskName='" + taskName + '\'' +
                ", projectName='" + projectName + '\'' +
                ", assignedAt=" + assignedAt +
                '}';
    }
}
