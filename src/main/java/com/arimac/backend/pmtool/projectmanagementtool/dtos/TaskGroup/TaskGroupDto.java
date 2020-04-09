package com.arimac.backend.pmtool.projectmanagementtool.dtos.TaskGroup;

public class TaskGroupDto {
    private String taskGroupName;
    private String taskGroupCreator;

    public String getTaskGroupName() {
        return taskGroupName;
    }

    public void setTaskGroupName(String taskGroupName) {
        this.taskGroupName = taskGroupName;
    }

    public String getTaskGroupCreator() {
        return taskGroupCreator;
    }

    public void setTaskGroupCreator(String taskGroupCreator) {
        this.taskGroupCreator = taskGroupCreator;
    }

    @Override
    public String toString() {
        return "TaskGroupDto{" +
                "taskGroupName='" + taskGroupName + '\'' +
                ", taskGroupCreator='" + taskGroupCreator + '\'' +
                '}';
    }
}
