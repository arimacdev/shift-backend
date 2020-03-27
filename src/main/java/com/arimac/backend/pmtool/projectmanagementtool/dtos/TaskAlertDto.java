package com.arimac.backend.pmtool.projectmanagementtool.dtos;

import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

public class TaskAlertDto implements RowMapper<TaskAlertDto> {
    private String taskId;
    private String projectId;
    private String assigneeId;
    private String taskName;
    private Timestamp taskReminder;
    private Timestamp taskDue;
    private String projectName;
    private String assigneeFirstName;
    private String assigneeLastName;
    private String assigneeSlackId;
    private boolean assigneeNotification;

    public TaskAlertDto() {
    }

    public TaskAlertDto(String taskId, String projectId, String assigneeId, String taskName, Timestamp taskReminder, Timestamp taskDue, String projectName, String assigneeFirstName, String assigneeLastName, String assigneeSlackId, boolean assigneeNotification) {
        this.taskId = taskId;
        this.projectId = projectId;
        this.assigneeId = assigneeId;
        this.taskName = taskName;
        this.taskReminder = taskReminder;
        this.taskDue = taskDue;
        this.projectName = projectName;
        this.assigneeFirstName = assigneeFirstName;
        this.assigneeLastName = assigneeLastName;
        this.assigneeSlackId = assigneeSlackId;
        this.assigneeNotification = assigneeNotification;
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

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

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
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

    @Override
    public TaskAlertDto mapRow(ResultSet resultSet, int i) throws SQLException {
        return new TaskAlertDto(
                resultSet.getString("taskId"),
                resultSet.getString("projectId"),
                resultSet.getString("taskAssignee"),
                resultSet.getString("taskName"),
                resultSet.getTimestamp("taskReminderAt"),
                resultSet.getTimestamp("taskDueDateAt"),
                resultSet.getString("projectName"),
                resultSet.getString("firstName"),
                resultSet.getString("lastName"),
                resultSet.getString("userSlackId"),
                resultSet.getBoolean("notification")
        );
    }
}