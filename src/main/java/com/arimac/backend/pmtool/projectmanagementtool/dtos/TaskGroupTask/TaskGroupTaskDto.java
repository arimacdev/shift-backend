package com.arimac.backend.pmtool.projectmanagementtool.dtos.TaskGroupTask;

import com.arimac.backend.pmtool.projectmanagementtool.enumz.TaskGroupTaskStatusEnum;

import java.sql.Timestamp;

public class TaskGroupTaskDto {
    private String taskName;
    private String taskGroupId;
    private TaskGroupTaskStatusEnum taskStatus;
    private String taskInitiator;
    private String taskAssignee;
    private String taskNotes;
    private Timestamp taskDueDate;
    private Timestamp taskRemindOnDate;
    private String parentTaskId;


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

    public TaskGroupTaskStatusEnum getTaskStatus() {
        return taskStatus;
    }

    public void setTaskStatus(TaskGroupTaskStatusEnum taskStatus) {
        this.taskStatus = taskStatus;
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

    public String getParentTaskId() {
        return parentTaskId;
    }

    public void setParentTaskId(String parentTaskId) {
        this.parentTaskId = parentTaskId;
    }

    @Override
    public String toString() {
        return "TaskGroupTaskDto{" +
                "taskName='" + taskName + '\'' +
                ", taskGroupId='" + taskGroupId + '\'' +
                ", taskStatus=" + taskStatus +
                ", taskInitiator='" + taskInitiator + '\'' +
                ", taskAssignee='" + taskAssignee + '\'' +
                ", taskNotes='" + taskNotes + '\'' +
                ", taskDueDate=" + taskDueDate +
                ", taskRemindOnDate=" + taskRemindOnDate +
                ", parentTaskId='" + parentTaskId + '\'' +
                '}';
    }
}
