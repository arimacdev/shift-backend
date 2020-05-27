package com.arimac.backend.pmtool.projectmanagementtool.dtos;

import com.arimac.backend.pmtool.projectmanagementtool.enumz.IssueTypeEnum;
import com.arimac.backend.pmtool.projectmanagementtool.enumz.TaskStatusEnum;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

public class TaskUserDto implements RowMapper<TaskUserDto> {
    private String taskId;
    private String secondaryTaskId;
    private String taskName;
    private String projectId;
    private String sprintId;
    private String taskAssignee;
    private String taskInitiator;
    private String taskNote;
    private Timestamp taskCreatedAt;
    private Timestamp taskDueDateAt;
    private Timestamp taskReminderAt;
    private boolean isDeleted;
    private TaskStatusEnum taskStatus;

    private IssueTypeEnum issueType;
    private String parentId;
    private boolean isParent;

    private String firstName;
    private String lastName;
    private String profileImage;


    public TaskUserDto() {
    }

    public TaskUserDto(String taskId, String secondaryTaskId, String taskName, String projectId, String sprintId, String taskAssignee, String taskInitiator, String taskNote, Timestamp taskCreatedAt, Timestamp taskDueDateAt, Timestamp taskReminderAt, boolean isDeleted, TaskStatusEnum taskStatus, IssueTypeEnum issueType, String parentId, boolean isParent, String firstName, String lastName, String profileImage) {
        this.taskId = taskId;
        this.secondaryTaskId = secondaryTaskId;
        this.taskName = taskName;
        this.projectId = projectId;
        this.sprintId = sprintId;
        this.taskAssignee = taskAssignee;
        this.taskInitiator = taskInitiator;
        this.taskNote = taskNote;
        this.taskCreatedAt = taskCreatedAt;
        this.taskDueDateAt = taskDueDateAt;
        this.taskReminderAt = taskReminderAt;
        this.isDeleted = isDeleted;
        this.taskStatus = taskStatus;
        this.issueType = issueType;
        this.parentId = parentId;
        this.isParent = isParent;
        this.firstName = firstName;
        this.lastName = lastName;
        this.profileImage = profileImage;
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public String getSecondaryTaskId() {
        return secondaryTaskId;
    }

    public void setSecondaryTaskId(String secondaryTaskId) {
        this.secondaryTaskId = secondaryTaskId;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    public String getSprintId() {
        return sprintId;
    }

    public void setSprintId(String sprintId) {
        this.sprintId = sprintId;
    }

    public String getTaskAssignee() {
        return taskAssignee;
    }

    public void setTaskAssignee(String taskAssignee) {
        this.taskAssignee = taskAssignee;
    }

    public String getTaskInitiator() {
        return taskInitiator;
    }

    public void setTaskInitiator(String taskInitiator) {
        this.taskInitiator = taskInitiator;
    }

    public String getTaskNote() {
        return taskNote;
    }

    public void setTaskNote(String taskNote) {
        this.taskNote = taskNote;
    }

    public Timestamp getTaskCreatedAt() {
        return taskCreatedAt;
    }

    public void setTaskCreatedAt(Timestamp taskCreatedAt) {
        this.taskCreatedAt = taskCreatedAt;
    }

    public Timestamp getTaskDueDateAt() {
        return taskDueDateAt;
    }

    public void setTaskDueDateAt(Timestamp taskDueDateAt) {
        this.taskDueDateAt = taskDueDateAt;
    }

    public Timestamp getTaskReminderAt() {
        return taskReminderAt;
    }

    public void setTaskReminderAt(Timestamp taskReminderAt) {
        this.taskReminderAt = taskReminderAt;
    }

    public boolean isDeleted() {
        return isDeleted;
    }

    public void setDeleted(boolean deleted) {
        isDeleted = deleted;
    }

    public TaskStatusEnum getTaskStatus() {
        return taskStatus;
    }

    public void setTaskStatus(TaskStatusEnum taskStatus) {
        this.taskStatus = taskStatus;
    }

    public IssueTypeEnum getIssueType() {
        return issueType;
    }

    public void setIssueType(IssueTypeEnum issueType) {
        this.issueType = issueType;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public boolean isParent() {
        return isParent;
    }

    public void setParent(boolean parent) {
        isParent = parent;
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
    public TaskUserDto mapRow(ResultSet resultSet, int i) throws SQLException {
        return new TaskUserDto(
                resultSet.getString("taskId"),
                resultSet.getString("secondaryTaskId"),
                resultSet.getString("taskName"),
                resultSet.getString("projectId"),
                resultSet.getString("sprintId"),
                resultSet.getString("taskAssignee"),
                resultSet.getString("taskInitiator"),
                resultSet.getString("taskNote"),
                resultSet.getTimestamp("taskCreatedAt"),
                resultSet.getTimestamp("taskDueDateAt"),
                resultSet.getTimestamp("taskReminderAt"),
                resultSet.getBoolean("isDeleted"),
                TaskStatusEnum.valueOf(resultSet.getString("taskStatus")),
                IssueTypeEnum.valueOf(resultSet.getString("issueType")),
                resultSet.getString("parentId"),
                resultSet.getBoolean("isParent"),
                resultSet.getString("firstName"),
                resultSet.getString("lastName"),
                resultSet.getString("profileImage")
        );
    }
}
