package com.arimac.backend.pmtool.projectmanagementtool.dtos;

public class TaskCompletionDto {
    private int totalTasks;
    private int completed;

    public int getTotalTasks() {
        return totalTasks;
    }

    public void setTotalTasks(int totalTasks) {
        this.totalTasks = totalTasks;
    }

    public int getCompleted() {
        return completed;
    }

    public void setCompleted(int completed) {
        this.completed = completed;
    }
}
