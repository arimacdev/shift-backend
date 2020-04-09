package com.arimac.backend.pmtool.projectmanagementtool.model;

import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

public class TaskGroup implements RowMapper<TaskGroup> {
    private String taskGroupId;
    private String taskGroupName;
    private Timestamp taskGroupCreatedAt;
    private boolean isDeleted;

    public TaskGroup() {
    }

    public TaskGroup(String taskGroupId, String taskGroupName, Timestamp taskGroupCreatedAt, boolean isDeleted) {
        this.taskGroupId = taskGroupId;
        this.taskGroupName = taskGroupName;
        this.taskGroupCreatedAt = taskGroupCreatedAt;
        this.isDeleted = isDeleted;
    }

    public String getTaskGroupId() {
        return taskGroupId;
    }

    public void setTaskGroupId(String taskGroupId) {
        this.taskGroupId = taskGroupId;
    }

    public String getTaskGroupName() {
        return taskGroupName;
    }

    public void setTaskGroupName(String taskGroupName) {
        this.taskGroupName = taskGroupName;
    }

    public Timestamp getTaskGroupCreatedAt() {
        return taskGroupCreatedAt;
    }

    public void setTaskGroupCreatedAt(Timestamp taskGroupCreatedAt) {
        this.taskGroupCreatedAt = taskGroupCreatedAt;
    }

    public boolean getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(boolean deleted) {
        isDeleted = deleted;
    }

    @Override
    public TaskGroup mapRow(ResultSet resultSet, int i) throws SQLException {
        return new TaskGroup(
                resultSet.getString("taskGroupId"),
                resultSet.getString("taskGroupName"),
                resultSet.getTimestamp("taskGroupCreatedAt"),
                resultSet.getBoolean("isDeleted")
        );
    }
}