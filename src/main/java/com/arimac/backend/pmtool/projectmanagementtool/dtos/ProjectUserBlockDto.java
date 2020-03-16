package com.arimac.backend.pmtool.projectmanagementtool.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ProjectUserBlockDto {
    private String executorId;
    private String blockedUserId;
    @JsonProperty
    private Boolean blockedStatus;

    public String getExecutorId() {
        return executorId;
    }

    public void setExecutorId(String executorId) {
        this.executorId = executorId;
    }

    public String getBlockedUserId() {
        return blockedUserId;
    }

    public void setBlockedUserId(String blockedUserId) {
        this.blockedUserId = blockedUserId;
    }

    public Boolean getBlockedStatus() {
        return blockedStatus;
    }

    public void SetBlockedStatus(Boolean blockedStatus) {
        this.blockedStatus = blockedStatus;
    }

    @Override
    public String toString() {
        return "ProjectUserBlockDto{" +
                "executorId='" + executorId + '\'' +
                ", blockedUserId='" + blockedUserId + '\'' +
                ", blockedStatus=" + blockedStatus +
                '}';
    }
}
