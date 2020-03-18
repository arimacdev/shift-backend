package com.arimac.backend.pmtool.projectmanagementtool.dtos;

public class ProjectTaskCompletionDto {
    private int tasksDueToday;
    private int tasksOverDue;
    private int tasksCompleted;
    private int tasksLeft;
    private int tasksAssigned;

    public int getTasksDueToday() {
        return tasksDueToday;
    }

    public void setTasksDueToday(int tasksDueToday) {
        this.tasksDueToday = tasksDueToday;
    }

    public int getTasksOverDue() {
        return tasksOverDue;
    }

    public void setTasksOverDue(int tasksOverDue) {
        this.tasksOverDue = tasksOverDue;
    }

    public int getTasksCompleted() {
        return tasksCompleted;
    }

    public void setTasksCompleted(int tasksCompleted) {
        this.tasksCompleted = tasksCompleted;
    }

    public int getTasksLeft() {
        return tasksLeft;
    }

    public void setTasksLeft(int tasksLeft) {
        this.tasksLeft = tasksLeft;
    }

    public int getTasksAssigned() {
        return tasksAssigned;
    }

    public void setTasksAssigned(int tasksAssigned) {
        this.tasksAssigned = tasksAssigned;
    }
}
