package com.arimac.backend.pmtool.projectmanagementtool.model;

import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

public class TaskLog implements RowMapper<TaskLog> {
    private String taskLogId;
    private String tasklogInitiator;
    private String projectId;
    private int taskLogEntity;
    private int operation;
    private String taskLogEntityId;
    private String modifiedField;
    private String previous;
    private String modified;
    private Timestamp timestamp;


    public TaskLog() {
    }

    public TaskLog(String taskLogId, String tasklogInitiator, String projectId, int taskLogEntity, int operation, String taskLogEntityId, String modifiedField, String previous, String modified, Timestamp timestamp) {
        this.taskLogId = taskLogId;
        this.tasklogInitiator = tasklogInitiator;
        this.projectId = projectId;
        this.taskLogEntity = taskLogEntity;
        this.operation = operation;
        this.taskLogEntityId = taskLogEntityId;
        this.modifiedField = modifiedField;
        this.previous = previous;
        this.modified = modified;
        this.timestamp = timestamp;
    }

    public String getModifiedField() {
        return modifiedField;
    }

    public void setModifiedField(String modifiedField) {
        this.modifiedField = modifiedField;
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

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    public int getTaskLogEntity() {
        return taskLogEntity;
    }

    public void setTaskLogEntity(int taskLogEntity) {
        this.taskLogEntity = taskLogEntity;
    }

    public String getTaskLogEntityId() {
        return taskLogEntityId;
    }

    public void setTaskLogEntityId(String taskLogEntityId) {
        this.taskLogEntityId = taskLogEntityId;
    }

    public int getOperation() {
        return operation;
    }

    public void setOperation(int operation) {
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
                resultSet.getInt("taskLogEntity"),
                resultSet.getInt("operation"),
                resultSet.getString("taskLogEntityId"),
                resultSet.getString("modifiedField"),
                resultSet.getString("previous"),
                resultSet.getString("modified"),
                resultSet.getTimestamp("timestamp")
        );
    }
}

