package com.arimac.backend.pmtool.projectmanagementtool.dtos.PersonalTask;

import com.arimac.backend.pmtool.projectmanagementtool.enumz.IssueTypeEnum;
import com.arimac.backend.pmtool.projectmanagementtool.enumz.PersonalTaskEnum;
import com.arimac.backend.pmtool.projectmanagementtool.enumz.TaskTypeEnum;

import java.sql.Timestamp;

public class PersonalTaskUpdateDto {
    private String taskName;
    private Timestamp taskDueDate;
    private Timestamp taskRemindOnDate;
    private String taskNotes;
    private PersonalTaskEnum taskStatus;


    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
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

    public PersonalTaskEnum getTaskStatus() {
        return taskStatus;
    }

    public void setTaskStatus(PersonalTaskEnum taskStatus) {
        this.taskStatus = taskStatus;
    }

    @Override
    public String toString() {
        return "PersonalTaskUpdateDto{" +
                "taskName='" + taskName + '\'' +
                ", taskDueDate=" + taskDueDate +
                ", taskRemindOnDate=" + taskRemindOnDate +
                ", taskNotes='" + taskNotes + '\'' +
                ", taskStatus=" + taskStatus +
                '}';
    }
}
