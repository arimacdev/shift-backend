package com.arimac.backend.pmtool.projectmanagementtool.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

public class Notification implements RowMapper<Notification> {
    private String notificationId;
    private String assigneeId;
    private String taskId;
    private Timestamp taskDueDateAt;
    private boolean hourly;
    private boolean daily;

    public Notification() {
    }

    public Notification(String notificationId, String assigneeId, String taskId, Timestamp taskDueDateAt, boolean hourly, boolean daily) {
        this.notificationId = notificationId;
        this.assigneeId = assigneeId;
        this.taskId = taskId;
        this.taskDueDateAt = taskDueDateAt;
        this.hourly = hourly;
        this.daily = daily;
    }

    public String getNotificationId() {
        return notificationId;
    }

    public void setNotificationId(String notificationId) {
        this.notificationId = notificationId;
    }

    public String getAssigneeId() {
        return assigneeId;
    }

    public void setAssigneeId(String assigneeId) {
        this.assigneeId = assigneeId;
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public Timestamp getTaskDueDateAt() {
        return taskDueDateAt;
    }

    public void setTaskDueDateAt(Timestamp taskDueDateAt) {
        this.taskDueDateAt = taskDueDateAt;
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
    public Notification mapRow(ResultSet resultSet, int i) throws SQLException {
        return new Notification(
                resultSet.getString("notificationId"),
                resultSet.getString("assigneeId"),
                resultSet.getString("taskId"),
                resultSet.getTimestamp("taskDueDate"),
                resultSet.getBoolean("hourly"),
                resultSet.getBoolean("daily")
        );
    }

    @Override
    public String toString() {
        return "Notification{" +
                "notificationId='" + notificationId + '\'' +
                ", assigneeId='" + assigneeId + '\'' +
                ", taskId='" + taskId + '\'' +
                ", taskDueDateAt=" + taskDueDateAt +
                ", hourly=" + hourly +
                ", daily=" + daily +
                '}';
    }
}
