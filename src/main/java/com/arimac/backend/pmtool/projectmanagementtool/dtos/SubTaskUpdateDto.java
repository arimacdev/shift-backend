package com.arimac.backend.pmtool.projectmanagementtool.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;

public class SubTaskUpdateDto {
    private String subTaskEditor;
    private String subtaskName;
    @JsonProperty
    private Boolean subtaskStatus;

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

    @Override
    public String toString() {
        return "SubTaskUpdateDto{" +
                "subTaskEditor='" + subTaskEditor + '\'' +
                ", subtaskName='" + subtaskName + '\'' +
                ", subtaskStatus=" + subtaskStatus +
                '}';
    }
}
