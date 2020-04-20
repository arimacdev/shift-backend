package com.arimac.backend.pmtool.projectmanagementtool.Service;

import com.arimac.backend.pmtool.projectmanagementtool.dtos.ProjectUserResponseDto;
import com.arimac.backend.pmtool.projectmanagementtool.dtos.SlackNotificationDto;
import com.arimac.backend.pmtool.projectmanagementtool.dtos.TaskUpdateDto;
import com.arimac.backend.pmtool.projectmanagementtool.model.SubTask;
import com.arimac.backend.pmtool.projectmanagementtool.model.Task;

public interface NotificationService {
    @Deprecated
    Object addSlackIdToUser(String userId, SlackNotificationDto slackNotificationDto);
    void sendTaskAssignNotification(Task task);
    void sendTaskAssigneeUpdateNotification(Task task, String sender, String newAssignee);
    void sendTaskModificationNotification(Task task, TaskUpdateDto taskUpdateDto, String type, String taskEditor);
    void sendTaskFileUploadNotification(String userId, String taskId, String file, String fileName);

    void sendSubTaskCreateNotification(String sender, SubTask subTask, ProjectUserResponseDto projectUser, Task task);
    void sendSubTaskUpdateNotification(String sender, Task task, SubTask previous, SubTask modified, ProjectUserResponseDto projectUser, String type);
    void sendSubTaskFlagNotification(String sender, Task task, SubTask subTask, ProjectUserResponseDto projectUser);
    Object checkSlackNotification();

}
