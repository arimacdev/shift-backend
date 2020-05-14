package com.arimac.backend.pmtool.projectmanagementtool.dtos.TaskGroupTask;

import com.arimac.backend.pmtool.projectmanagementtool.enumz.IssueTypeEnum;
import com.arimac.backend.pmtool.projectmanagementtool.enumz.TaskGroupTaskStatusEnum;
import com.arimac.backend.pmtool.projectmanagementtool.enumz.TaskStatusEnum;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

public class TaskGroupTaskUserResponseDto implements RowMapper<TaskGroupTaskUserResponseDto> {
    private String taskId;
    private String taskName;
    private String taskGroupId;
    private String taskAssignee;
    private String taskInitiator;
    private String taskNote;
    private TaskGroupTaskStatusEnum taskStatus;
    private Timestamp taskCreatedAt;
    private Timestamp taskDueDateAt;
    private Timestamp taskReminderAt;
    private boolean isDeleted;
    private String taskAssigneeProfileImage;
    private String parentId;
    @JsonProperty
    private boolean isParent;

    public TaskGroupTaskUserResponseDto() {
    }

    public TaskGroupTaskUserResponseDto(String taskId, String taskName, String taskGroupId, String taskAssignee, String taskInitiator, String taskNote, TaskGroupTaskStatusEnum taskStatus, Timestamp taskCreatedAt, Timestamp taskDueDateAt, Timestamp taskReminderAt, boolean isDeleted, String taskAssigneeProfileImage, String parentId, boolean isParent) {
        this.taskId = taskId;
        this.taskName = taskName;
        this.taskGroupId = taskGroupId;
        this.taskAssignee = taskAssignee;
        this.taskInitiator = taskInitiator;
        this.taskNote = taskNote;
        this.taskStatus = taskStatus;
        this.taskCreatedAt = taskCreatedAt;
        this.taskDueDateAt = taskDueDateAt;
        this.taskReminderAt = taskReminderAt;
        this.isDeleted = isDeleted;
        this.taskAssigneeProfileImage = taskAssigneeProfileImage;
        this.parentId = parentId;
        this.isParent = isParent;
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

    public String getTaskGroupId() {
        return taskGroupId;
    }

    public void setTaskGroupId(String taskGroupId) {
        this.taskGroupId = taskGroupId;
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

    public TaskGroupTaskStatusEnum getTaskStatus() {
        return taskStatus;
    }

    public void setTaskStatus(TaskGroupTaskStatusEnum taskStatus) {
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

    @Override
    public TaskGroupTaskUserResponseDto mapRow(ResultSet resultSet, int i) throws SQLException {
        return new TaskGroupTaskUserResponseDto(
                resultSet.getString("taskId"),
                resultSet.getString("taskName"),
                resultSet.getString("taskGroupId"),
                resultSet.getString("taskAssignee"),
                resultSet.getString("taskInitiator"),
                resultSet.getString("taskNote"),
                TaskGroupTaskStatusEnum.valueOf(resultSet.getString("taskStatus")),
                resultSet.getTimestamp("taskCreatedAt"),
                resultSet.getTimestamp("taskDueDateAt"),
                resultSet.getTimestamp("taskReminderAt"),
                resultSet.getBoolean("isDeleted"),
                resultSet.getString("profileImage"),
                resultSet.getString("parentId"),
                resultSet.getBoolean("isParent")
        );
    }
}
