package com.arimac.backend.pmtool.projectmanagementtool.dtos;

import com.arimac.backend.pmtool.projectmanagementtool.enumz.TaskStatusEnum;

import java.sql.Timestamp;

public class TaskUpdateDto {
    private String taskName;
    private String taskAssignee;
    private Timestamp taskDueDate;
    private Timestamp taskRemindOnDate;
    private String taskNotes;
    private String taskStatus;

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

    public String getTaskNotes() {
        return taskNotes;
    }

    public void setTaskNotes(String taskNotes) {
        this.taskNotes = taskNotes;
    }

    public String getTaskStatus() {
        return taskStatus;
    }

    public void setTaskStatus(String taskStatus) {
        this.taskStatus = taskStatus;
    }
}
