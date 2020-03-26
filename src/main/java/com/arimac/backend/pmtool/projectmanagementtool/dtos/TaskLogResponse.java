package com.arimac.backend.pmtool.projectmanagementtool.dtos;

import java.sql.Timestamp;

public class TaskLogResponse {
    //TaskLog Details
    private String taskLogId;
    private String projectId;
    private String modifiedField;
    private String previous;
    private String modified;
    private Timestamp timestamp;
    //Initiator Details
    private String tasklogInitiator; // same
    private String userId; //same
    private String firstName;
    private String lastName;
    private String profileImage;
    //Entity Details
    private int taskLogEntity;
    private int operation;
    private String taskLogEntityId;
    private String taskLogEntityName;
    private String taskLogAdditional;

    public String getTaskLogId() {
        return taskLogId;
    }

    public void setTaskLogId(String taskLogId) {
        this.taskLogId = taskLogId;
    }

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
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

    public String getTasklogInitiator() {
        return tasklogInitiator;
    }

    public void setTasklogInitiator(String tasklogInitiator) {
        this.tasklogInitiator = tasklogInitiator;
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

    public int getTaskLogEntity() {
        return taskLogEntity;
    }

    public void setTaskLogEntity(int taskLogEntity) {
        this.taskLogEntity = taskLogEntity;
    }

    public int getOperation() {
        return operation;
    }

    public void setOperation(int operation) {
        this.operation = operation;
    }

    public String getTaskLogEntityId() {
        return taskLogEntityId;
    }

    public void setTaskLogEntityId(String taskLogEntityId) {
        this.taskLogEntityId = taskLogEntityId;
    }

    public String getTaskLogEntityName() {
        return taskLogEntityName;
    }

    public void setTaskLogEntityName(String taskLogEntityName) {
        this.taskLogEntityName = taskLogEntityName;
    }

    public String getTaskLogAdditional() {
        return taskLogAdditional;
    }

    public void setTaskLogAdditional(String taskLogAdditional) {
        this.taskLogAdditional = taskLogAdditional;
    }

    public String getModifiedField() {
        return modifiedField;
    }

    public void setModifiedField(String modifiedField) {
        this.modifiedField = modifiedField;
    }
}
