package com.arimac.backend.pmtool.projectmanagementtool.dtos.PersonalTask;

import com.arimac.backend.pmtool.projectmanagementtool.enumz.IssueTypeEnum;
import com.arimac.backend.pmtool.projectmanagementtool.enumz.PersonalTaskEnum;
import com.arimac.backend.pmtool.projectmanagementtool.enumz.TaskStatusEnum;
import com.arimac.backend.pmtool.projectmanagementtool.enumz.TaskTypeEnum;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

public class PersonalTask implements RowMapper<PersonalTask> {
    private String taskId;
    private String taskName;
    private String taskAssignee;
    private String taskNote;
    private Timestamp taskCreatedAt;
    private Timestamp taskDueDateAt;
    private Timestamp taskReminderAt;
    private boolean isDeleted;
    private PersonalTaskEnum taskStatus;

    public PersonalTask() {
    }

    public PersonalTask(String taskId, String taskName, String taskAssignee, String taskNote, Timestamp taskCreatedAt, Timestamp taskDueDateAt, Timestamp taskReminderAt, boolean isDeleted, PersonalTaskEnum taskStatus) {
        this.taskId = taskId;
        this.taskName = taskName;
        this.taskAssignee = taskAssignee;
        this.taskNote = taskNote;
        this.taskCreatedAt = taskCreatedAt;
        this.taskDueDateAt = taskDueDateAt;
        this.taskReminderAt = taskReminderAt;
        this.isDeleted = isDeleted;
        this.taskStatus = taskStatus;
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

    public String getTaskAssignee() {
        return taskAssignee;
    }

    public void setTaskAssignee(String taskAssignee) {
        this.taskAssignee = taskAssignee;
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

    public PersonalTaskEnum getTaskStatus() {
        return taskStatus;
    }

    public void setTaskStatus(PersonalTaskEnum taskStatus) {
        this.taskStatus = taskStatus;
    }

    @Override
    public PersonalTask mapRow(ResultSet resultSet, int i) throws SQLException {
        return new PersonalTask(
                resultSet.getString("taskId"),
                 resultSet.getString("taskName"),
                resultSet.getString("taskAssignee"),
                resultSet.getString("taskNote"),
                resultSet.getTimestamp("taskCreatedAt"),
                resultSet.getTimestamp("taskDueDateAt"),
                resultSet.getTimestamp("taskReminderAt"),
                resultSet.getBoolean("isDeleted"),
                PersonalTaskEnum.valueOf(resultSet.getString("taskStatus"))
        );
    }
}
