package com.arimac.backend.pmtool.projectmanagementtool.dtos;
import com.arimac.backend.pmtool.projectmanagementtool.enumz.IssueTypeEnum;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

public class WorkLoadProjectDto implements RowMapper<WorkLoadProjectDto> {
//    private String userId;
//    private String idpUserId;
//    private String firstName;
//    private String lastName;
//    private String email;
//    private String profileImage;

    private String taskId;
    private String taskName;
    private String taskAssignee;
    private String taskInitiator;
    private String taskNote;
    private String taskStatus;
    private Timestamp taskCreatedAt;
    private Timestamp taskDueDateAt;
    private Timestamp taskReminderAt;
    private IssueTypeEnum issueType;

    private String projectId;
    private String projectName;
    private String clientId;
    private Timestamp projectStartDate;
    private Timestamp projectEndDate;

    public WorkLoadProjectDto() {
    }


    public WorkLoadProjectDto(String taskId, String taskName, String taskAssignee, String taskInitiator, String taskNote, String taskStatus, Timestamp taskCreatedAt, Timestamp taskDueDateAt, Timestamp taskReminderAt, String projectId, String projectName, String clientId, Timestamp projectStartDate, Timestamp projectEndDate, IssueTypeEnum issueType) {
        this.taskId = taskId;
        this.taskName = taskName;
        this.taskAssignee = taskAssignee;
        this.taskInitiator = taskInitiator;
        this.taskNote = taskNote;
        this.taskStatus = taskStatus;
        this.taskCreatedAt = taskCreatedAt;
        this.taskDueDateAt = taskDueDateAt;
        this.taskReminderAt = taskReminderAt;
        this.projectId = projectId;
        this.projectName = projectName;
        this.clientId = clientId;
        this.projectStartDate = projectStartDate;
        this.projectEndDate = projectEndDate;
        this.issueType = issueType;
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

    public String getTaskStatus() {
        return taskStatus;
    }

    public void setTaskStatus(String taskStatus) {
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

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public Timestamp getProjectStartDate() {
        return projectStartDate;
    }

    public void setProjectStartDate(Timestamp projectStartDate) {
        this.projectStartDate = projectStartDate;
    }

    public Timestamp getProjectEndDate() {
        return projectEndDate;
    }

    public void setProjectEndDate(Timestamp projectEndDate) {
        this.projectEndDate = projectEndDate;
    }

    public IssueTypeEnum getIssueType() {
        return issueType;
    }

    public void setIssueType(IssueTypeEnum issueType) {
        this.issueType = issueType;
    }

    @Override
    public WorkLoadProjectDto mapRow(ResultSet resultSet, int i) throws SQLException {
        return new WorkLoadProjectDto(
                resultSet.getString("taskId"),
                resultSet.getString("taskName"),
                resultSet.getString("taskAssignee"),
                resultSet.getString("taskInitiator"),
                resultSet.getString("taskNote"),
                resultSet.getString("taskStatus"),
                resultSet.getTimestamp("taskCreatedAt"),
                resultSet.getTimestamp("taskDueDateAt"),
                resultSet.getTimestamp("taskReminderAt"),
                resultSet.getString("projectId"),
                resultSet.getString("projectName"),
                resultSet.getString("clientId"),
                resultSet.getTimestamp("projectStartDate"),
                resultSet.getTimestamp("projectEndDate"),
                IssueTypeEnum.valueOf(resultSet.getString("issueType"))
        );
    }
}
