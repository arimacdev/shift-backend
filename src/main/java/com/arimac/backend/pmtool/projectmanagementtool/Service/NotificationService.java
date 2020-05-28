package com.arimac.backend.pmtool.projectmanagementtool.Service;

import com.arimac.backend.pmtool.projectmanagementtool.dtos.ProjectUserResponseDto;
import com.arimac.backend.pmtool.projectmanagementtool.dtos.SlackNotificationDto;
import com.arimac.backend.pmtool.projectmanagementtool.dtos.TaskGroupTask.TaskGroupTaskUpdateDto;
import com.arimac.backend.pmtool.projectmanagementtool.dtos.TaskUpdateDto;
import com.arimac.backend.pmtool.projectmanagementtool.model.SubTask;
import com.arimac.backend.pmtool.projectmanagementtool.model.Task;
import com.arimac.backend.pmtool.projectmanagementtool.model.TaskGroupTask;

public interface NotificationService {
    @Deprecated
    Object addSlackIdToUser(String userId, SlackNotificationDto slackNotificationDto);

    void sendTaskAssignNotification(Task task); //done
    void sendTaskAssigneeUpdateNotification(Task task, String sender, String newAssignee);  //done
    void sendTaskModificationNotification(Task task, TaskUpdateDto taskUpdateDto, String type, String taskEditor); //done
    void sendTaskFileUploadNotification(String userId, String taskId, String file, String fileName); //done

    //TaskGroup Task Notification
    void sendTaskGroupTaskAssignNotification(TaskGroupTask taskGroupTask);
    void sendTaskGroupTaskAssigneeUpdateNotification(TaskGroupTask taskGroupTask, String userId, String newTaskAssignee);
    void sendTaskGroupTaskContentModificationNotification(TaskGroupTask taskGroupTask, TaskGroupTaskUpdateDto taskGroupTaskUpdateDto, String type, String taskEditor);
    void sendTaskGroupTaskFileUploadNotification(String userId, TaskGroupTask taskGroupTask, String taskGroupId, String file, String fileName);

    void sendSubTaskCreateNotification(String sender, SubTask subTask, ProjectUserResponseDto projectUser, Task task);
    void sendSubTaskUpdateNotification(String sender, Task task, SubTask previous, SubTask modified, ProjectUserResponseDto projectUser, String type);
    void sendSubTaskFlagNotification(String sender, Task task, SubTask subTask, ProjectUserResponseDto projectUser);
    Object checkSlackNotification();

}
