package com.arimac.backend.pmtool.projectmanagementtool.dtos.Analytics.User;

import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDetailedAnalysis implements RowMapper<UserDetailedAnalysis> {
    private String userId;
    private String firstName;
    private String lastName;
    private String profileImage;
    private int projectCount;
    private int activeProjectCount;
    private int taskGroupCount;
    private int assignedTasks;
    private int taskGroupTaskCount;
    private int personalTaskCount;

    public UserDetailedAnalysis() {
    }

    public UserDetailedAnalysis(String userId, String firstName, String lastName, String profileImage, int projectCount, int activeProjectCount, int taskGroupCount, int assignedTasks, int taskGroupTaskCount, int personalTaskCount) {
        this.userId = userId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.profileImage = profileImage;
        this.projectCount = projectCount;
        this.activeProjectCount = activeProjectCount;
        this.taskGroupCount = taskGroupCount;
        this.assignedTasks = assignedTasks;
        this.taskGroupTaskCount = taskGroupTaskCount;
        this.personalTaskCount = personalTaskCount;
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

    public int getProjectCount() {
        return projectCount;
    }

    public void setProjectCount(int projectCount) {
        this.projectCount = projectCount;
    }

    public int getActiveProjectCount() {
        return activeProjectCount;
    }

    public void setActiveProjectCount(int activeProjectCount) {
        this.activeProjectCount = activeProjectCount;
    }

    public int getTaskGroupCount() {
        return taskGroupCount;
    }

    public void setTaskGroupCount(int taskGroupCount) {
        this.taskGroupCount = taskGroupCount;
    }

    public int getAssignedTasks() {
        return assignedTasks;
    }

    public void setAssignedTasks(int assignedTasks) {
        this.assignedTasks = assignedTasks;
    }

    public int getTaskGroupTaskCount() {
        return taskGroupTaskCount;
    }

    public void setTaskGroupTaskCount(int taskGroupTaskCount) {
        this.taskGroupTaskCount = taskGroupTaskCount;
    }

    public int getPersonalTaskCount() {
        return personalTaskCount;
    }

    public void setPersonalTaskCount(int personalTaskCount) {
        this.personalTaskCount = personalTaskCount;
    }

    @Override
    public UserDetailedAnalysis mapRow(ResultSet resultSet, int i) throws SQLException {
        return new UserDetailedAnalysis(
                resultSet.getString("userId"),
                resultSet.getString("firstName"),
                resultSet.getString("lastName"),
                resultSet.getString("profileImage"),
                resultSet.getInt("projectCount"),
                resultSet.getInt("activeProjectCount"),
                resultSet.getInt("taskGroupCount"),
                resultSet.getInt("assignedTasks"),
                resultSet.getInt("taskGroupTaskCount"),
                resultSet.getInt("personalTaskCount")
        );
    }
}


