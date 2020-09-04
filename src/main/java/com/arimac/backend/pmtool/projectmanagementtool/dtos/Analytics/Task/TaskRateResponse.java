package com.arimac.backend.pmtool.projectmanagementtool.dtos.Analytics.Task;

public class TaskRateResponse {
    private String date;
    private int taskCreationCount;
    private int taskCompletionCount;
    private int overDueCount;

    public TaskRateResponse() {
        this.taskCompletionCount = 0;
        this.taskCreationCount = 0;
        this.overDueCount = 0;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getTaskCreationCount() {
        return taskCreationCount;
    }

    public void setTaskCreationCount(int taskCreationCount) {
        this.taskCreationCount = taskCreationCount;
    }

    public int getTaskCompletionCount() {
        return taskCompletionCount;
    }

    public void setTaskCompletionCount(int taskCompletionCount) {
        this.taskCompletionCount = taskCompletionCount;
    }

    public int getOverDueCount() {
        return overDueCount;
    }

    public void setOverDueCount(int overDueCount) {
        this.overDueCount = overDueCount;
    }
}
