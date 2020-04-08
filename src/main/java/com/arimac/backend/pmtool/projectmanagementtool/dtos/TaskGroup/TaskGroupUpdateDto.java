package com.arimac.backend.pmtool.projectmanagementtool.dtos.TaskGroup;

public class TaskGroupUpdateDto {
    private String taskGroupName;
    private String taskGroupEditor;

    public String getTaskGroupName() {
        return taskGroupName;
    }

    public void setTaskGroupName(String taskGroupName) {
        this.taskGroupName = taskGroupName;
    }

    public String getTaskGroupEditor() {
        return taskGroupEditor;
    }

    public void setTaskGroupEditor(String taskGroupEditor) {
        this.taskGroupEditor = taskGroupEditor;
    }

    @Override
    public String toString() {
        return "TaskGroupUpdateDto{" +
                "taskGroupName='" + taskGroupName + '\'' +
                ", taskGroupEditor='" + taskGroupEditor + '\'' +
                '}';
    }
}
