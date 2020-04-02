package com.arimac.backend.pmtool.projectmanagementtool.repository;

import com.arimac.backend.pmtool.projectmanagementtool.dtos.NotificationUpdateDto;
import com.arimac.backend.pmtool.projectmanagementtool.dtos.TaskAlertDto;
import com.arimac.backend.pmtool.projectmanagementtool.model.Notification;

import java.util.List;

public interface NotificationRepository {
//    void addSlackIdToUser(Notification notification);
    void addTaskNotification(Notification notification);
//    Notification getTaskNotification(String )
    void updateTaskNotification(NotificationUpdateDto notificationUpdateDto);
    List<TaskAlertDto> getTaskAlertList();
}
