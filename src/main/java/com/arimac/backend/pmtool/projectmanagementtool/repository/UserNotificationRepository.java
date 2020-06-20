package com.arimac.backend.pmtool.projectmanagementtool.repository;

import com.arimac.backend.pmtool.projectmanagementtool.dtos.Notification.NotificationDto;
import com.arimac.backend.pmtool.projectmanagementtool.model.UserNotification;

import java.util.List;

public interface UserNotificationRepository {
    void registerForNotifications(UserNotification userNotification);
    List<UserNotification> getNotificationUserByProviderAndStatus(String userId, String provider, boolean status);
    UserNotification getUserNotificationByProviderStatusAndPlatform(String userId, String subscriptionId, String provider, String platform);
    void changeSubscriptionStatus(NotificationDto notificationDto);
}
