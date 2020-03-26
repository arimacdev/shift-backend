package com.arimac.backend.pmtool.projectmanagementtool.model;

import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

public class TaskLog implements RowMapper<TaskLog> {
    private String taskLogId;
    private String tasklogInitiator;
    private String projectId;
    private String taskLogEntity;
    private String taskLogEntityId;
    private String operation;
    private String previous;
    private String modified;
    private Timestamp timestamp;


    public TaskLog() {
    }

    public TaskLog(String taskLogId, String tasklogInitiator, String projectId, String taskLogEntity, String taskLogEntityId, String operation, String previous, String modified, Timestamp timestamp) {
        this.taskLogId = taskLogId;
        this.tasklogInitiator = tasklogInitiator;
        this.projectId = projectId;
        this.taskLogEntity = taskLogEntity;
        this.taskLogEntityId = taskLogEntityId;
        this.operation = operation;
        this.previous = previous;
        this.modified = modified;
        this.timestamp = timestamp;
    }

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    public String getTaskLogId() {
        return taskLogId;
    }

    public void setTaskLogId(String taskLogId) {
        this.taskLogId = taskLogId;
    }

    public String getTasklogInitiator() {
        return tasklogInitiator;
    }

    public void setTasklogInitiator(String tasklogInitiator) {
        this.tasklogInitiator = tasklogInitiator;
    }

    public String getTaskLogEntity() {
        return taskLogEntity;
    }

    public void setTaskLogEntity(String taskLogEntity) {
        this.taskLogEntity = taskLogEntity;
    }

    public String getTaskLogEntityId() {
        return taskLogEntityId;
    }

    public void setTaskLogEntityId(String taskLogEntityId) {
        this.taskLogEntityId = taskLogEntityId;
    }

    public String getOperation() {
        return operation;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }

    public String getPrevious() {
        return previous;
    }

    public void setPrevious(String previous) {
        this.previous = previous;
    }

    public String getModified() {
        return modified;
    }

    public void setModified(String modified) {
        this.modified = modified;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }


    @Override
    public TaskLog mapRow(ResultSet resultSet, int i) throws SQLException {
        return new TaskLog(
                resultSet.getString("taskLogId"),
                resultSet.getString("tasklogInitiator"),
                resultSet.getString("projectId"),
                resultSet.getString("taskLogEntity"),
                resultSet.getString("taskLogEntityId"),
                resultSet.getString("operation"),
                resultSet.getString("previous"),
                resultSet.getString("modified"),
                resultSet.getTimestamp("timestamp")
        );
    }
}
