package com.arimac.backend.pmtool.projectmanagementtool.Service;

import com.arimac.backend.pmtool.projectmanagementtool.dtos.SlackNotificationDto;

public interface NotificationService {
    @Deprecated
    Object addSlackIdToUser(String userId, SlackNotificationDto slackNotificationDto);

}
