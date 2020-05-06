package com.arimac.backend.pmtool.projectmanagementtool.dtos;

import com.arimac.backend.pmtool.projectmanagementtool.enumz.IssueTypeEnum;
import com.arimac.backend.pmtool.projectmanagementtool.enumz.TaskStatusEnum;
import com.arimac.backend.pmtool.projectmanagementtool.enumz.TaskTypeEnum;

import java.sql.Timestamp;

public class TaskDto {
    private String taskName;
    private String projectId;
    private String sprintId;
    private TaskStatusEnum taskStatus;
    private String taskInitiator;
    private String taskAssignee;
    private String taskNotes;
    private Timestamp taskDueDate;
    private Timestamp taskRemindOnDate;
//    private TaskTypeEnum taskType;

    private IssueTypeEnum issueType;
    private String parentTaskId;

    public TaskDto() {
    }


    public String getSprintId() {
        return sprintId;
    }

    public void setSprintId(String sprintId) {
        this.sprintId = sprintId;
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

    public String getTaskInitiator() {
        return taskInitiator;
    }

    public void setTaskInitiator(String taskInitiator) {
        this.taskInitiator = taskInitiator;
    }

    public String getTaskAssignee() {
        return taskAssignee;
    }

    public void setTaskAssignee(String taskAssignee) {
        this.taskAssignee = taskAssignee;
    }

    public String getTaskNotes() {
        return taskNotes;
    }

    public void setTaskNotes(String taskNotes) {
        this.taskNotes = taskNotes;
    }

    public Timestamp getTaskDueDate() {
        return taskDueDate;
    }

    public void setTaskDueDate(Timestamp taskDueDate) {
        this.taskDueDate = taskDueDate;
    }

    public Timestamp getTaskRemindOnDate() {
        return taskRemindOnDate;
    }

    public void setTaskRemindOnDate(Timestamp taskRemindOnDate) {
        this.taskRemindOnDate = taskRemindOnDate;
    }


    public TaskStatusEnum getTaskStatus() {
        return taskStatus;
    }

    public void setTaskStatus(TaskStatusEnum taskStatus) {
        this.taskStatus = taskStatus;
    }

//    public TaskTypeEnum getTaskType() {
//        return taskType;
//    }
//
//    public void setTaskType(TaskTypeEnum taskType) {
//        this.taskType = taskType;
//    }

    public IssueTypeEnum getIssueType() {
        return issueType;
    }

    public void setIssueType(IssueTypeEnum issueType) {
        this.issueType = issueType;
    }

    public String getParentTaskId() {
        return parentTaskId;
    }

    public void setParentId(String parentId) {
        this.parentTaskId = parentId;
    }

    @Override
    public String toString() {
        return "TaskDto{" +
                "taskName='" + taskName + '\'' +
                ", projectId='" + projectId + '\'' +
                ", taskStatus=" + taskStatus +
                ", taskInitiator='" + taskInitiator + '\'' +
                ", taskAssignee='" + taskAssignee + '\'' +
                ", taskNotes='" + taskNotes + '\'' +
                ", taskDueDate=" + taskDueDate +
                ", taskRemindOnDate=" + taskRemindOnDate +
//                ", taskType=" + taskType +
                ", issueType=" + issueType +
                ", parentId='" + parentTaskId + '\'' +
                '}';
    }
}
