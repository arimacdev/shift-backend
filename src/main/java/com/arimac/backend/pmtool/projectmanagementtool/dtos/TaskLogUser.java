package com.arimac.backend.pmtool.projectmanagementtool.dtos;

import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

public class TaskLogUser  implements RowMapper<TaskLogUser> {
    private String taskLogId;
    private String tasklogInitiator;
    private String projectId;
    private int taskLogEntity;
    private String taskLogEntityId;
    private int operation;
    private String previous;
    private String modified;
    private Timestamp timestamp;
    private String userId;
    private String firstName;
    private String lastName;
    private String profileImage;

    public TaskLogUser() {
    }

    public TaskLogUser(String taskLogId, String tasklogInitiator, String projectId, int taskLogEntity, String taskLogEntityId, int operation, String previous, String modified, Timestamp timestamp, String userId, String firstName, String lastName, String profileImage) {
        this.taskLogId = taskLogId;
        this.tasklogInitiator = tasklogInitiator;
        this.projectId = projectId;
        this.taskLogEntity = taskLogEntity;
        this.taskLogEntityId = taskLogEntityId;
        this.operation = operation;
        this.previous = previous;
        this.modified = modified;
        this.timestamp = timestamp;
        this.userId = userId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.profileImage = profileImage;
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

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }

    @Override
    public TaskLogUser mapRow(ResultSet resultSet, int i) throws SQLException {
        return new TaskLogUser(
                resultSet.getString("taskLogId"),
                resultSet.getString("tasklogInitiator"),
                resultSet.getString("projectId"),
                resultSet.getInt("taskLogEntity"),
                resultSet.getString("taskLogEntityId"),
                resultSet.getInt("operation"),
                resultSet.getString("previous"),
                resultSet.getString("modified"),
                resultSet.getTimestamp("timestamp"),
                resultSet.getString("userId"),
                resultSet.getString("firstName"),
                resultSet.getString("lastName"),
                resultSet.getString("profileImage")
        );
    }
}
