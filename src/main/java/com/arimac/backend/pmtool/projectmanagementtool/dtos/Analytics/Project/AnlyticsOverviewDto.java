package com.arimac.backend.pmtool.projectmanagementtool.dtos.Analytics.Project;

public class AnlyticsOverviewDto {
    private int activeUsers;
    private int activeProjects;
    private int totalTasks;
    private int activeTasks;
    private int closedTasks;

    public int getActiveUsers() {
        return activeUsers;
    }

    public void setActiveUsers(int activeUsers) {
        this.activeUsers = activeUsers;
    }

    public int getActiveProjects() {
        return activeProjects;
    }

    public void setActiveProjects(int activeProjects) {
        this.activeProjects = activeProjects;
    }

    public int getTotalTasks() {
        return totalTasks;
    }

    public void setTotalTasks(int totalTasks) {
        this.totalTasks = totalTasks;
    }

    public int getActiveTasks() {
        return activeTasks;
    }

    public void setActiveTasks(int activeTasks) {
        this.activeTasks = activeTasks;
    }

    public int getClosedTasks() {
        return closedTasks;
    }

    public void setClosedTasks(int closedTasks) {
        this.closedTasks = closedTasks;
    }
}
