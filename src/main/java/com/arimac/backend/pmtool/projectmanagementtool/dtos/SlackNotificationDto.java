package com.arimac.backend.pmtool.projectmanagementtool.dtos;

public class SlackNotificationDto {
    private String slackAssignerId;
    private String slackAssigneeId;
    private String assigneeSlackId;
    private boolean notificationStatus;

    public String getSlackAssignerId() {
        return slackAssignerId;
    }

    public void setSlackAssignerId(String slackAssignerId) {
        this.slackAssignerId = slackAssignerId;
    }

    public String getSlackAssigneeId() {
        return slackAssigneeId;
    }

    public void setSlackAssigneeId(String slackAssigneeId) {
        this.slackAssigneeId = slackAssigneeId;
    }

    public String getAssigneeSlackId() {
        return assigneeSlackId;
    }

    public void setAssigneeSlackId(String assigneeSlackId) {
        this.assigneeSlackId = assigneeSlackId;
    }

    public boolean getNotificationStatus() {
        return notificationStatus;
    }

    public void setNotificationStatus(boolean notificationStatus) {
        this.notificationStatus = notificationStatus;
    }

    @Override
    public String toString() {
        return "SlackNotificationDto{" +
                "slackAssignerId='" + slackAssignerId + '\'' +
                ", slackAssigneeId='" + slackAssigneeId + '\'' +
                ", assigneeSlackId='" + assigneeSlackId + '\'' +
                ", notificationStatus=" + notificationStatus +
                '}';
    }
}