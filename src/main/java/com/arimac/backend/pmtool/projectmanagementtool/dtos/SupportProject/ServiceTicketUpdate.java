package com.arimac.backend.pmtool.projectmanagementtool.dtos.SupportProject;

import com.arimac.backend.pmtool.projectmanagementtool.enumz.ServiceDesk.ServiceLevelEnum;
import com.arimac.backend.pmtool.projectmanagementtool.enumz.ServiceDesk.ServiceTicketStatusEnum;

import javax.validation.constraints.NotNull;

public class ServiceTicketUpdate {
    @NotNull
    private String projectId;
    private ServiceLevelEnum serviceLevel;
    private ServiceTicketStatusEnum ticketStatus;

    public ServiceTicketUpdate(@NotNull String projectId, ServiceTicketStatusEnum ticketStatus) {
        this.projectId = projectId;
        this.ticketStatus = ticketStatus;
    }

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    public ServiceLevelEnum getServiceLevel() {
        return serviceLevel;
    }

    public void setServiceLevel(ServiceLevelEnum serviceLevel) {
        this.serviceLevel = serviceLevel;
    }

    public ServiceTicketStatusEnum getTicketStatus() {
        return ticketStatus;
    }

    public void setTicketStatus(ServiceTicketStatusEnum ticketStatus) {
        this.ticketStatus = ticketStatus;
    }

    @Override
    public String toString() {
        return "ServiceTicketUpdate{" +
                "projectId='" + projectId + '\'' +
                ", serviceLevel=" + serviceLevel +
                ", ticketStatus=" + ticketStatus +
                '}';
    }
}
