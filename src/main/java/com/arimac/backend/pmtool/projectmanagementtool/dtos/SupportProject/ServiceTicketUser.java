package com.arimac.backend.pmtool.projectmanagementtool.dtos.SupportProject;

import java.sql.Timestamp;

public class ServiceTicketUser {
    private String ticketId;
    private String issueTopic;
    private String description;
    private String projectId;

    private String ticketStatus;
    private SeverityEnum severity;
    private boolean isFinished;

    private Timestamp ticketCreation;
    private Timestamp ticketAcceptance;
    private Timestamp ticketResolution;

    private UserDto serviceAssignee;
    private UserDto reporter;

    public String getIssueTopic() {
        return issueTopic;
    }

    public void setIssueTopic(String issueTopic) {
        this.issueTopic = issueTopic;
    }

    public String getTicketId() {
        return ticketId;
    }

    public void setTicketId(String ticketId) {
        this.ticketId = ticketId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    public String getTicketStatus() {
        return ticketStatus;
    }

    public void setTicketStatus(String ticketStatus) {
        this.ticketStatus = ticketStatus;
    }

    public SeverityEnum getSeverity() {
        return severity;
    }

    public void setSeverity(SeverityEnum severity) {
        this.severity = severity;
    }

    public boolean getIsFinished() {
        return isFinished;
    }

    public void setIsFinished(boolean finished) {
        isFinished = finished;
    }

    public Timestamp getTicketCreation() {
        return ticketCreation;
    }

    public void setTicketCreation(Timestamp ticketCreation) {
        this.ticketCreation = ticketCreation;
    }

    public Timestamp getTicketAcceptance() {
        return ticketAcceptance;
    }

    public void setTicketAcceptance(Timestamp ticketAcceptance) {
        this.ticketAcceptance = ticketAcceptance;
    }

    public Timestamp getTicketResolution() {
        return ticketResolution;
    }

    public void setTicketResolution(Timestamp ticketResolution) {
        this.ticketResolution = ticketResolution;
    }

    public UserDto getServiceAssignee() {
        return serviceAssignee;
    }

    public void setServiceAssignee(UserDto serviceAssignee) {
        this.serviceAssignee = serviceAssignee;
    }

    public UserDto getReporter() {
        return reporter;
    }

    public void setReporter(UserDto reporter) {
        this.reporter = reporter;
    }
}
