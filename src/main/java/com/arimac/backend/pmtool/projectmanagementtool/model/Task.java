package com.arimac.backend.pmtool.projectmanagementtool.model;

import com.arimac.backend.pmtool.projectmanagementtool.enumz.IssueTypeEnum;
import com.arimac.backend.pmtool.projectmanagementtool.enumz.TaskStatusEnum;
import com.arimac.backend.pmtool.projectmanagementtool.enumz.TaskTypeEnum;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

public class Task implements RowMapper<Task> {

    private String taskId;
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
    private TaskTypeEnum taskType;

    private IssueTypeEnum issueType;
    private String parentId;
    private boolean isParent;

    public Task() {
    }

    public Task(String taskId, String taskName, String projectId, String sprintId, String taskAssignee, String taskInitiator, String taskNote, Timestamp taskCreatedAt, Timestamp taskDueDateAt, Timestamp taskReminderAt, boolean isDeleted, TaskStatusEnum taskStatus, TaskTypeEnum taskType, IssueTypeEnum issueType, String parentId, boolean isParent) {
        this.taskId = taskId;
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
        this.taskType = taskType;
        this.issueType = issueType;
        this.parentId = parentId;
        this.isParent = isParent;
    }

    public boolean getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(boolean deleted) {
        isDeleted = deleted;
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

    public TaskTypeEnum getTaskType() {
        return taskType;
    }

    public void setTaskType(TaskTypeEnum taskType) {
        this.taskType = taskType;
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

    @Override
    public Task mapRow(ResultSet resultSet, int i) throws SQLException {
        return new Task(
                resultSet.getString("taskId"),
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
                TaskTypeEnum.valueOf(resultSet.getString("taskType")),
                IssueTypeEnum.valueOf(resultSet.getString("issueType")),
                resultSet.getString("parentId"),
                resultSet.getBoolean("isParent")
        );
    }

}
