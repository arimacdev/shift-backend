package com.arimac.backend.pmtool.projectmanagementtool.dtos.PersonalTask;

import com.arimac.backend.pmtool.projectmanagementtool.enumz.IssueTypeEnum;
import com.arimac.backend.pmtool.projectmanagementtool.enumz.PersonalTaskEnum;
import com.arimac.backend.pmtool.projectmanagementtool.enumz.TaskStatusEnum;
import com.arimac.backend.pmtool.projectmanagementtool.enumz.TaskTypeEnum;

import java.sql.Timestamp;

public class PersonalTaskDto {
    private String taskName;
    private String taskAssignee;
    private String taskNotes;
    private Timestamp taskDueDate;
    private Timestamp taskRemindOnDate;

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

    @Override
    public String toString() {
        return "PersonalTaskDto{" +
                "taskName='" + taskName + '\'' +
                ", taskAssignee='" + taskAssignee + '\'' +
                ", taskNotes='" + taskNotes + '\'' +
                ", taskDueDate=" + taskDueDate +
                ", taskRemindOnDate=" + taskRemindOnDate +
                '}';
    }
}
