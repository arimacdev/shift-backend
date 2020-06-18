package com.arimac.backend.pmtool.projectmanagementtool.repository;

import com.arimac.backend.pmtool.projectmanagementtool.model.UserNotification;

public interface UserNotificationRepository {
    void registerForNotifications(UserNotification userNotification);
}
