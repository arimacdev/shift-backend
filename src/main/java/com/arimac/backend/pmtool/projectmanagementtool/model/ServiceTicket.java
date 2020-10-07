package com.arimac.backend.pmtool.projectmanagementtool.model;

import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

public class ServiceTicket implements RowMapper<ServiceTicket> {
    private String ticketId;
    private String description;
    private String projectId;

    private String serviceAssignee;
    private String reporter;

    private int ticketStatus;
    private int severity;
    private int serviceLevel;

    private boolean isProjectTicket;
    private String taskReference;
    private boolean isFinished;

    private Timestamp ticketCreation;
    private Timestamp ticketAcceptance;
    private Timestamp ticketResolution;

    public ServiceTicket() {
    }

    public ServiceTicket(String ticketId, String description, String projectId, String serviceAssignee, String reporter, int ticketStatus, int severity, int serviceLevel, Timestamp ticketCreation, Timestamp ticketAcceptance, Timestamp ticketResolution) {
        this.ticketId = ticketId;
        this.description = description;
        this.projectId = projectId;
        this.serviceAssignee = serviceAssignee;
        this.reporter = reporter;
        this.ticketStatus = ticketStatus;
        this.severity = severity;
        this.serviceLevel = serviceLevel;
        this.ticketCreation = ticketCreation;
        this.ticketAcceptance = ticketAcceptance;
        this.ticketResolution = ticketResolution;
    }


    public boolean getIsProjectTicket() {
        return isProjectTicket;
    }

    public void setIsProjectTicket(boolean projectTicket) {
        isProjectTicket = projectTicket;
    }

    public String getTaskReference() {
        return taskReference;
    }

    public void setTaskReference(String taskReference) {
        this.taskReference = taskReference;
    }

    public boolean getIsFinished() {
        return isFinished;
    }

    public void setIsFinished(boolean finished) {
        isFinished = finished;
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

    public String getServiceAssignee() {
        return serviceAssignee;
    }

    public void setServiceAssignee(String serviceAssignee) {
        this.serviceAssignee = serviceAssignee;
    }

    public String getReporter() {
        return reporter;
    }

    public void setReporter(String reporter) {
        this.reporter = reporter;
    }

    public int getTicketStatus() {
        return ticketStatus;
    }

    public void setTicketStatus(int ticketStatus) {
        this.ticketStatus = ticketStatus;
    }

    public int getSeverity() {
        return severity;
    }

    public void setSeverity(int severity) {
        this.severity = severity;
    }

    public int getServiceLevel() {
        return serviceLevel;
    }

    public void setServiceLevel(int serviceLevel) {
        this.serviceLevel = serviceLevel;
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

    @Override
    public ServiceTicket mapRow(ResultSet resultSet, int i) throws SQLException {
        return null;
    }
}
