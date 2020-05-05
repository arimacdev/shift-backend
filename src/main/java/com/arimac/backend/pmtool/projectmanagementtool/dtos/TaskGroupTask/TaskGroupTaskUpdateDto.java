package com.arimac.backend.pmtool.projectmanagementtool.dtos.TaskGroupTask;

import com.arimac.backend.pmtool.projectmanagementtool.enumz.IssueTypeEnum;
import com.arimac.backend.pmtool.projectmanagementtool.enumz.TaskGroupTaskStatusEnum;
import com.arimac.backend.pmtool.projectmanagementtool.enumz.TaskTypeEnum;

import java.sql.Timestamp;

public class TaskGroupTaskUpdateDto {
    private String taskName;
    private String taskAssignee;
    private Timestamp taskDueDate;
    private Timestamp taskRemindOnDate;
    private String taskNotes;
    private TaskGroupTaskStatusEnum taskStatus;

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

    public TaskGroupTaskStatusEnum getTaskStatus() {
        return taskStatus;
    }

    public void setTaskStatus(TaskGroupTaskStatusEnum taskStatus) {
        this.taskStatus = taskStatus;
    }

    @Override
    public String toString() {
        return "TaskGroupTaskUpdateDto{" +
                "taskName='" + taskName + '\'' +
                ", taskAssignee='" + taskAssignee + '\'' +
                ", taskDueDate=" + taskDueDate +
                ", taskRemindOnDate=" + taskRemindOnDate +
                ", taskNotes='" + taskNotes + '\'' +
                ", taskStatus='" + taskStatus + '\'' +
                '}';
    }
}
