package com.arimac.backend.pmtool.projectmanagementtool.dtos;

public class NotificationUpdateDto {
    private String taskId;
    private boolean daily;
    private boolean hourly;

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public boolean getIsDaily() {
        return daily;
    }

    public void setIsDaily(boolean daily) {
        this.daily = daily;
    }

    public boolean getIsHourly() {
        return hourly;
    }

    public void setIsHourly(boolean hourly) {
        this.hourly = hourly;
    }
}
