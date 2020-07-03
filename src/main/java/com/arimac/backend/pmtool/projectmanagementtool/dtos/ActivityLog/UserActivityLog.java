package com.arimac.backend.pmtool.projectmanagementtool.dtos.ActivityLog;

import com.arimac.backend.pmtool.projectmanagementtool.enumz.ActivityLog.EntityEnum;
import com.arimac.backend.pmtool.projectmanagementtool.enumz.ActivityLog.LogOperationEnum;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.concurrent.TimeUnit;

public class UserActivityLog implements RowMapper<UserActivityLog> {
    private String logId;
    private EntityEnum entityType;
    private String entityId;
    private LogOperationEnum operation;
    private Timestamp actionTimestamp;
    private String updateType;
    private String actor;
    private String previousValue;
    private String updatedvalue;

    private String actorFirstName;
    private String actorLastName;
    private String actorProfileImage;


    public UserActivityLog() {
    }

    public UserActivityLog(String logId, EntityEnum entityType, String entityId, LogOperationEnum operation, Timestamp actionTimestamp, String updateType, String actor, String previousValue, String updatedvalue, String firstName, String lastName, String actorProfileImage) {
        this.logId = logId;
        this.entityType = entityType;
        this.entityId = entityId;
        this.operation = operation;
        this.actionTimestamp = actionTimestamp;
        this.updateType = updateType;
        this.actor = actor;
        this.previousValue = previousValue;
        this.updatedvalue = updatedvalue;
        this.actorFirstName = firstName;
        this.actorLastName = lastName;
        this.actorProfileImage = actorProfileImage;
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

    public String getFirstName() {
        return actorFirstName;
    }

    public void setFirstName(String firstName) {
        this.actorFirstName = firstName;
    }

    public String getLastName() {
        return actorLastName;
    }

    public void setLastName(String lastName) {
        this.actorLastName = lastName;
    }

    public String getActorProfileImage() {
        return actorProfileImage;
    }

    public void setActorProfileImage(String actorProfileImage) {
        this.actorProfileImage = actorProfileImage;
    }

    private Timestamp formatDate(Timestamp commented) {
        commented.setTime(commented.getTime() + TimeUnit.MINUTES.toMillis(330));

        return commented;
    }

    @Override
    public UserActivityLog mapRow(ResultSet resultSet, int i) throws SQLException {
        return new UserActivityLog(
                resultSet.getString("logId"),
                EntityEnum.valueOf(resultSet.getString("entityType")),
                resultSet.getString("entityId"),
                LogOperationEnum.valueOf(resultSet.getString("operation")),
                formatDate(resultSet.getTimestamp("actionTimestamp")),
                resultSet.getString("updateType"),
                resultSet.getString("actor"),
                resultSet.getString("previousValue"),
                resultSet.getString("updatedvalue"),
                resultSet.getString("firstName"),
                resultSet.getString("lastName"),
                resultSet.getString("profileImage")

        );
    }


}
