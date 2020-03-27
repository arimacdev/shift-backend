package com.arimac.backend.pmtool.projectmanagementtool.dtos;

import java.util.List;

public class UserProjectWorkLoadDto {
    private String userId;
    private String projectId;
    private String projectName;
    private List<ProjectTaskWorkLoadDto> taskList;
    private int completed;
    private int total;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public List<ProjectTaskWorkLoadDto> getTaskList() {
        return taskList;
    }

    public void setTaskList(List<ProjectTaskWorkLoadDto> taskList) {
        this.taskList = taskList;
    }

    public int getCompleted() {
        return completed;
    }

    public void setCompleted(int completed) {
        this.completed = completed;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }
}
