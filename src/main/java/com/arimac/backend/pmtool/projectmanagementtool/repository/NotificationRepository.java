package com.arimac.backend.pmtool.projectmanagementtool.repository;

import com.arimac.backend.pmtool.projectmanagementtool.model.Notification;

public interface NotificationRepository {
    void addSlackIdToUser(Notification notification);
}
