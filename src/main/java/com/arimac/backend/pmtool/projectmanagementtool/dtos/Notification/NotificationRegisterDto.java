package com.arimac.backend.pmtool.projectmanagementtool.dtos.Notification;

import com.arimac.backend.pmtool.projectmanagementtool.enumz.Notification.NotificationEnum;
import com.arimac.backend.pmtool.projectmanagementtool.enumz.Notification.NotificationPlatformEnum;

import javax.validation.constraints.NotEmpty;

public class NotificationRegisterDto {
    @NotEmpty
    private String subscriberId;
    @NotEmpty
    private String subscriptionId;
    @NotEmpty
    private NotificationEnum provider;
    @NotEmpty
    private NotificationPlatformEnum platform;

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
}
