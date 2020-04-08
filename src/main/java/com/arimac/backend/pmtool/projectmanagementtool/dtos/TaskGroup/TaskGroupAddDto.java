package com.arimac.backend.pmtool.projectmanagementtool.dtos.TaskGroup;

public class TaskGroupAddDto {
    private String taskGroupId;
    private String taskGroupAssigner;
    private String taskGroupAssignee;


    public String getTaskGroupId() {
        return taskGroupId;
    }

    public void setTaskGroupId(String taskGroupId) {
        this.taskGroupId = taskGroupId;
    }

    public String getTaskGroupAssigner() {
        return taskGroupAssigner;
    }

    public void setTaskGroupAssigner(String taskGroupAssigner) {
        this.taskGroupAssigner = taskGroupAssigner;
    }

    public String getTaskGroupAssignee() {
        return taskGroupAssignee;
    }

    public void setTaskGroupAssignee(String taskGroupAssignee) {
        this.taskGroupAssignee = taskGroupAssignee;
    }

    @Override
    public String toString() {
        return "TaskGroupAddDto{" +
                "taskGroupId='" + taskGroupId + '\'' +
                ", taskGroupAssigner='" + taskGroupAssigner + '\'' +
                ", taskGroupAssignee='" + taskGroupAssignee + '\'' +
                '}';
    }
}
