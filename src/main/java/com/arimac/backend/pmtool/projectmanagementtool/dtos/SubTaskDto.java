package com.arimac.backend.pmtool.projectmanagementtool.dtos;

import com.arimac.backend.pmtool.projectmanagementtool.enumz.TaskTypeEnum;

public class SubTaskDto {
    private String taskId;
    private String subtaskName;
    private String subTaskCreator;
    private TaskTypeEnum taskType;

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


    public TaskTypeEnum getTaskType() {
        return taskType;
    }

    public void setTaskType(TaskTypeEnum taskType) {
        this.taskType = taskType;
    }

    @Override
    public String toString() {
        return "SubTaskDto{" +
                "taskId='" + taskId + '\'' +
                ", subtaskName='" + subtaskName + '\'' +
                ", subTaskCreator='" + subTaskCreator + '\'' +
                ", taskType=" + taskType +
                '}';
    }
}
