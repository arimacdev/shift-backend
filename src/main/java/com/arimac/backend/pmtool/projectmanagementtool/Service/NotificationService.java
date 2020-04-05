package com.arimac.backend.pmtool.projectmanagementtool.Service;

import com.arimac.backend.pmtool.projectmanagementtool.dtos.SlackNotificationDto;
import com.arimac.backend.pmtool.projectmanagementtool.dtos.TaskAssignNotificationDto;

public interface NotificationService {
    @Deprecated
    Object addSlackIdToUser(String userId, SlackNotificationDto slackNotificationDto);
    void sendTaskAssignNotification(TaskAssignNotificationDto taskAssignNotificationDto);

}
