package com.arimac.backend.pmtool.projectmanagementtool.dtos.Notification;

import com.arimac.backend.pmtool.projectmanagementtool.enumz.Notification.NotificationEnum;
import com.arimac.backend.pmtool.projectmanagementtool.enumz.Notification.NotificationPlatformEnum;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.NotEmpty;

public class NotificationDto {
    @NotEmpty
    private String subscriberId;
    @NotEmpty
    private String subscriptionId;
    @NotEmpty
    private NotificationEnum provider;
    @NotEmpty
    private NotificationPlatformEnum platform;
    @JsonProperty
    private Boolean notificationStatus;

    public String getSubscriberId() {
        return subscriberId;
    }

    public void setSubscriberId(String subscriberId) {
        this.subscriberId = subscriberId;
    }

    public String getSubscriptionId() {
        return subscriptionId;
    }

    public void setSubscriptionId(String subscriptionId) {
        this.subscriptionId = subscriptionId;
    }

    public NotificationEnum getProvider() {
        return provider;
    }

    public void setProvider(NotificationEnum provider) {
        this.provider = provider;
    }

    public NotificationPlatformEnum getPlatform() {
        return platform;
    }

    public void setPlatform(NotificationPlatformEnum platform) {
        this.platform = platform;
    }

    public Boolean getNotificationStatus() {
        return notificationStatus;
    }

    public void setNotificationStatus(Boolean notificationStatus) {
        this.notificationStatus = notificationStatus;
    }
}
