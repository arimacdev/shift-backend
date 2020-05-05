package com.arimac.backend.pmtool.projectmanagementtool.model;

import com.arimac.backend.pmtool.projectmanagementtool.enumz.IssueTypeEnum;
import com.arimac.backend.pmtool.projectmanagementtool.enumz.TaskGroupTaskStatusEnum;
import com.arimac.backend.pmtool.projectmanagementtool.enumz.TaskStatusEnum;
import com.arimac.backend.pmtool.projectmanagementtool.enumz.TaskTypeEnum;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

public class TaskGroupTask implements RowMapper<TaskGroupTask> {
    private String taskId;
    private String taskName;
    private String taskGroupId;
    private String taskAssignee;
    private String taskInitiator;
    private String taskNote;
    private Timestamp taskCreatedAt;
    private Timestamp taskDueDateAt;
    private Timestamp taskReminderAt;
    private boolean isDeleted;
    private TaskGroupTaskStatusEnum taskStatus;
    private String parentId;
    private boolean isParent;


    public TaskGroupTask() {
    }

    public TaskGroupTask(String taskId, String taskName, String taskGroupId, String taskAssignee, String taskInitiator, String taskNote, Timestamp taskCreatedAt, Timestamp taskDueDateAt, Timestamp taskReminderAt, boolean isDeleted, TaskGroupTaskStatusEnum taskStatus, String parentId, boolean isParent) {
        this.taskId = taskId;
        this.taskName = taskName;
        this.taskGroupId = taskGroupId;
        this.taskAssignee = taskAssignee;
        this.taskInitiator = taskInitiator;
        this.taskNote = taskNote;
        this.taskCreatedAt = taskCreatedAt;
        this.taskDueDateAt = taskDueDateAt;
        this.taskReminderAt = taskReminderAt;
        this.isDeleted = isDeleted;
        this.taskStatus = taskStatus;
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

    public boolean getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(boolean deleted) {
        isDeleted = deleted;
    }

    public TaskGroupTaskStatusEnum getTaskStatus() {
        return taskStatus;
    }

    public void setTaskStatus(TaskGroupTaskStatusEnum taskStatus) {
        this.taskStatus = taskStatus;
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
    public TaskGroupTask mapRow(ResultSet resultSet, int i) throws SQLException {
        return new TaskGroupTask(
                resultSet.getString("taskId"),
                resultSet.getString("taskName"),
                resultSet.getString("taskGroupId"),
                resultSet.getString("taskAssignee"),
                resultSet.getString("taskInitiator"),
                resultSet.getString("taskNote"),
                resultSet.getTimestamp("taskCreatedAt"),
                resultSet.getTimestamp("taskDueDateAt"),
                resultSet.getTimestamp("taskReminderAt"),
                resultSet.getBoolean("isDeleted"),
                TaskGroupTaskStatusEnum.valueOf(resultSet.getString("taskStatus")),
                resultSet.getString("parentId"),
                resultSet.getBoolean("isParent")
        );
    }

}
