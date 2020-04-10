package com.arimac.backend.pmtool.projectmanagementtool.Service;

import com.arimac.backend.pmtool.projectmanagementtool.dtos.SlackNotificationDto;
import com.arimac.backend.pmtool.projectmanagementtool.dtos.TaskUpdateDto;
import com.arimac.backend.pmtool.projectmanagementtool.model.Task;

public interface NotificationService {
    @Deprecated
    Object addSlackIdToUser(String userId, SlackNotificationDto slackNotificationDto);
    void sendTaskAssignNotification(Task task);
    void sendTaskAssigneeUpdateNotification(Task task, String newAssignee);
    void sendTaskNameModificationNotification(Task task, TaskUpdateDto taskUpdateDto, String type, String taskEditor);
    void sendTaskFileUploadNotification(String userId, String taskId, String file, String fileName);
    Object checkSlackNotification();

}
