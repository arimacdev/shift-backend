package com.arimac.backend.pmtool.projectmanagementtool.model;

import com.arimac.backend.pmtool.projectmanagementtool.enumz.EntityEnum;
import com.arimac.backend.pmtool.projectmanagementtool.enumz.LogOperationEnum;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

public class ActivityLog implements RowMapper<ActivityLog> {
    private String logId;
    private EntityEnum entityType;
    private String entityId;
    private LogOperationEnum operation;
    private Timestamp actionTimestamp;
    private String actor;
    private String from;
    private String to;

    public ActivityLog() {
    }

    public ActivityLog(String logId, EntityEnum entityType, String entityId, LogOperationEnum operation, Timestamp actionTimestamp, String actor, String from, String to) {
        this.logId = logId;
        this.entityType = entityType;
        this.entityId = entityId;
        this.operation = operation;
        this.actionTimestamp = actionTimestamp;
        this.actor = actor;
        this.from = from;
        this.to = to;
    }

    public String getLogId() {
        return logId;
    }

    public void setLogId(String logId) {
        this.logId = logId;
    }

    public EntityEnum getEntityType() {
        return entityType;
    }

    public void setEntityType(EntityEnum entityType) {
        this.entityType = entityType;
    }

    public String getEntityId() {
        return entityId;
    }

    public void setEntityId(String entityId) {
        this.entityId = entityId;
    }

    public LogOperationEnum getOperation() {
        return operation;
    }

    public void setOperation(LogOperationEnum operation) {
        this.operation = operation;
    }

    public Timestamp getActionTimestamp() {
        return actionTimestamp;
    }

    public void setActionTimestamp(Timestamp actionTimestamp) {
        this.actionTimestamp = actionTimestamp;
    }

    public String getActor() {
        return actor;
    }

    public void setActor(String actor) {
        this.actor = actor;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    @Override
    public ActivityLog mapRow(ResultSet resultSet, int i) throws SQLException {
        return null;
    }
}
