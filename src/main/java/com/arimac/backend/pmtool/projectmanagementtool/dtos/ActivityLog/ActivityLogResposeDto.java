package com.arimac.backend.pmtool.projectmanagementtool.dtos.ActivityLog;

import com.arimac.backend.pmtool.projectmanagementtool.enumz.ActivityLog.EntityEnum;
import com.arimac.backend.pmtool.projectmanagementtool.enumz.ActivityLog.LogOperationEnum;

import java.sql.Timestamp;

public class ActivityLogResposeDto {
    private String logId;
    private EntityEnum entityType;
    private String entityId;
    private LogOperationEnum operation;
    private Timestamp actionTimestamp;
    private String updateType;
    private String actor;
    private FieldValue previousValue;
    private FieldValue updatedvalue;

    private String actorFirstName;
    private String actorLastName;
    private String actorProfileImage;

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

    public FieldValue getPreviousValue() {
        return previousValue;
    }

    public void setPreviousValue(FieldValue previousValue) {
        this.previousValue = previousValue;
    }

    public FieldValue getUpdatedvalue() {
        return updatedvalue;
    }

    public void setUpdatedvalue(FieldValue updatedvalue) {
        this.updatedvalue = updatedvalue;
    }

    public String getActorFirstName() {
        return actorFirstName;
    }

    public void setActorFirstName(String actorFirstName) {
        this.actorFirstName = actorFirstName;
    }

    public String getActorLastName() {
        return actorLastName;
    }

    public void setActorLastName(String actorLastName) {
        this.actorLastName = actorLastName;
    }

    public String getActorProfileImage() {
        return actorProfileImage;
    }

    public void setActorProfileImage(String actorProfileImage) {
        this.actorProfileImage = actorProfileImage;
    }
}
