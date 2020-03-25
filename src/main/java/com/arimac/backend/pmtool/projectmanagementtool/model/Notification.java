package com.arimac.backend.pmtool.projectmanagementtool.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class Notification implements RowMapper<Notification> {
    private String userId;
    private String userSlackId;
    @JsonProperty
    private boolean notificationStatus;

    public Notification() {
    }

    public Notification(String userId, String userSlackId, boolean notificationStatus) {
        this.userId = userId;
        this.userSlackId = userSlackId;
        this.notificationStatus = notificationStatus;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserSlackId() {
        return userSlackId;
    }

    public void setUserSlackId(String userSlackId) {
        this.userSlackId = userSlackId;
    }

    public boolean getNotificationStatus() {
        return notificationStatus;
    }

    public void setNotificationStatus(boolean notificationStatus) {
        this.notificationStatus = notificationStatus;
    }

    @Override
    public Notification mapRow(ResultSet resultSet, int i) throws SQLException {
        return new Notification(
                resultSet.getString("userId"),
                resultSet.getString("userSlackId"),
                resultSet.getBoolean("notificationStatus")
        );
    }
}
