package com.arimac.backend.pmtool.projectmanagementtool.dtos;

import com.arimac.backend.pmtool.projectmanagementtool.enumz.IssueTypeEnum;
import com.arimac.backend.pmtool.projectmanagementtool.enumz.TaskStatusEnum;
import org.springframework.jdbc.core.RowMapper;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

public class TaskUserResponseDto implements RowMapper<TaskUserResponseDto> {
    private String taskId;
    private String secondaryTaskId;
    private String taskName;
    private String projectId;
    private String taskAssignee;
    private String taskInitiator;
    private String taskNote;
    private TaskStatusEnum taskStatus;
    private Timestamp taskCreatedAt;
    private Timestamp taskDueDateAt;
    private Timestamp taskReminderAt;
    private boolean isDeleted;
    private String  firstName;
    private String lastName;
    private String taskAssigneeProfileImage;
    private String sprintId;

    private IssueTypeEnum issueType;
    private String parentId;
    private boolean isParent;

    private BigDecimal estimatedWeight;
    private BigDecimal actualWeight;

    public TaskUserResponseDto() {
    }

    public TaskUserResponseDto(String taskId, String secondaryTaskId, String taskName, String projectId, String taskAssignee, String taskInitiator, String taskNote, TaskStatusEnum taskStatus, Timestamp taskCreatedAt, Timestamp taskDueDateAt, Timestamp taskReminderAt, boolean isDeleted, String firstName, String lastName, String taskAssigneeProfileImage, String sprintId, IssueTypeEnum issueType, String parentId, boolean isParent, BigDecimal estimatedWeight, BigDecimal actualWeight) {
        this.taskId = taskId;
        this.secondaryTaskId = secondaryTaskId;
        this.taskName = taskName;
        this.projectId = projectId;
        this.taskAssignee = taskAssignee;
        this.taskInitiator = taskInitiator;
        this.taskNote = taskNote;
        this.taskStatus = taskStatus;
        this.taskCreatedAt = taskCreatedAt;
        this.taskDueDateAt = taskDueDateAt;
        this.taskReminderAt = taskReminderAt;
        this.isDeleted = isDeleted;
        this.firstName = firstName;
        this.lastName = lastName;
        this.taskAssigneeProfileImage = taskAssigneeProfileImage;
        this.sprintId = sprintId;
        this.issueType = issueType;
        this.parentId = parentId;
        this.isParent = isParent;
        this.estimatedWeight = estimatedWeight;
        this.actualWeight = actualWeight;
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
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

    public TaskStatusEnum getTaskStatus() {
        return taskStatus;
    }

    public void setTaskStatus(TaskStatusEnum taskStatus) {
        this.taskStatus = taskStatus;
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

    public String getTaskAssigneeProfileImage() {
        return taskAssigneeProfileImage;
    }

    public void setTaskAssigneeProfileImage(String taskAssigneeProfileImage) {
        this.taskAssigneeProfileImage = taskAssigneeProfileImage;
    }

    public String getSprintId() {
        return sprintId;
    }

    public void setSprintId(String sprintId) {
        this.sprintId = sprintId;
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

    public boolean getIsParent() {
        return isParent;
    }

    public void setIsParent(boolean parent) {
        isParent = parent;
    }

    public String getSecondaryTaskId() {
        return secondaryTaskId;
    }

    public void setSecondaryTaskId(String secondaryTaskId) {
        this.secondaryTaskId = secondaryTaskId;
    }

    public BigDecimal getEstimatedWeight() {
        return estimatedWeight;
    }

    public void setEstimatedWeight(BigDecimal estimatedWeight) {
        this.estimatedWeight = estimatedWeight;
    }

    public BigDecimal getActualWeight() {
        return actualWeight;
    }

    public void setActualWeight(BigDecimal actualWeight) {
        this.actualWeight = actualWeight;
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

    @Override
    public TaskUserResponseDto mapRow(ResultSet resultSet, int i) throws SQLException {
        return new TaskUserResponseDto(
                resultSet.getString("taskId"),
                resultSet.getString("secondaryTaskId"),
                resultSet.getString("taskName"),
                resultSet.getString("projectId"),
                resultSet.getString("taskAssignee"),
                resultSet.getString("taskInitiator"),
                resultSet.getString("taskNote"),
                TaskStatusEnum.valueOf(resultSet.getString("taskStatus")),
                resultSet.getTimestamp("taskCreatedAt"),
                resultSet.getTimestamp("taskDueDateAt"),
                resultSet.getTimestamp("taskReminderAt"),
                resultSet.getBoolean("isDeleted"),
                resultSet.getString("firstName"),
                resultSet.getString("lastName"),
                resultSet.getString("profileImage"),
                resultSet.getString("sprintId"),
                IssueTypeEnum.valueOf(resultSet.getString("issueType")),
                resultSet.getString("parentId"),
                resultSet.getBoolean("isParent"),
                resultSet.getBigDecimal("estimatedWeight"),
                resultSet.getBigDecimal("actualWeight")
        );
    }

}
