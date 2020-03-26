package com.arimac.backend.pmtool.projectmanagementtool.dtos;

import com.arimac.backend.pmtool.projectmanagementtool.enumz.TaskStatusEnum;

import java.sql.Timestamp;

public class TaskDto {
    private String taskName;
    private String projectId;
    private TaskStatusEnum taskStatus;
    private String taskInitiator;
    private String taskAssignee;
    private String taskNotes;
    private Timestamp taskDueDate;
    private Timestamp taskRemindOnDate;

    public TaskDto() {
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

    public TaskDto(String taskName, String projectId, TaskStatusEnum taskStatus, String taskInitiator, String taskAssignee, String taskNotes, Timestamp taskDueDate, Timestamp taskRemindOnDate) {
        this.taskName = taskName;
        this.projectId = projectId;
        this.taskStatus = taskStatus;
        this.taskInitiator = taskInitiator;
        this.taskAssignee = taskAssignee;
        this.taskNotes = taskNotes;
        this.taskDueDate = taskDueDate;
        this.taskRemindOnDate = taskRemindOnDate;
    }
}
