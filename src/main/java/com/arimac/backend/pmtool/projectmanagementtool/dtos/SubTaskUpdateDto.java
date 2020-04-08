package com.arimac.backend.pmtool.projectmanagementtool.dtos;

import com.arimac.backend.pmtool.projectmanagementtool.enumz.TaskTypeEnum;
import com.fasterxml.jackson.annotation.JsonProperty;

public class SubTaskUpdateDto {
    private String subTaskEditor;
    private String subtaskName;
    @JsonProperty
    private Boolean subtaskStatus;
    private TaskTypeEnum taskType;

    public String getSubTaskEditor() {
        return subTaskEditor;
    }

    public void setSubTaskEditor(String subTaskEditor) {
        this.subTaskEditor = subTaskEditor;
    }

    public String getSubtaskName() {
        return subtaskName;
    }

    public void setSubtaskName(String subtaskName) {
        this.subtaskName = subtaskName;
    }

    public Boolean getSubTaskStatus() {
        return subtaskStatus;
    }

    public void setSubtaskStatus(Boolean subtaskStatus) {
        this.subtaskStatus = subtaskStatus;
    }

    public TaskTypeEnum getTaskType() {
        return taskType;
    }

    public void setTaskType(TaskTypeEnum taskType) {
        this.taskType = taskType;
    }

    @Override
    public String toString() {
        return "SubTaskUpdateDto{" +
                "subTaskEditor='" + subTaskEditor + '\'' +
                ", subtaskName='" + subtaskName + '\'' +
                ", subtaskStatus=" + subtaskStatus +
                ", taskType='" + taskType + '\'' +
                '}';
    }
}
