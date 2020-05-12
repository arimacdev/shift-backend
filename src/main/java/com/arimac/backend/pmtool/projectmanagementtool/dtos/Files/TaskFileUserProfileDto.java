package com.arimac.backend.pmtool.projectmanagementtool.dtos.Files;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

public class TaskFileUserProfileDto implements RowMapper<TaskFileUserProfileDto> {
    private String userId;
    private String firstName;
    private String lastName;
    private String profileImage;

    private String taskFileId;
    private String taskId;
    private String taskFileName;
    private String taskFileUrl;
    private String taskFileCreator;
    private int taskFileSize;
    private Timestamp taskFileDate;
    private boolean isDeleted;

    public TaskFileUserProfileDto() {
    }

    public TaskFileUserProfileDto(String userId, String firstName, String lastName, String profileImage, String taskFileId, String taskId, String taskFileName, String taskFileUrl, String taskFileCreator, int taskFileSize, Timestamp taskFileDate, boolean isDeleted) {
        this.userId = userId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.profileImage = profileImage;
        this.taskFileId = taskFileId;
        this.taskId = taskId;
        this.taskFileName = taskFileName;
        this.taskFileUrl = taskFileUrl;
        this.taskFileCreator = taskFileCreator;
        this.taskFileSize = taskFileSize;
        this.taskFileDate = taskFileDate;
        this.isDeleted = isDeleted;
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

    public String getTaskFileId() {
        return taskFileId;
    }

    public void setTaskFileId(String taskFileId) {
        this.taskFileId = taskFileId;
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public String getTaskFileName() {
        return taskFileName;
    }

    public void setTaskFileName(String taskFileName) {
        this.taskFileName = taskFileName;
    }

    public String getTaskFileUrl() {
        return taskFileUrl;
    }

    public void setTaskFileUrl(String taskFileUrl) {
        this.taskFileUrl = taskFileUrl;
    }

    public String getTaskFileCreator() {
        return taskFileCreator;
    }

    public void setTaskFileCreator(String taskFileCreator) {
        this.taskFileCreator = taskFileCreator;
    }

    public int getTaskFileSize() {
        return taskFileSize;
    }

    public void setTaskFileSize(int taskFileSize) {
        this.taskFileSize = taskFileSize;
    }

    public Timestamp getTaskFileDate() {
        return taskFileDate;
    }

    public void setTaskFileDate(Timestamp taskFileDate) {
        this.taskFileDate = taskFileDate;
    }

    public boolean isDeleted() {
        return isDeleted;
    }

    public void setDeleted(boolean deleted) {
        isDeleted = deleted;
    }


    @Override
    public TaskFileUserProfileDto mapRow(ResultSet resultSet, int i) throws SQLException {
        return new TaskFileUserProfileDto(
                resultSet.getString("userId"),
                resultSet.getString("firstName"),
                resultSet.getString("lastName"),
                resultSet.getString("profileImage"),
                resultSet.getString("taskFileId"),
                resultSet.getString("taskId"),
                resultSet.getString("taskFileName"),
                resultSet.getString("taskFileUrl"),
                resultSet.getString("taskFileCreator"),
                resultSet.getInt("taskFileSize"),
                resultSet.getTimestamp("taskFileDate"),
                resultSet.getBoolean("isDeleted")
        );
    }
}
