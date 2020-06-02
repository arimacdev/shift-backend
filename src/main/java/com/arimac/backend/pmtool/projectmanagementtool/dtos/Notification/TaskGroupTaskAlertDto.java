package com.arimac.backend.pmtool.projectmanagementtool.dtos.Notification;

import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

public class TaskGroupTaskAlertDto implements RowMapper<TaskGroupTaskAlertDto> {
    private String taskId;
    private String taskGroupId;
    private String assigneeId;
    private String taskName;
    private Timestamp taskReminder;
    private Timestamp taskDue;
    private String taskGroupName;
    private String assigneeFirstName;
    private String assigneeLastName;
    private String assigneeSlackId;
    private boolean assigneeNotification;

    private String notificationId;
    private boolean hourly;
    private boolean daily;


    public TaskGroupTaskAlertDto() {
    }

    public TaskGroupTaskAlertDto(String taskId, String taskGroupId, String assigneeId, String taskName, Timestamp taskReminder, Timestamp taskDue, String taskGroupName, String assigneeFirstName, String assigneeLastName, String assigneeSlackId, boolean assigneeNotification, String notificationId, boolean hourly, boolean daily) {
        this.taskId = taskId;
        this.taskGroupId = taskGroupId;
        this.assigneeId = assigneeId;
        this.taskName = taskName;
        this.taskReminder = taskReminder;
        this.taskDue = taskDue;
        this.taskGroupName = taskGroupName;
        this.assigneeFirstName = assigneeFirstName;
        this.assigneeLastName = assigneeLastName;
        this.assigneeSlackId = assigneeSlackId;
        this.assigneeNotification = assigneeNotification;
        this.notificationId = notificationId;
        this.hourly = hourly;
        this.daily = daily;
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

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

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public Timestamp getTaskReminder() {
        return taskReminder;
    }

    public void setTaskReminder(Timestamp taskReminder) {
        this.taskReminder = taskReminder;
    }

    public Timestamp getTaskDue() {
        return taskDue;
    }

    public void setTaskDue(Timestamp taskDue) {
        this.taskDue = taskDue;
    }

    public String getTaskGroupName() {
        return taskGroupName;
    }

    public void setTaskGroupName(String taskGroupName) {
        this.taskGroupName = taskGroupName;
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

    public String getAssigneeSlackId() {
        return assigneeSlackId;
    }

    public void setAssigneeSlackId(String assigneeSlackId) {
        this.assigneeSlackId = assigneeSlackId;
    }

    public boolean isAssigneeNotification() {
        return assigneeNotification;
    }

    public void setAssigneeNotification(boolean assigneeNotification) {
        this.assigneeNotification = assigneeNotification;
    }

    public String getNotificationId() {
        return notificationId;
    }

    public void setNotificationId(String notificationId) {
        this.notificationId = notificationId;
    }

    public boolean getIsHourly() {
        return hourly;
    }

    public void setHourly(boolean hourly) {
        this.hourly = hourly;
    }

    public boolean getIsDaily() {
        return daily;
    }

    public void setDaily(boolean daily) {
        this.daily = daily;
    }

    @Override
    public TaskGroupTaskAlertDto mapRow(ResultSet resultSet, int i) throws SQLException {
        return new TaskGroupTaskAlertDto(
                resultSet.getString("taskId"),
                resultSet.getString("taskGroupId"),
                resultSet.getString("taskAssignee"),
                resultSet.getString("taskName"),
                resultSet.getTimestamp("taskReminderAt"),
                resultSet.getTimestamp("taskDueDateAt"),
                resultSet.getString("taskGroupName"),
                resultSet.getString("firstName"),
                resultSet.getString("lastName"),
                resultSet.getString("userSlackId"),
                resultSet.getBoolean("notification"),
                resultSet.getString("notificationId"),
                resultSet.getBoolean("hourly"),
                resultSet.getBoolean("daily")
        );
    }
}
