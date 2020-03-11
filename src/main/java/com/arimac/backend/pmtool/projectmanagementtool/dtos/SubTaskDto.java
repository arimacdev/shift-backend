package com.arimac.backend.pmtool.projectmanagementtool.dtos;

public class SubTaskDto {
    private String taskId;
    private String subtaskName;
    private String subTaskCreator;

    public String getSubTaskCreator() {
        return subTaskCreator;
    }

    public void setSubTaskCreator(String subTaskCreator) {
        this.subTaskCreator = subTaskCreator;
    }

    public String getSubtaskName() {
        return subtaskName;
    }

    public void setSubtaskName(String subtaskName) {
        this.subtaskName = subtaskName;
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    @Override
    public String toString() {
        return "SubTaskDto{" +
                "taskId='" + taskId + '\'' +
                ", subtaskName='" + subtaskName + '\'' +
                ", subTaskCreator='" + subTaskCreator + '\'' +
                '}';
    }
}
