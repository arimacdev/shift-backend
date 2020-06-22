package com.arimac.backend.pmtool.projectmanagementtool.model;

import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class UserNotification implements RowMapper<UserNotification> {
    private String userId;
    private String subscriptionId;
    private String provider;
    private boolean notificationStatus;
    private String platform;


    public UserNotification() {
    }

    public UserNotification(String userId, String subscriptionId, String provider, boolean notificationStatus, String platform) {
        this.userId = userId;
        this.subscriptionId = subscriptionId;
        this.provider = provider;
        this.notificationStatus = notificationStatus;
        this.platform = platform;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getSubscriptionId() {
        return subscriptionId;
    }

    public void setSubscriptionId(String subscriptionId) {
        this.subscriptionId = subscriptionId;
    }

    public String getProvider() {
        return provider;
    }

    public void setProvider(String provider) {
        this.provider = provider;
    }

    public boolean getNotificationStatus() {
        return notificationStatus;
    }

    public void setNotificationStatus(boolean notificationStatus) {
        this.notificationStatus = notificationStatus;
    }


    public String getPlatform() {
        return platform;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }

    @Override
    public UserNotification mapRow(ResultSet resultSet, int i) throws SQLException {
        return new UserNotification(
                resultSet.getString("userId"),
                resultSet.getString("subscriptionId"),
                resultSet.getString("provider"),
                resultSet.getBoolean("notificationStatus"),
                resultSet.getString("platform")
        );
    }
}
