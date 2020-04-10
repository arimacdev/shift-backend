package com.arimac.backend.pmtool.projectmanagementtool.Service;

import com.arimac.backend.pmtool.projectmanagementtool.dtos.SlackNotificationDto;
import com.arimac.backend.pmtool.projectmanagementtool.model.Task;

public interface NotificationService {
    @Deprecated
    Object addSlackIdToUser(String userId, SlackNotificationDto slackNotificationDto);
    void sendTaskAssignNotification(Task task);
    void sendTaskAssigneeUpdateNotification(Task task, String newAssignee);
    Object checkSlackNotification();

}
