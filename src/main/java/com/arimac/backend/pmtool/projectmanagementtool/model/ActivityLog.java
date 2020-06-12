package com.arimac.backend.pmtool.projectmanagementtool.model;

import com.arimac.backend.pmtool.projectmanagementtool.enumz.ActivityLog.EntityEnum;
import com.arimac.backend.pmtool.projectmanagementtool.enumz.ActivityLog.LogOperationEnum;
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
    private String updateType;
    private String actor;
    private String previousValue;
    private String updatedvalue;

    public ActivityLog() {
    }

    public ActivityLog(String logId, EntityEnum entityType, String entityId, LogOperationEnum operation, Timestamp actionTimestamp, String updateType, String actor, String previousValue, String updatedvalue) {
        this.logId = logId;
        this.entityType = entityType;
        this.entityId = entityId;
        this.operation = operation;
        this.actionTimestamp = actionTimestamp;
        this.updateType = updateType;
        this.actor = actor;
        this.previousValue = previousValue;
        this.updatedvalue = updatedvalue;
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

    public String getUpdateType() {
        return updateType;
    }

    public void setUpdateType(String updateType) {
        this.updateType = updateType;
    }

    public String getActor() {
        return actor;
    }

    public void setActor(String actor) {
        this.actor = actor;
    }

    public String getPreviousValue() {
        return previousValue;
    }

    public void setPreviousValue(String previousValue) {
        this.previousValue = previousValue;
    }

    public String getUpdatedvalue() {
        return updatedvalue;
    }

    public void setUpdatedvalue(String updatedvalue) {
        this.updatedvalue = updatedvalue;
    }

    @Override
    public ActivityLog mapRow(ResultSet resultSet, int i) throws SQLException {
        return null;
    }
}
