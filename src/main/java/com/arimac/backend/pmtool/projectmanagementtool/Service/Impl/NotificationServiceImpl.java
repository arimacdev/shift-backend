package com.arimac.backend.pmtool.projectmanagementtool.Service.Impl;

import com.arimac.backend.pmtool.projectmanagementtool.Response.Response;
import com.arimac.backend.pmtool.projectmanagementtool.Service.NotificationService;
import com.arimac.backend.pmtool.projectmanagementtool.dtos.*;
import com.arimac.backend.pmtool.projectmanagementtool.dtos.Notification.NotificationRegisterDto;
import com.arimac.backend.pmtool.projectmanagementtool.dtos.Notification.PersonalTaskAlertDto;
import com.arimac.backend.pmtool.projectmanagementtool.dtos.Notification.TaskGroupTaskAlertDto;
import com.arimac.backend.pmtool.projectmanagementtool.dtos.PersonalTask.PersonalTask;
import com.arimac.backend.pmtool.projectmanagementtool.dtos.Slack.SlackBlock;
import com.arimac.backend.pmtool.projectmanagementtool.dtos.Slack.SlackElement;
import com.arimac.backend.pmtool.projectmanagementtool.dtos.TaskGroupTask.TaskGroupTaskUpdateDto;
import com.arimac.backend.pmtool.projectmanagementtool.enumz.Notification.NotificationEnum;
import com.arimac.backend.pmtool.projectmanagementtool.enumz.ResponseMessage;
import com.arimac.backend.pmtool.projectmanagementtool.exception.ErrorMessage;
import com.arimac.backend.pmtool.projectmanagementtool.model.*;
import com.arimac.backend.pmtool.projectmanagementtool.repository.*;
import com.arimac.backend.pmtool.projectmanagementtool.utils.ENVConfig;
import com.arimac.backend.pmtool.projectmanagementtool.utils.OneSignalMessages;
import com.arimac.backend.pmtool.projectmanagementtool.utils.SlackMessages;
import org.joda.time.*;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.*;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Service
public class NotificationServiceImpl implements NotificationService {
    private static final Logger logger = LoggerFactory.getLogger(NotificationServiceImpl.class);

    private static final String CHANNEL = "channel";
    private static final String TEXT = "text";
    private static final String SECTION = "section";
    private static final String PLAIN_TEXT = "plain_text";
    private static final String DIVIDER = "divider";
    private static final String MARK_DOWN = "mrkdwn";
    private static final String BLOCKS = "blocks";
    private static final String NAME = "name";
    private static final String NOTES = "notes";
    private static final String DUE_DATE = "dueDate";
    private static final String STATUS = "status";


    private final NotificationRepository notificationRepository;
    private final UserNotificationRepository userNotificationRepository;
    private final UserRepository userRepository;
    private final TaskRepository taskRepository;
    private final ProjectRepository projectRepository;
    private final TaskGroupTaskRepository taskGroupTaskRepository;
    private final TaskGroupRepository taskGroupRepository;
    private final PersonalTaskRepository personalTaskRepository;

    private final RestTemplate restTemplate;

    public NotificationServiceImpl(NotificationRepository notificationRepository, UserNotificationRepository userNotificationRepository, UserRepository userRepository, TaskRepository taskRepository, ProjectRepository projectRepository, TaskGroupTaskRepository taskGroupTaskRepository, TaskGroupRepository taskGroupRepository, PersonalTaskRepository personalTaskRepository, RestTemplate restTemplate) {
        this.notificationRepository = notificationRepository;
        this.userNotificationRepository = userNotificationRepository;
        this.userRepository = userRepository;
        this.taskRepository = taskRepository;
        this.projectRepository = projectRepository;
        this.taskGroupTaskRepository = taskGroupTaskRepository;
        this.taskGroupRepository = taskGroupRepository;
        this.personalTaskRepository = personalTaskRepository;
        this.restTemplate = restTemplate;
    }

    @Deprecated
    @Override
    public Object addSlackIdToUser(String userId, SlackNotificationDto slackNotificationDto) {
        if (slackNotificationDto.getAssigneeSlackId().equals(slackNotificationDto.getSlackAssignerId()))
            return new ErrorMessage(ResponseMessage.UNAUTHORIZED, HttpStatus.UNAUTHORIZED);
        User user = userRepository.getUserByUserId(slackNotificationDto.getSlackAssignerId());
        if (user == null)
            return new ErrorMessage(ResponseMessage.USER_NOT_FOUND, HttpStatus.NOT_FOUND);
        userRepository.addSlackIdToUser(userId, slackNotificationDto.getAssigneeSlackId());
        return new Response(ResponseMessage.SUCCESS);
    }

    @Override
    public Object registerForNotifications(String userId, NotificationRegisterDto notificationRegisterDto) {
        UserNotification userNotification = new UserNotification();
        userNotification.setUserId(notificationRegisterDto.getSubscriberId());
        userNotification.setSubscriptionId(notificationRegisterDto.getSubscriptionId());
        userNotification.setProvider(notificationRegisterDto.getProvider().toString());
        userNotification.setPlatform(notificationRegisterDto.getPlatform().toString());
        userNotification.setNotificationStatus(true);
        userNotificationRepository.registerForNotifications(userNotification);
        return new Response(ResponseMessage.SUCCESS, HttpStatus.OK);
    }

    private HttpHeaders getHttpHeaders(){
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set("Authorization", "Bearer " + ENVConfig.SLACK_BOT_TOKEN);
        httpHeaders.set("Content-Type", "application/json");

        return httpHeaders;
    }

    @Override
    public void sendTaskAssignNotification(Task task) {
        User user = userRepository.getUserByUserId(task.getTaskAssignee());
        List<UserNotification> oneSignalDevices = userNotificationRepository.getNotificationUserByProviderAndStatus(task.getTaskAssignee(), NotificationEnum.OneSignal.toString(), true);
        if ((user.getUserSlackId() != null && user.getNotification()) || !oneSignalDevices.isEmpty()) {
            Project project = projectRepository.getProjectById(task.getProjectId());
            User sender = userRepository.getUserByUserId(task.getTaskInitiator());
            if (!oneSignalDevices.isEmpty()) {
                for (UserNotification device : oneSignalDevices) {
                    StringBuilder oneSignalAssigneeNotif = getTaskOneSignalMessage(user,task,project,OneSignalMessages.TASK_ASSIGNMENT);
                    oneSignalAssigneeNotif.append(OneSignalMessages.ASSIGNED_BY);
                    oneSignalAssigneeNotif.append(sender.getFirstName());
                    oneSignalAssigneeNotif.append(" ");
                    oneSignalAssigneeNotif.append(sender.getLastName());

                    sendOneSignalNotification(oneSignalAssigneeNotif.toString(), device.getSubscriptionId());
                }
            }
            if (user.getUserSlackId() != null && user.getNotification()) {
                JSONObject payload = new JSONObject();
                payload.put(CHANNEL, user.getUserSlackId());
                payload.put(TEXT, SlackMessages.TASK_ASSIGNMENT_TITLE);
                List<SlackBlock> blocks = new ArrayList<>();

                SlackBlock headerBlock = new SlackBlock();
                headerBlock.setType(SECTION);
                headerBlock.getText().setType(MARK_DOWN);
                StringBuilder greeting = new StringBuilder();
                greeting.append(getWelcomeAddressing(user.getUserSlackId()));
                greeting.append(SlackMessages.TASK_ASSIGNMENT_GREETING);
                headerBlock.getText().setText(greeting.toString());
                headerBlock.setAccessory(null);
                blocks.add(headerBlock);

                SlackBlock divider = new SlackBlock();
                divider.setType(DIVIDER);
                divider.setText(null);
                divider.setAccessory(null);
                blocks.add(divider);

                SlackBlock body = new SlackBlock();
                body.setType(SECTION);
                body.getText().setType(MARK_DOWN);
                StringBuilder bodyText = new StringBuilder();
                bodyText.append(SlackMessages.TASK_ICON);
                bodyText.append(getTaskUrl(task));
                bodyText.append(SlackMessages.PROJECT_ICON);
                bodyText.append(getProjectUrl(project));
                bodyText.append(SlackMessages.ASSIGNED_BY_ICON);
                if (sender.getUserSlackId() != null) {
                    bodyText.append(getMentionedName(sender.getUserSlackId()));
                } else {
                    bodyText.append(sender.getFirstName());
                    bodyText.append(" ");
                    bodyText.append(sender.getLastName());
                }
                bodyText.append(SlackMessages.DUE_DATE_ICON);
                if (task.getTaskDueDateAt() != null) {
                    DateTime dueUtc = new DateTime(task.getTaskDueDateAt(), DateTimeZone.forID("UTC"));
                    bodyText.append(getDueDate(dueUtc));
                } else {
                    bodyText.append("No Due Date Assigned");
                }
                body.getText().setText(bodyText.toString());
                body.getAccessory().setType("image");
                body.getAccessory().setImage_url(SlackMessages.ASSIGNMENT_THUMBNAIL);
                body.getAccessory().setAlt_text("Assignee Thumbnail");
                blocks.add(body);
                blocks.add(divider);

                payload.put(BLOCKS, blocks);
                StringBuilder url = new StringBuilder();
                url.append(ENVConfig.SLACK_BASE_URL);
                url.append("/chat.postMessage");
                logger.info("Slack Message Url {}", url);
                HttpEntity<Object> entity = new HttpEntity<>(payload.toString(), getHttpHeaders());
                Object response = restTemplate.exchange(url.toString(), HttpMethod.POST, entity, String.class);
            }
        }
    }

    private StringBuilder getTaskOneSignalMessage(User user, Task task, Project project, String title){
        StringBuilder message = new StringBuilder();
        message.append(OneSignalMessages.GREETING);
        message.append(user.getFirstName());
        message.append(title);
        message.append(OneSignalMessages.TASK);
        message.append(task.getTaskName());
        message.append(OneSignalMessages.PROJECT);
        message.append(project.getProjectName());

        return message;

    }

    private StringBuilder getTaskGroupTaskOneSignalMessage(User user, TaskGroupTask task, TaskGroup taskGroup, String title){
        StringBuilder message = new StringBuilder();
        message.append(OneSignalMessages.GREETING);
        message.append(user.getFirstName());
        message.append(title);
        message.append(OneSignalMessages.TASKGROUP_TASK);
        message.append(task.getTaskName());
        message.append(OneSignalMessages.TASKGROUP);
        message.append(taskGroup.getTaskGroupName());

        return message;
    }

    @Override
    public void sendTaskAssigneeUpdateNotification(Task task, String userId, String newTaskAssignee) {
        User newAssignee = userRepository.getUserByUserId(newTaskAssignee);
        List<UserNotification> oneSignalDevices = userNotificationRepository.getNotificationUserByProviderAndStatus(newTaskAssignee, NotificationEnum.OneSignal.toString(), true);
        if ((newAssignee.getUserSlackId() != null && newAssignee.getNotification()) || !oneSignalDevices.isEmpty()) {
            User previous = userRepository.getUserByUserId(task.getTaskAssignee());
            Project project = projectRepository.getProjectById(task.getProjectId());
            User sender = userRepository.getUserByUserId(userId);
            if (!oneSignalDevices.isEmpty()) {
                for (UserNotification device : oneSignalDevices) {
                    StringBuilder oneSignalTaskAssigneeUpdateNotf = getTaskOneSignalMessage(newAssignee, task, project, OneSignalMessages.TASK_ASSIGNMENT_UPDATE);
                    oneSignalTaskAssigneeUpdateNotf.append(OneSignalMessages.TRANSITION);
                    oneSignalTaskAssigneeUpdateNotf.append(previous.getFirstName());
                    oneSignalTaskAssigneeUpdateNotf.append(" ");
                    oneSignalTaskAssigneeUpdateNotf.append(previous.getLastName());
                    oneSignalTaskAssigneeUpdateNotf.append(OneSignalMessages.ARROW);
                    oneSignalTaskAssigneeUpdateNotf.append(newAssignee.getFirstName());
                    oneSignalTaskAssigneeUpdateNotf.append(" ");
                    oneSignalTaskAssigneeUpdateNotf.append(newAssignee.getLastName());

                    sendOneSignalNotification(oneSignalTaskAssigneeUpdateNotf.toString(), device.getSubscriptionId());
                }
            }
            if (newAssignee.getUserSlackId() != null && newAssignee.getNotification()) {
                JSONObject payload = new JSONObject();
                payload.put(CHANNEL, newAssignee.getUserSlackId());
                payload.put(TEXT, SlackMessages.TASK_ASSIGNEE_UPDATE_TITLE);
                List<SlackBlock> blocks = new ArrayList<>();
                SlackBlock headerBlock = new SlackBlock();
                headerBlock.setType(SECTION);
                headerBlock.getText().setType(MARK_DOWN);
                StringBuilder greeting = new StringBuilder();
                greeting.append(getWelcomeAddressing(newAssignee.getUserSlackId()));
                greeting.append(SlackMessages.TASK_ASSIGNMENT_TRANSITION_GREETING);
                headerBlock.getText().setText(greeting.toString());
                headerBlock.setAccessory(null);
                headerBlock.setElements(null);
                blocks.add(headerBlock);

                SlackBlock divider = new SlackBlock();
                divider.setType(DIVIDER);
                divider.setText(null);
                divider.setAccessory(null);
                blocks.add(divider);

                SlackBlock body = new SlackBlock();
                body.setType(SECTION);
                body.getText().setType(MARK_DOWN);
                StringBuilder bodyText = new StringBuilder();
                bodyText.append(SlackMessages.TASK_ICON);
                bodyText.append(getTaskUrl(task));
                bodyText.append(SlackMessages.PROJECT_ICON);
                bodyText.append(getProjectUrl(project));
                bodyText.append(SlackMessages.TRANSITION_ICON);
                if (previous.getUserSlackId() != null) {
                    bodyText.append(getMentionedName(previous.getUserSlackId()));
                } else {
                    bodyText.append(previous.getFirstName());
                    bodyText.append(" ");
                    bodyText.append(previous.getLastName());
                }
                bodyText.append(SlackMessages.ARROW_ICON);
                if (newAssignee.getUserSlackId() != null) {
                    bodyText.append(getMentionedName(newAssignee.getUserSlackId()));
                } else {
                    bodyText.append(newAssignee.getFirstName());
                    bodyText.append(" ");
                    bodyText.append(newAssignee.getLastName());
                }
                bodyText.append(SlackMessages.ASSIGNED_BY_ICON);
                if (sender.getUserSlackId() != null) {
                    bodyText.append(getMentionedName(sender.getUserSlackId()));
                } else {
                    bodyText.append(sender.getFirstName());
                    bodyText.append(" ");
                    bodyText.append(sender.getLastName());
                }
                bodyText.append(SlackMessages.DUE_DATE_ICON);
                if (task.getTaskDueDateAt() != null) {
                    DateTime dueUtc = new DateTime(task.getTaskDueDateAt(), DateTimeZone.forID("UTC"));
                    bodyText.append(getDueDate(dueUtc));
                } else {
                    bodyText.append("No Due Date Assigned");
                }
                body.getText().setText(bodyText.toString());
                body.getAccessory().setType("image");
                body.getAccessory().setImage_url(SlackMessages.ASSIGNMENT_UPDATE_THUMBNAIL);
                body.getAccessory().setAlt_text("Calender Thumbnail");
                blocks.add(body);
                blocks.add(getFooter(task.getTaskStatus().toString()));
                blocks.add(divider);

                payload.put(BLOCKS, blocks);
                StringBuilder url = new StringBuilder();
                url.append(ENVConfig.SLACK_BASE_URL);
                url.append("/chat.postMessage");
                logger.info("Slack Message Url {}", url);
                HttpEntity<Object> entity = new HttpEntity<>(payload.toString(), getHttpHeaders());
                Object response = restTemplate.exchange(url.toString(), HttpMethod.POST, entity, String.class);
            }
        }
    }

    @Override
    public void sendTaskModificationNotification(Task task, TaskUpdateDto taskUpdateDto, String type, String taskEditor) {
        User user = userRepository.getUserByUserId(task.getTaskAssignee());
        List<UserNotification> oneSignalDevices = userNotificationRepository.getNotificationUserByProviderAndStatus(task.getTaskAssignee(), NotificationEnum.OneSignal.toString(), true);
        if ((user.getUserSlackId() != null && user.getNotification()) || !oneSignalDevices.isEmpty()) {
            User editor = userRepository.getUserByUserId(taskEditor);
            Project project = projectRepository.getProjectById(task.getProjectId());

            if (!oneSignalDevices.isEmpty()) {
                for (UserNotification device : oneSignalDevices) {
                    StringBuilder oneSignalTaskUpdateNotf = getTaskOneSignalMessage(user, task, project, OneSignalMessages.TASK_MODIFICATION);
                    switch (type) {
                        case NAME:
                            oneSignalTaskUpdateNotf.append(OneSignalMessages.MODIFIED);
                            oneSignalTaskUpdateNotf.append(taskUpdateDto.getTaskName());
                            oneSignalTaskUpdateNotf.append(OneSignalMessages.PREVIOUS);
                            oneSignalTaskUpdateNotf.append(task.getTaskName());
                            break;
                        case NOTES:
                            oneSignalTaskUpdateNotf.append(OneSignalMessages.MODIFIED);
                            oneSignalTaskUpdateNotf.append(taskUpdateDto.getTaskNotes());
                            oneSignalTaskUpdateNotf.append(OneSignalMessages.PREVIOUS);
                            if (task.getTaskNote() == null || task.getTaskNote().isEmpty())
                                oneSignalTaskUpdateNotf.append("<No Previous Task Note Content>");
                            else
                                oneSignalTaskUpdateNotf.append(task.getTaskNote());
                            break;
                        case DUE_DATE:
                            oneSignalTaskUpdateNotf.append(OneSignalMessages.MODIFIED);
                            oneSignalTaskUpdateNotf.append(taskUpdateDto.getTaskDueDate());
                            oneSignalTaskUpdateNotf.append(OneSignalMessages.PREVIOUS);
                            if (task.getTaskDueDateAt() == null)
                                oneSignalTaskUpdateNotf.append("No Previous Due Date");
                            else
                                oneSignalTaskUpdateNotf.append(getDate(task.getTaskDueDateAt()));
                            break;
                        case STATUS:
                            oneSignalTaskUpdateNotf.append(OneSignalMessages.TRANSITION);
                            oneSignalTaskUpdateNotf.append(task.getTaskStatus().toString());
                            oneSignalTaskUpdateNotf.append(OneSignalMessages.ARROW);
                            oneSignalTaskUpdateNotf.append(taskUpdateDto.getTaskStatus());
                            break;
                        default:
                            return;
                    }
                    sendOneSignalNotification(oneSignalTaskUpdateNotf.toString(), device.getSubscriptionId());
                }
            }

            if (user.getUserSlackId() != null && user.getNotification()) {
                JSONObject payload = new JSONObject();
                payload.put(CHANNEL, user.getUserSlackId());
                payload.put(TEXT, SlackMessages.TASK_MODIFICATION_TITLE);
                List<SlackBlock> blocks = new ArrayList<>();

                SlackBlock headerBlock = addHeaderBlock(user.getUserSlackId(), SlackMessages.TASK_MODIFICATION_GREETING);
                headerBlock.setAccessory(null);
                blocks.add(headerBlock);
                blocks.add(addDivider());

                SlackBlock body = new SlackBlock();
                body.setType(SECTION);
                body.getText().setType(MARK_DOWN);
                StringBuilder bodyText = new StringBuilder();
                bodyText.append(SlackMessages.TASK_ICON);
                bodyText.append(getTaskUrl(task));
                body.getText().setText(bodyText.toString());
                bodyText.append(SlackMessages.PROJECT_ICON);
                bodyText.append(getProjectUrl(project));
                switch (type) {
                    case NAME:
                        bodyText.append(SlackMessages.MODIFIED_NAME_ICON);
                        bodyText.append(taskUpdateDto.getTaskName());
                        bodyText.append(SlackMessages.PREVIOUS_NAME_ICON);
                        bodyText.append(task.getTaskName());
                        setNotificationThumbnail(body, SlackMessages.TASK_NAME_THUMBNAIL_TEXT, SlackMessages.UPDATE_TASK_NAME_THUMBNAIL);
                        break;
                    case NOTES:
                        bodyText.append(SlackMessages.MODIFIED_NOTES_ICON);
                        bodyText.append(taskUpdateDto.getTaskNotes());
                        bodyText.append(SlackMessages.PREVIOUS_NOTES_ICON);
                        if (task.getTaskNote() == null || task.getTaskNote().isEmpty())
                            bodyText.append("<No Previous Task Note Content>");
                        else
                            bodyText.append(task.getTaskNote());
                        setNotificationThumbnail(body, SlackMessages.TASK_NOTE_THUMBNAIL_TEXT, SlackMessages.UPDATE_TASK_NOTE_THUMBNAIL);
                        break;
                    case DUE_DATE:
                        bodyText.append(SlackMessages.MODIFIED_DUE_DATE_ICON);
                        bodyText.append(getDate(taskUpdateDto.getTaskDueDate()));
                        bodyText.append(SlackMessages.PREVIOUS_DUE_DATE_ICON);
                        if (task.getTaskDueDateAt() == null)
                            bodyText.append("No Previous Due Date");
                        else
                            bodyText.append(getDate(task.getTaskDueDateAt()));
                        setNotificationThumbnail(body, SlackMessages.TASK_DUE_THUMBNAIL_TEXT, SlackMessages.UPDATE_TASK_DUE_DATE_THUMBNAIL);
                        break;
                    case STATUS:
                        bodyText.append(SlackMessages.TRANSITION_ICON);
                        bodyText.append(task.getTaskStatus());
                        bodyText.append(SlackMessages.ARROW_ICON);
                        bodyText.append(taskUpdateDto.getTaskStatus());
                        setNotificationThumbnail(body, SlackMessages.TASK_TRANSITION_THUMBNAIL_TEXT, SlackMessages.TRANSITION_THUMBNAIL);
                        break;
                    default:
                        return;
                }
                bodyText.append(SlackMessages.MODIFIED_BY_ICON);
                if (editor.getUserSlackId() != null) {
                    bodyText.append(getMentionedName(editor.getUserSlackId()));
                } else {
                    bodyText.append(editor.getFirstName());
                    bodyText.append(" ");
                    bodyText.append(editor.getLastName());
                }

                body.getText().setText(bodyText.toString());

                blocks.add(body);
                if (type.equals(STATUS))
                    blocks.add(getFooter(taskUpdateDto.getTaskStatus().toString()));
                else
                    blocks.add(getFooter(task.getTaskStatus().toString()));
                blocks.add(addDivider());

                payload.put(BLOCKS, blocks);
                StringBuilder url = new StringBuilder();
                url.append(ENVConfig.SLACK_BASE_URL);
                url.append("/chat.postMessage");
                logger.info("Slack Message Url {}", url);
                HttpEntity<Object> entity = new HttpEntity<>(payload.toString(), getHttpHeaders());
                Object response = restTemplate.exchange(url.toString(), HttpMethod.POST, entity, String.class);
            }
        }
    }

    @Override
    public void sendTaskFileUploadNotification(String userId, String taskId, String file, String fileName) {
        Task task = taskRepository.getProjectTask(taskId);
        User user = userRepository.getUserByUserId(task.getTaskAssignee());
        List<UserNotification> oneSignalDevices = userNotificationRepository.getNotificationUserByProviderAndStatus(task.getTaskAssignee(), NotificationEnum.OneSignal.toString(), true);
        if ((user.getUserSlackId() != null && user.getNotification()) || !oneSignalDevices.isEmpty()) {
            Project project = projectRepository.getProjectById(task.getProjectId());
            if (!oneSignalDevices.isEmpty()) {
             for (UserNotification device: oneSignalDevices){
                 StringBuilder oneSignalFileUploadNotf = getTaskOneSignalMessage(user, task, project, OneSignalMessages.TASK_FILE_UPLOAD);
                 oneSignalFileUploadNotf.append(OneSignalMessages.UPLOADED_FILE);
                 oneSignalFileUploadNotf.append(fileName);
                 oneSignalFileUploadNotf.append(OneSignalMessages.UPLOADED_BY);
                 oneSignalFileUploadNotf.append(user.getFirstName());
                 oneSignalFileUploadNotf.append(" ");
                 oneSignalFileUploadNotf.append(user.getLastName());
                 sendOneSignalNotification(oneSignalFileUploadNotf.toString(), device.getSubscriptionId());
             }
            }
            if (user.getUserSlackId() != null && user.getNotification()) {
                JSONObject payload = new JSONObject();
                payload.put(CHANNEL, user.getUserSlackId());
                payload.put(TEXT, SlackMessages.TASK_FILE_UPLOAD_NOTIFICATION_TITLE);
                List<SlackBlock> blocks = new ArrayList<>();

                SlackBlock headerBlock = new SlackBlock();
                headerBlock.setType(SECTION);
                headerBlock.getText().setType(MARK_DOWN);
                StringBuilder greeting = new StringBuilder();
                greeting.append(getWelcomeAddressing(user.getUserSlackId()));
                greeting.append(SlackMessages.TASK_FILE_UPLOAD_GREETING);

                headerBlock.getText().setText(greeting.toString());
                headerBlock.setAccessory(null);
                blocks.add(headerBlock);

                SlackBlock divider = new SlackBlock();
                divider.setType(DIVIDER);
                divider.setText(null);
                divider.setAccessory(null);
                blocks.add(divider);

                SlackBlock body = new SlackBlock();
                body.setType(SECTION);
                body.getText().setType(MARK_DOWN);
                StringBuilder bodyText = new StringBuilder();
                bodyText.append(SlackMessages.TASK_ICON);
                bodyText.append(getTaskUrl(task));
                bodyText.append(SlackMessages.PROJECT_ICON);
                bodyText.append(getProjectUrl(project));
                bodyText.append(SlackMessages.UPLOADED_BY_ICON);
                bodyText.append(getMentionedName(user.getUserSlackId()));
                body.getText().setText(bodyText.toString());
                body.getAccessory().setType("image");
                body.getAccessory().setImage_url(SlackMessages.FILE_UPLOAD_THUMBNAIL);
                body.getAccessory().setAlt_text("File Upload Thumbnail");
                blocks.add(body);

                SlackBlock fileUpload = new SlackBlock();
                fileUpload.setType(SECTION);
                fileUpload.getText().setType(MARK_DOWN);
                StringBuilder fileText = new StringBuilder();
                fileText.append(SlackMessages.UPLOADED_FILE_ICON);
                fileText.append("*<");
                fileText.append(file);
                fileText.append("|");
                fileText.append(fileName);
                fileText.append(">*");
                fileUpload.getText().setText(fileText.toString());
                fileUpload.setAccessory(null);
                blocks.add(fileUpload);
                blocks.add(getFooter(task.getTaskStatus().toString()));
                blocks.add(divider);
                payload.put(BLOCKS, blocks);
                StringBuilder url = new StringBuilder();
                url.append(ENVConfig.SLACK_BASE_URL);
                url.append("/chat.postMessage");
                logger.info("Slack Message Url {}", url);
                HttpEntity<Object> entity = new HttpEntity<>(payload.toString(), getHttpHeaders());
                Object response = restTemplate.exchange(url.toString(), HttpMethod.POST, entity, String.class);
            }
        }
    }

    @Override
    public void sendTaskDeleteNotification(Task task, String deletedBy) {
        User user = userRepository.getUserByUserId(task.getTaskAssignee());
        List<UserNotification> oneSignalDevices = userNotificationRepository.getNotificationUserByProviderAndStatus(task.getTaskAssignee(), NotificationEnum.OneSignal.toString(), true);
        if (((user.getUserSlackId() != null && user.getNotification())) || !oneSignalDevices.isEmpty()) {
            Project project = projectRepository.getProjectById(task.getProjectId());
            User remover = userRepository.getUserByUserId(deletedBy);
            if (!oneSignalDevices.isEmpty()) {
                for (UserNotification device: oneSignalDevices){
                    StringBuilder oneSignalTaskDeleteNotf = getTaskOneSignalMessage(user, task, project, OneSignalMessages.TASK_DELETE);
                    oneSignalTaskDeleteNotf.append(OneSignalMessages.DELETED_BY);
                    oneSignalTaskDeleteNotf.append(remover.getFirstName());
                    oneSignalTaskDeleteNotf.append(" ");
                    oneSignalTaskDeleteNotf.append(remover.getLastName());
                    sendOneSignalNotification(oneSignalTaskDeleteNotf.toString(), device.getSubscriptionId());
                }
            }
            if (user.getUserSlackId() != null && user.getNotification()) {
                JSONObject payload = new JSONObject();
                payload.put(CHANNEL, user.getUserSlackId());
                payload.put(TEXT, SlackMessages.TASK_ASSIGNEE_DELETE_TITLE);
                List<SlackBlock> blocks = new ArrayList<>();

                SlackBlock headerBlock = addHeaderBlock(user.getUserSlackId(), SlackMessages.TASK_DELETION_GREETING);
                headerBlock.setAccessory(null);
                headerBlock.setElements(null);

                blocks.add(headerBlock);
                blocks.add(addDivider());

                SlackBlock body = new SlackBlock();
                body.setType(SECTION);
                body.getText().setType(MARK_DOWN);
                StringBuilder bodyText = new StringBuilder();
                bodyText.append(SlackMessages.TASK_ICON);
                bodyText.append(getTaskUrl(task));
                bodyText.append(SlackMessages.PROJECT_ICON);
                bodyText.append(getProjectUrl(project));
                bodyText.append(SlackMessages.DELETED_BY_ICON);
                if (remover.getUserSlackId() != null) {
                    bodyText.append(getMentionedName(remover.getUserSlackId()));
                } else {
                    bodyText.append(remover.getFirstName());
                    bodyText.append(" ");
                    bodyText.append(remover.getLastName());
                }
                body.getText().setText(bodyText.toString());
                setNotificationThumbnail(body, SlackMessages.TASK_DELETE_THUMBNAIL_TEXT, SlackMessages.DELETED_BY_THUMBNAIL);
                blocks.add(body);
                blocks.add(addDivider());

                payload.put(BLOCKS, blocks);
                StringBuilder url = new StringBuilder();
                url.append(ENVConfig.SLACK_BASE_URL);
                url.append("/chat.postMessage");
                logger.info("Slack Message Url {}", url);
                HttpEntity<Object> entity = new HttpEntity<>(payload.toString(), getHttpHeaders());
                Object response = restTemplate.exchange(url.toString(), HttpMethod.POST, entity, String.class);
            }
        }
    }

    @Override
    public void sendTaskGroupTaskAssignNotification(TaskGroupTask taskGroupTask) {
        User user = userRepository.getUserByUserId(taskGroupTask.getTaskAssignee());
        List<UserNotification> oneSignalDevices = userNotificationRepository.getNotificationUserByProviderAndStatus(taskGroupTask.getTaskAssignee(), NotificationEnum.OneSignal.toString(), true);
        if ((user.getUserSlackId() != null && user.getNotification()) || !oneSignalDevices.isEmpty()) {
            TaskGroup taskGroup = taskGroupRepository.getTaskGroupById(taskGroupTask.getTaskGroupId());
            User sender = userRepository.getUserByUserId(taskGroupTask.getTaskInitiator());
            if (!oneSignalDevices.isEmpty()) {
                for (UserNotification device : oneSignalDevices) {
                    StringBuilder oneSignalTaskGroupTaskNtf = getTaskGroupTaskOneSignalMessage(user, taskGroupTask, taskGroup, OneSignalMessages.TASK_GROUP_TASK_ASSIGNMENT);
                    oneSignalTaskGroupTaskNtf.append(OneSignalMessages.ASSIGNED_BY);
                    oneSignalTaskGroupTaskNtf.append(sender.getFirstName());
                    oneSignalTaskGroupTaskNtf.append(" ");
                    oneSignalTaskGroupTaskNtf.append(sender.getLastName());

                    sendOneSignalNotification(oneSignalTaskGroupTaskNtf.toString(), device.getSubscriptionId());
                }
            }
            if (user.getUserSlackId() != null && user.getNotification()) {
                JSONObject payload = new JSONObject();
                payload.put(CHANNEL, user.getUserSlackId());
                payload.put(TEXT, SlackMessages.TASKGROUP_TASK_ASSIGNMENT_TITLE);
                List<SlackBlock> blocks = new ArrayList<>();

                SlackBlock headerBlock = new SlackBlock();
                headerBlock.setType(SECTION);
                headerBlock.getText().setType(MARK_DOWN);
                StringBuilder greeting = new StringBuilder();
                greeting.append(getWelcomeAddressing(user.getUserSlackId()));
                greeting.append(SlackMessages.TASKGROUP_TASK_GREETING);
                headerBlock.getText().setText(greeting.toString());
                headerBlock.setAccessory(null);
                blocks.add(headerBlock);

                blocks.add(addDivider());

                SlackBlock body = new SlackBlock();
                body.setType(SECTION);
                body.getText().setType(MARK_DOWN);
                StringBuilder bodyText = new StringBuilder();
                bodyText.append(SlackMessages.TASKGROUP_TASK_ICON);
                bodyText.append(getTaskGroupTaskUrl(taskGroupTask));
                bodyText.append(SlackMessages.TASKGROUP_ICON);
                bodyText.append(getTaskGroupUrl(taskGroup));
                bodyText.append(SlackMessages.ASSIGNED_BY_ICON);
                if (sender.getUserSlackId() != null) {
                    bodyText.append(getMentionedName(sender.getUserSlackId()));
                } else {
                    bodyText.append(sender.getFirstName());
                    bodyText.append(" ");
                    bodyText.append(sender.getLastName());
                }
                bodyText.append(SlackMessages.DUE_DATE_ICON);
                if (taskGroupTask.getTaskDueDateAt() != null) {
                    DateTime dueUtc = new DateTime(taskGroupTask.getTaskDueDateAt(), DateTimeZone.forID("UTC"));
                    bodyText.append(getDueDate(dueUtc));
                } else {
                    bodyText.append("No Due Date Assigned");
                }
                body.getText().setText(bodyText.toString());
                body.getAccessory().setType("image");
                body.getAccessory().setImage_url(SlackMessages.ASSIGNMENT_THUMBNAIL);
                body.getAccessory().setAlt_text("Assignee Thumbnail");
                blocks.add(body);
                blocks.add(addDivider());

                payload.put(BLOCKS, blocks);
                StringBuilder url = new StringBuilder();
                url.append(ENVConfig.SLACK_BASE_URL);
                url.append("/chat.postMessage");
                logger.info("Slack Message Url {}", url);
                HttpEntity<Object> entity = new HttpEntity<>(payload.toString(), getHttpHeaders());
                Object response = restTemplate.exchange(url.toString(), HttpMethod.POST, entity, String.class);
            }
        }
    }

    @Override
    public void sendTaskGroupTaskAssigneeUpdateNotification(TaskGroupTask taskGroupTask, String userId, String newTaskAssignee) {
        List<UserNotification> oneSignalDevices = userNotificationRepository.getNotificationUserByProviderAndStatus(newTaskAssignee, NotificationEnum.OneSignal.toString(), true);
        User newAssignee = userRepository.getUserByUserId(newTaskAssignee);
        if ((newAssignee.getUserSlackId() != null && newAssignee.getNotification()) || !oneSignalDevices.isEmpty()) {
            User previous = userRepository.getUserByUserId(taskGroupTask.getTaskAssignee());
            TaskGroup taskGroup = taskGroupRepository.getTaskGroupById(taskGroupTask.getTaskGroupId());
            User sender = userRepository.getUserByUserId(userId);
            if (!oneSignalDevices.isEmpty()) {
                for (UserNotification device: oneSignalDevices){
                    StringBuilder oneSignalTaskGroupTaskNtf = getTaskGroupTaskOneSignalMessage(newAssignee, taskGroupTask, taskGroup, OneSignalMessages.TASK_GROUP_TASK_ASSIGNMENT_UPDATE);
                    oneSignalTaskGroupTaskNtf.append(OneSignalMessages.TRANSITION);
                    oneSignalTaskGroupTaskNtf.append(previous.getFirstName());
                    oneSignalTaskGroupTaskNtf.append(" ");
                    oneSignalTaskGroupTaskNtf.append(previous.getLastName());
                    oneSignalTaskGroupTaskNtf.append(OneSignalMessages.ARROW);
                    oneSignalTaskGroupTaskNtf.append(newAssignee.getFirstName());
                    oneSignalTaskGroupTaskNtf.append(" ");
                    oneSignalTaskGroupTaskNtf.append(newAssignee.getLastName());
                    oneSignalTaskGroupTaskNtf.append(OneSignalMessages.ASSIGNED_BY);
                    oneSignalTaskGroupTaskNtf.append(sender.getFirstName());
                    oneSignalTaskGroupTaskNtf.append(sender.getLastName());
                    sendOneSignalNotification(oneSignalTaskGroupTaskNtf.toString(), device.getSubscriptionId());
                }
            }
            if (newAssignee.getUserSlackId() != null && newAssignee.getNotification()) {
                JSONObject payload = new JSONObject();
                payload.put(CHANNEL, newAssignee.getUserSlackId());
                payload.put(TEXT, SlackMessages.TASKGROUP_TASK_ASSIGNEE_UPDATE_TITLE);
                List<SlackBlock> blocks = new ArrayList<>();
                SlackBlock headerBlock = addHeaderBlock(newAssignee.getUserSlackId(), SlackMessages.TASKGROUP_TASK_MODIFICATION_GREETING);
                headerBlock.setAccessory(null);
                headerBlock.setElements(null);
                blocks.add(headerBlock);

                blocks.add(addDivider());

                SlackBlock body = new SlackBlock();
                body.setType(SECTION);
                body.getText().setType(MARK_DOWN);
                StringBuilder bodyText = new StringBuilder();
                bodyText.append(SlackMessages.TASKGROUP_TASK_ICON);
                bodyText.append(getTaskGroupTaskUrl(taskGroupTask));
                bodyText.append(SlackMessages.TASKGROUP_ICON);
                bodyText.append(getTaskGroupUrl(taskGroup));
                bodyText.append(SlackMessages.TRANSITION_ICON);
                if (previous.getUserSlackId() != null) {
                    bodyText.append(getMentionedName(previous.getUserSlackId()));
                } else {
                    bodyText.append(previous.getFirstName());
                    bodyText.append(" ");
                    bodyText.append(previous.getLastName());
                }
                bodyText.append(SlackMessages.ARROW_ICON);
                if (newAssignee.getUserSlackId() != null) {
                    bodyText.append(getMentionedName(newAssignee.getUserSlackId()));
                } else {
                    bodyText.append(newAssignee.getFirstName());
                    bodyText.append(" ");
                    bodyText.append(newAssignee.getLastName());
                }
                bodyText.append(SlackMessages.ASSIGNED_BY_ICON);
                if (sender.getUserSlackId() != null) {
                    bodyText.append(getMentionedName(sender.getUserSlackId()));
                } else {
                    bodyText.append(sender.getFirstName());
                    bodyText.append(" ");
                    bodyText.append(sender.getLastName());
                }
                body.getText().setText(bodyText.toString());
                setNotificationThumbnail(body, SlackMessages.TASKGROUP_TASK_ASSIGNEE_UPDATE, SlackMessages.ASSIGNMENT_UPDATE_THUMBNAIL);

                blocks.add(body);
                blocks.add(getFooter(taskGroupTask.getTaskStatus().toString()));
                blocks.add(addDivider());

                payload.put(BLOCKS, blocks);
                StringBuilder url = new StringBuilder();
                url.append(ENVConfig.SLACK_BASE_URL);
                url.append("/chat.postMessage");
                logger.info("Slack Message Url {}", url);
                HttpEntity<Object> entity = new HttpEntity<>(payload.toString(), getHttpHeaders());
                Object response = restTemplate.exchange(url.toString(), HttpMethod.POST, entity, String.class);
            }
        }
    }

    @Override
    public void sendTaskGroupTaskContentModificationNotification(TaskGroupTask taskGroupTask, TaskGroupTaskUpdateDto taskGroupTaskUpdateDto, String type, String taskEditor) {
        User user = userRepository.getUserByUserId(taskGroupTask.getTaskAssignee());
        List<UserNotification> oneSignalDevices = userNotificationRepository.getNotificationUserByProviderAndStatus(user.getUserId(), NotificationEnum.OneSignal.toString(), true);
        if ((user.getUserSlackId() != null && user.getNotification()) || !oneSignalDevices.isEmpty()) {
            User editor = userRepository.getUserByUserId(taskEditor);
            TaskGroup taskGroup = taskGroupRepository.getTaskGroupById(taskGroupTask.getTaskGroupId());
            if (!oneSignalDevices.isEmpty()) {
                for (UserNotification device: oneSignalDevices){
                    StringBuilder oneSignalTaskGroupTaskNtf = getTaskGroupTaskOneSignalMessage(user, taskGroupTask, taskGroup, OneSignalMessages.TASK_GROUP_TASK_MODIFICATION);
                    switch (type) {
                        case NAME:
                            oneSignalTaskGroupTaskNtf.append(OneSignalMessages.MODIFIED);
                            oneSignalTaskGroupTaskNtf.append(taskGroupTaskUpdateDto.getTaskName());
                            oneSignalTaskGroupTaskNtf.append(OneSignalMessages.PREVIOUS);
                            oneSignalTaskGroupTaskNtf.append(taskGroupTask.getTaskName());
                            sendOneSignalNotification(oneSignalTaskGroupTaskNtf.toString(), device.getSubscriptionId());
                            break;
//                        case NOTES:
//                            bodyText.append(SlackMessages.MODIFIED_NOTES_ICON);
//                            bodyText.append(taskGroupTaskUpdateDto.getTaskNotes());
//                            bodyText.append(SlackMessages.PREVIOUS_NOTES_ICON);
//                            if (taskGroupTask.getTaskNote() == null || taskGroupTask.getTaskNote().isEmpty())
//                                bodyText.append("<No Previous Task Note Content>");
//                            else
//                                bodyText.append(taskGroupTask.getTaskNote());
//                            setNotificationThumbnail(body, SlackMessages.TASKGROUP_TASK_NOTE_THUMBNAIL_TEXT, SlackMessages.UPDATE_TASK_NOTE_THUMBNAIL);
//                            break;
//                        case DUE_DATE:
//                            bodyText.append(SlackMessages.MODIFIED_DUE_DATE_ICON);
//                            bodyText.append(getDate(taskGroupTaskUpdateDto.getTaskDueDate()));
//                            bodyText.append(SlackMessages.PREVIOUS_DUE_DATE_ICON);
//                            if (taskGroupTask.getTaskDueDateAt() == null)
//                                bodyText.append("No Previous Due Date");
//                            else
//                                bodyText.append(getDate(taskGroupTask.getTaskDueDateAt()));
//                            setNotificationThumbnail(body, SlackMessages.TASKGROUP_TASK_DATE_THUMBNAIL_TEXT, SlackMessages.UPDATE_TASK_DUE_DATE_THUMBNAIL);
//                            break;
//                        case STATUS:
//                            bodyText.append(SlackMessages.TRANSITION_ICON);
//                            bodyText.append(taskGroupTask.getTaskStatus());
//                            bodyText.append(SlackMessages.ARROW_ICON);
//                            bodyText.append(taskGroupTaskUpdateDto.getTaskStatus());
//                            setNotificationThumbnail(body, SlackMessages.TASKGROUP_TASK_TRANSITION_THUMBNAIL_TEXT, SlackMessages.TRANSITION_THUMBNAIL);
//                            break;
                        default:
                            return;
                    }
                }
            }
            if (user.getUserSlackId() != null && user.getNotification()) {
                JSONObject payload = new JSONObject();
                payload.put(CHANNEL, user.getUserSlackId());
                payload.put(TEXT, SlackMessages.TASK_MODIFICATION_TITLE);
                List<SlackBlock> blocks = new ArrayList<>();

                SlackBlock headerBlock = addHeaderBlock(user.getUserSlackId(), SlackMessages.TASKGROUP_TASK_MODIFICATION_GREETING);
                headerBlock.setAccessory(null);
                blocks.add(headerBlock);
                blocks.add(addDivider());

                SlackBlock body = new SlackBlock();
                body.setType(SECTION);
                body.getText().setType(MARK_DOWN);
                StringBuilder bodyText = new StringBuilder();
                bodyText.append(SlackMessages.TASKGROUP_TASK_ICON);
                bodyText.append(getTaskGroupTaskUrl(taskGroupTask));
                body.getText().setText(bodyText.toString());
                bodyText.append(SlackMessages.TASKGROUP_ICON);
                bodyText.append(getTaskGroupUrl(taskGroup));
                switch (type) {
                    case NAME:
                        bodyText.append(SlackMessages.MODIFIED_NAME_ICON);
                        bodyText.append(taskGroupTaskUpdateDto.getTaskName());
                        bodyText.append(SlackMessages.PREVIOUS_NAME_ICON);
                        bodyText.append(taskGroupTask.getTaskName());
                        setNotificationThumbnail(body, SlackMessages.TASKGROUP_TASK_NAME_THUMBNAIL_TEXT, SlackMessages.UPDATE_TASK_NAME_THUMBNAIL);
                        break;
                    case NOTES:
                        bodyText.append(SlackMessages.MODIFIED_NOTES_ICON);
                        bodyText.append(taskGroupTaskUpdateDto.getTaskNotes());
                        bodyText.append(SlackMessages.PREVIOUS_NOTES_ICON);
                        if (taskGroupTask.getTaskNote() == null || taskGroupTask.getTaskNote().isEmpty())
                            bodyText.append("<No Previous Task Note Content>");
                        else
                            bodyText.append(taskGroupTask.getTaskNote());
                        setNotificationThumbnail(body, SlackMessages.TASKGROUP_TASK_NOTE_THUMBNAIL_TEXT, SlackMessages.UPDATE_TASK_NOTE_THUMBNAIL);
                        break;
                    case DUE_DATE:
                        bodyText.append(SlackMessages.MODIFIED_DUE_DATE_ICON);
                        bodyText.append(getDate(taskGroupTaskUpdateDto.getTaskDueDate()));
                        bodyText.append(SlackMessages.PREVIOUS_DUE_DATE_ICON);
                        if (taskGroupTask.getTaskDueDateAt() == null)
                            bodyText.append("No Previous Due Date");
                        else
                            bodyText.append(getDate(taskGroupTask.getTaskDueDateAt()));
                        setNotificationThumbnail(body, SlackMessages.TASKGROUP_TASK_DATE_THUMBNAIL_TEXT, SlackMessages.UPDATE_TASK_DUE_DATE_THUMBNAIL);
                        break;
                    case STATUS:
                        bodyText.append(SlackMessages.TRANSITION_ICON);
                        bodyText.append(taskGroupTask.getTaskStatus());
                        bodyText.append(SlackMessages.ARROW_ICON);
                        bodyText.append(taskGroupTaskUpdateDto.getTaskStatus());
                        setNotificationThumbnail(body, SlackMessages.TASKGROUP_TASK_TRANSITION_THUMBNAIL_TEXT, SlackMessages.TRANSITION_THUMBNAIL);
                        break;
                    default:
                        return;
                }
                bodyText.append(SlackMessages.MODIFIED_BY_ICON);
                if (editor.getUserSlackId() != null) {
                    bodyText.append(getMentionedName(editor.getUserSlackId()));
                } else {
                    bodyText.append(editor.getFirstName());
                    bodyText.append(" ");
                    bodyText.append(editor.getLastName());
                }
                body.getText().setText(bodyText.toString());

                blocks.add(body);

                if (type.equals(STATUS))
                    blocks.add(getTaskGroupTaskFooter(taskGroupTaskUpdateDto.getTaskStatus().toString()));
                else
                    blocks.add(getTaskGroupTaskFooter(taskGroupTask.getTaskStatus().toString()));
                blocks.add(addDivider());

                payload.put(BLOCKS, blocks);
                StringBuilder url = new StringBuilder();
                url.append(ENVConfig.SLACK_BASE_URL);
                url.append("/chat.postMessage");
                logger.info("Slack Message Url {}", url);
                HttpEntity<Object> entity = new HttpEntity<>(payload.toString(), getHttpHeaders());
                Object response = restTemplate.exchange(url.toString(), HttpMethod.POST, entity, String.class);
            }
        }
    }

    @Override
    public void sendTaskGroupTaskFileUploadNotification(String userId, TaskGroupTask taskGroupTask, String taskGroupId, String file, String fileName) {
        User user = userRepository.getUserByUserId(taskGroupTask.getTaskAssignee());
        if (user.getUserSlackId() != null && user.getNotification()){
            TaskGroup taskGroup = taskGroupRepository.getTaskGroupById(taskGroupId);
            JSONObject payload = new JSONObject();
            payload.put(CHANNEL, user.getUserSlackId());
            payload.put(TEXT, SlackMessages.TASK_FILE_UPLOAD_NOTIFICATION_TITLE);
            List<SlackBlock> blocks = new ArrayList<>();

            SlackBlock headerBlock = addHeaderBlock(user.getUserSlackId(), SlackMessages.TASK_FILE_UPLOAD_GREETING);
            headerBlock.setAccessory(null);
            blocks.add(headerBlock);

            blocks.add(addDivider());

            SlackBlock body = new SlackBlock();
            body.setType(SECTION);
            body.getText().setType(MARK_DOWN);
            StringBuilder bodyText = new StringBuilder();
            bodyText.append(SlackMessages.TASKGROUP_TASK_ICON);
            bodyText.append(getTaskGroupTaskUrl(taskGroupTask));
            bodyText.append(SlackMessages.TASKGROUP_ICON);
            bodyText.append(getTaskGroupUrl(taskGroup));
            bodyText.append(SlackMessages.UPLOADED_BY_ICON);
            bodyText.append(getMentionedName(user.getUserSlackId()));
            body.getText().setText(bodyText.toString());
            setNotificationThumbnail(body, SlackMessages.TASKGROUP_TASK_FILE_TEXT, SlackMessages.FILE_UPLOAD_THUMBNAIL);
            blocks.add(body);

            SlackBlock fileUpload = new SlackBlock();
            fileUpload.setType(SECTION);
            fileUpload.getText().setType(MARK_DOWN);
            StringBuilder fileText = new StringBuilder();
            fileText.append(SlackMessages.UPLOADED_FILE_ICON);
            fileText.append("*<");
            fileText.append(file);
            fileText.append("|");
            fileText.append(fileName);
            fileText.append(">*");
            fileUpload.getText().setText(fileText.toString());
            fileUpload.setAccessory(null);
            blocks.add(fileUpload);
            blocks.add(getFooter(taskGroupTask.getTaskStatus().toString()));
            blocks.add(addDivider());
            payload.put(BLOCKS,blocks);
            StringBuilder url = new StringBuilder();
            url.append(ENVConfig.SLACK_BASE_URL);
            url.append("/chat.postMessage");
            logger.info("Slack Message Url {}", url);
            HttpEntity<Object> entity = new HttpEntity<>(payload.toString(), getHttpHeaders());
            Object response = restTemplate.exchange(url.toString() , HttpMethod.POST, entity, String.class);
        }
    }

    @Override
    public void sendTaskGroupTaskDeleteNotification(TaskGroupTask task, String deletedBy) {
        User user = userRepository.getUserByUserId(task.getTaskAssignee());
        if (user.getUserSlackId() != null && user.getNotification()){
            TaskGroup taskGroup = taskGroupRepository.getTaskGroupById(task.getTaskGroupId());
            User remover = userRepository.getUserByUserId(deletedBy);
            JSONObject payload = new JSONObject();
            payload.put(CHANNEL, user.getUserSlackId());
            payload.put(TEXT, SlackMessages.TASKGROUP_TASK_DELETION_TITLE);
            List<SlackBlock> blocks = new ArrayList<>();

            SlackBlock headerBlock = addHeaderBlock(user.getUserSlackId(), SlackMessages.TASKGROUP_TASK_DELETION_GREETING);
            headerBlock.setAccessory(null);
            headerBlock.setElements(null);

            blocks.add(headerBlock);
            blocks.add(addDivider());

            SlackBlock body = new SlackBlock();
            body.setType(SECTION);
            body.getText().setType(MARK_DOWN);
            StringBuilder bodyText = new StringBuilder();
            bodyText.append(SlackMessages.TASKGROUP_TASK_ICON);
            bodyText.append(getTaskGroupTaskUrl(task));
            bodyText.append(SlackMessages.TASKGROUP_ICON);
            bodyText.append(getTaskGroupUrl(taskGroup));
            bodyText.append(SlackMessages.DELETED_BY_ICON);
            if (remover.getUserSlackId()!= null) {
                bodyText.append(getMentionedName(remover.getUserSlackId()));
            } else {
                bodyText.append(remover.getFirstName());
                bodyText.append(" ");
                bodyText.append(remover.getLastName());
            }
            body.getText().setText(bodyText.toString());
            setNotificationThumbnail(body,SlackMessages.TASK_DELETE_THUMBNAIL_TEXT, SlackMessages.DELETED_BY_THUMBNAIL);
            blocks.add(body);
            blocks.add(addDivider());

            payload.put(BLOCKS,blocks);
            StringBuilder url = new StringBuilder();
            url.append(ENVConfig.SLACK_BASE_URL);
            url.append("/chat.postMessage");
            logger.info("Slack Message Url {}", url);
            HttpEntity<Object> entity = new HttpEntity<>(payload.toString(), getHttpHeaders());
            Object response = restTemplate.exchange(url.toString() , HttpMethod.POST, entity, String.class);
        }
    }

    private SlackBlock addDivider(){
        SlackBlock divider = new SlackBlock();
        divider.setType(DIVIDER);
        divider.setText(null);
        divider.setAccessory(null);
        return divider;
    }

    private SlackBlock addHeaderBlock(String recipientSlackId, String greetingText){
        SlackBlock headerBlock = new SlackBlock();
        headerBlock.setType(SECTION);
        headerBlock.getText().setType(MARK_DOWN);
        StringBuilder greeting = new StringBuilder();
        greeting.append(getWelcomeAddressing(recipientSlackId));
        greeting.append(greetingText);
        headerBlock.getText().setText(greeting.toString());

        return  headerBlock;
    }

    private void setNotificationThumbnail(SlackBlock body, String altText, String thumbnail){
        body.getAccessory().setType("image");
        body.getAccessory().setImage_url(thumbnail);
        body.getAccessory().setAlt_text(altText);
    }

    @Override
    public void sendSubTaskCreateNotification(String senderId, SubTask subTask, ProjectUserResponseDto projectUser, Task task) {
        User recipient = userRepository.getUserByUserId(task.getTaskAssignee());
        if (recipient.getUserSlackId() != null && recipient.getNotification()){
            User sender = userRepository.getUserByUserId(senderId);
            JSONObject payload = new JSONObject();
            payload.put(CHANNEL, recipient.getUserSlackId());
            payload.put(TEXT, SlackMessages.SUB_TASK_CREATION_TITLE);
            List<SlackBlock> blocks = new ArrayList<>();
            logger.info("slack id {}", recipient.getUserSlackId());
            SlackBlock headerBlock = new SlackBlock();
            headerBlock.setType(SECTION);
            headerBlock.getText().setType(MARK_DOWN);
            StringBuilder  welcomeAddressing = new StringBuilder();
            welcomeAddressing.append(getWelcomeAddressing(recipient.getUserSlackId()));
            welcomeAddressing.append(SlackMessages.SUB_TASK_ASSIGNMENT_GREETING);
            headerBlock.getText().setText(welcomeAddressing.toString());
            headerBlock.setAccessory(null);
            blocks.add(headerBlock);

            SlackBlock divider = new SlackBlock();
            divider.setType(DIVIDER);
            divider.setText(null);
            divider.setAccessory(null);
            blocks.add(divider);

            SlackBlock body = new SlackBlock();
            body.setType(SECTION);
            body.getText().setType(MARK_DOWN);
            StringBuilder bodyText = new StringBuilder();
            bodyText.append(SlackMessages.SUB_TASK_ICON);
            bodyText.append(subTask.getSubtaskName());
            bodyText.append("\n");
            bodyText.append(SlackMessages.TASK_ICON);
            bodyText.append(task.getTaskName());
            bodyText.append(SlackMessages.PROJECT_ICON);
            bodyText.append(projectUser.getProjectName());
            bodyText.append(SlackMessages.CREATED_BY_ICON);
            if (sender.getUserSlackId()!= null) {
                bodyText.append(getMentionedName(sender.getUserSlackId()));
            } else {
                bodyText.append(sender.getFirstName());
                bodyText.append(" ");
                bodyText.append(sender.getLastName());
            }

            body.getText().setText(bodyText.toString());
            body.getAccessory().setType("image");
            body.getAccessory().setImage_url(SlackMessages.SUBTASK_CREATE_THUMBNAIL);
            body.getAccessory().setAlt_text("File Upload Thumbnail");
            blocks.add(body);
            blocks.add(getFooter(task.getTaskStatus().toString()));
            blocks.add(divider);

            payload.put(BLOCKS,blocks);
            StringBuilder url = new StringBuilder();
            url.append(ENVConfig.SLACK_BASE_URL);
            url.append("/chat.postMessage");
            logger.info("Slack Message Url {}", url);
            HttpEntity<Object> entity = new HttpEntity<>(payload.toString(), getHttpHeaders());
            Object response = restTemplate.exchange(url.toString() , HttpMethod.POST, entity, String.class);
        }
    }

    @Override
    public void sendSubTaskUpdateNotification(String senderId, Task task, SubTask subTask, SubTask modifiedSubTask, ProjectUserResponseDto projectUser, String type) {
        User recipient = userRepository.getUserByUserId(task.getTaskAssignee());
        if (recipient.getUserSlackId() != null && recipient.getNotification()){
            User sender = userRepository.getUserByUserId(senderId);
            JSONObject payload = new JSONObject();
            payload.put(CHANNEL, recipient.getUserSlackId());
            payload.put(TEXT, SlackMessages.SUB_TASK_MODIFICATION_TITLE);
            List<SlackBlock> blocks = new ArrayList<>();
            logger.info("slack id {}", recipient.getUserSlackId());
            SlackBlock headerBlock = new SlackBlock();
            headerBlock.setType(SECTION);
            headerBlock.getText().setType(MARK_DOWN);
            StringBuilder welcomeAddressing = new StringBuilder();
            welcomeAddressing.append(getWelcomeAddressing(recipient.getUserSlackId()));
            welcomeAddressing.append(SlackMessages.SUB_TASK_MODIFICATION_GREETING);
            headerBlock.getText().setText(welcomeAddressing.toString());
            headerBlock.setAccessory(null);
            blocks.add(headerBlock);

            SlackBlock divider = new SlackBlock();
            divider.setType(DIVIDER);
            divider.setText(null);
            divider.setAccessory(null);
            blocks.add(divider);

            SlackBlock body = new SlackBlock();
            body.setType(SECTION);
            body.getText().setType(MARK_DOWN);
            StringBuilder bodyText = new StringBuilder();
            bodyText.append(SlackMessages.SUB_TASK_ICON);
            bodyText.append(subTask.getSubtaskName());
            bodyText.append("\n");
            bodyText.append(SlackMessages.TASK_ICON);
            bodyText.append(task.getTaskName());
            bodyText.append(SlackMessages.PROJECT_ICON);
            bodyText.append(projectUser.getProjectName());
            switch (type){
                case "name":
                    bodyText.append(SlackMessages.PREVIOUS_CONTENT_ICON);
                    bodyText.append(subTask.getSubtaskName());
                    bodyText.append(SlackMessages.MODIFIED_CONTENT_ICON);
                    bodyText.append(modifiedSubTask.getSubtaskName());
                    bodyText.append(SlackMessages.MODIFIED_BY_ICON);

                    body.getAccessory().setType("image");
                    body.getAccessory().setImage_url(SlackMessages.SUBTASK_MODIFICATION_THUMBNAIL);
                    body.getAccessory().setAlt_text("Subtask Name Modify Thumbnail");
                    break;
                case "status":
                    bodyText.append(SlackMessages.TRANSITION_ICON);
                    if (subTask.isSubtaskStatus()){
                        bodyText.append("Open");
                        bodyText.append(SlackMessages.ARROW_ICON);
                        bodyText.append("Closed");
                        bodyText.append("\n");
                        bodyText.append(SlackMessages.TRANSITIONED_BY_ICON);
                        body.getText().setText(bodyText.toString());
                    } else {
                        bodyText.append("Closed");
                        bodyText.append(SlackMessages.ARROW_ICON);
                        bodyText.append("Open");
                        bodyText.append("\n");
                        bodyText.append(SlackMessages.MODIFIED_BY_ICON);
                        body.getText().setText(bodyText.toString());
                    }
                    body.getAccessory().setType("image");
                    body.getAccessory().setImage_url(SlackMessages.SUBTASK_TRANSITION_THUMBNAIL);
                    body.getAccessory().setAlt_text("Subtask Transition Thumbnail");
            }

            if (sender.getUserSlackId()!= null) {
                bodyText.append(getMentionedName(sender.getUserSlackId()));
            } else {
                bodyText.append(sender.getFirstName());
                bodyText.append(" ");
                bodyText.append(sender.getLastName());
            }
            body.getText().setText(bodyText.toString());

            blocks.add(body);
            blocks.add(getFooter(task.getTaskStatus().toString()));
            blocks.add(divider);

            payload.put(BLOCKS,blocks);
            StringBuilder url = new StringBuilder();
            url.append(ENVConfig.SLACK_BASE_URL);
            url.append("/chat.postMessage");
            logger.info("Slack Message Url {}", url);
            HttpEntity<Object> entity = new HttpEntity<>(payload.toString(), getHttpHeaders());
            Object response = restTemplate.exchange(url.toString() , HttpMethod.POST, entity, String.class);
        }
    }

    @Override
    public void sendSubTaskFlagNotification(String senderId, Task task, SubTask subTask, ProjectUserResponseDto projectUser) {
        User recipient = userRepository.getUserByUserId(task.getTaskAssignee());
        if (recipient.getUserSlackId() != null && recipient.getNotification()){
            User sender = userRepository.getUserByUserId(senderId);
            JSONObject payload = new JSONObject();
            payload.put(CHANNEL, recipient.getUserSlackId());
            payload.put(TEXT, SlackMessages.SUB_TASK_DELETION_TITLE);
            List<SlackBlock> blocks = new ArrayList<>();
            logger.info("slack id {}", recipient.getUserSlackId());
            SlackBlock headerBlock = new SlackBlock();
            headerBlock.setType(SECTION);
            headerBlock.getText().setType(MARK_DOWN);
            StringBuilder  welcomeAddressing = new StringBuilder();
            welcomeAddressing.append(getWelcomeAddressing(recipient.getUserSlackId()));
            welcomeAddressing.append(SlackMessages.SUB_TASK_DELETION_GREETING);
            headerBlock.getText().setText(welcomeAddressing.toString());
            headerBlock.setAccessory(null);
            blocks.add(headerBlock);

            SlackBlock divider = new SlackBlock();
            divider.setType(DIVIDER);
            divider.setText(null);
            divider.setAccessory(null);
            blocks.add(divider);

            SlackBlock body = new SlackBlock();
            body.setType(SECTION);
            body.getText().setType(MARK_DOWN);
            StringBuilder bodyText = new StringBuilder();
            bodyText.append(SlackMessages.SUB_TASK_ICON);
            bodyText.append(subTask.getSubtaskName());
            bodyText.append("\n");
            bodyText.append(SlackMessages.TASK_ICON);
            bodyText.append(task.getTaskName());
            bodyText.append(SlackMessages.PROJECT_ICON);
            bodyText.append(projectUser.getProjectName());
            bodyText.append(SlackMessages.DELETED_BY_ICON);
            if (sender.getUserSlackId()!= null) {
                bodyText.append(getMentionedName(sender.getUserSlackId()));
            } else {
                bodyText.append(sender.getFirstName());
                bodyText.append(" ");
                bodyText.append(sender.getLastName());
            }

            body.getText().setText(bodyText.toString());
            body.getAccessory().setType("image");
            body.getAccessory().setImage_url(SlackMessages.DELETED_BY_ICON);
            body.getAccessory().setAlt_text("Subtask Delete thumbnail");
            blocks.add(body);
            blocks.add(divider);

            payload.put(BLOCKS,blocks);
            StringBuilder url = new StringBuilder();
            url.append(ENVConfig.SLACK_BASE_URL);
            url.append("/chat.postMessage");
            logger.info("Slack Message Url {}", url);
            HttpEntity<Object> entity = new HttpEntity<>(payload.toString(), getHttpHeaders());
            Object response = restTemplate.exchange(url.toString() , HttpMethod.POST, entity, String.class);
        }
    }


    public void sendOneSignalNotification(String message, String recipientId) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set("Authorization", "Basic " + "NGY0OWQwNTYtM2E2Ny00NmMzLWFiN2QtZjdhZjA2OWMwZTgw");
        httpHeaders.set("Content-Type", "application/json; charset=utf-8");
        String url = "https://onesignal.com/api/v1/notifications";

        JSONObject payload = new JSONObject();
//        List<String> segmants = new ArrayList<>();
//        segmants.add("Subscribed Users");
        List<String> segmants = new ArrayList<>();
        segmants.add(recipientId);
        payload.put("include_player_ids", segmants);
        payload.put("app_id", "fe6df906-c5cf-4c5e-bc1f-21003be4b2d5");
        JSONObject contents = new JSONObject();
        contents.put("en", message);
        payload.put("contents", contents);

        logger.info("URL {}", url);
        HttpEntity<Object> entity = new HttpEntity<>(payload.toString(), httpHeaders);
        logger.info("Payload {}", payload.toString());
        try {
          ResponseEntity<String> exchange = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);
        } catch (Exception e){
            logger.info("OneSignal Exception {}", e.getMessage());
        }

    }

    @Scheduled(initialDelay = 10*1000, fixedRate = 30*60*1000)
    public void taskReminderOnDue() {
        List<TaskAlertDto> taskAlertList = notificationRepository.getTaskAlertList();
        for(TaskAlertDto taskAlert : taskAlertList) {
            if (taskAlert.getTaskDue() != null && taskAlert.getAssigneeSlackId() != null) {
                logger.info("<--------------Start Time for task {}------------->", taskAlert.getTaskName());
                long due = taskAlert.getTaskDue().getTime();
                DateTime duedate = new DateTime(due);
                DateTime now = DateTime.now();
                DateTime nowUTC = new DateTime(now, DateTimeZone.forID("UTC"));
                logger.info("nowUTC {}",nowUTC);
                DateTime nowCol = new DateTime(now, DateTimeZone.forID("Asia/Colombo"));
                logger.info("nowCol {}",nowCol);
                DateTime dueUtc = new DateTime(duedate, DateTimeZone.forID("UTC"));
                logger.info("dueUtc {}",dueUtc);
                DateTime dueCol = new DateTime(duedate, DateTimeZone.forID("Asia/Colombo"));
                logger.info("dueCol {}",dueCol);
                Duration duration = new Duration(nowCol, dueUtc);
                int difference = (int) duration.getStandardMinutes();
                logger.info("difference {}",difference);
                int timeFixDifference = difference - 330;
                logger.info("fix difference {}",timeFixDifference);
                logger.info("<--------------END Time for task {}------------->", taskAlert.getTaskName());
                if (timeFixDifference < 1440 && timeFixDifference > 0){
                    if (timeFixDifference < 60 && !taskAlert.getIsHourly()){
                        notificationRepository.deleteNotification(taskAlert.getTaskId());
                        sendTaskReminder(taskAlert, dueUtc);
                    } else if (!taskAlert.getIsDaily()){
                        //Daily Notification
                        NotificationUpdateDto updateDto = new NotificationUpdateDto();
                        updateDto.setTaskId(taskAlert.getTaskId());
                        updateDto.setIsDaily(true);
                        updateDto.setIsHourly(false);
                        notificationRepository.updateTaskNotification(updateDto);
                        sendTaskReminder(taskAlert, dueUtc);
                    }
                }
            }
        }
        List<TaskGroupTaskAlertDto> taskGroupTaskAlertList = notificationRepository.getTaskGroupTaskAlertList();

        for(TaskGroupTaskAlertDto taskAlert : taskGroupTaskAlertList) {
            if (taskAlert.getTaskDue() != null && taskAlert.getAssigneeSlackId() != null) {
                logger.info("<--------------Start Time for task {}------------->", taskAlert.getTaskName());
                long due = taskAlert.getTaskDue().getTime();
                DateTime duedate = new DateTime(due);
                DateTime now = DateTime.now();
                DateTime nowUTC = new DateTime(now, DateTimeZone.forID("UTC"));
                logger.info("nowUTC {}",nowUTC);
                DateTime nowCol = new DateTime(now, DateTimeZone.forID("Asia/Colombo"));
                logger.info("nowCol {}",nowCol);
                DateTime dueUtc = new DateTime(duedate, DateTimeZone.forID("UTC"));
                logger.info("dueUtc {}",dueUtc);
                DateTime dueCol = new DateTime(duedate, DateTimeZone.forID("Asia/Colombo"));
                logger.info("dueCol {}",dueCol);
                Duration duration = new Duration(nowCol, dueUtc);
                int difference = (int) duration.getStandardMinutes();
                logger.info("difference {}",difference);
                int timeFixDifference = difference - 330;
                logger.info("fix difference {}",timeFixDifference);
                logger.info("<--------------END Time for task {}------------->", taskAlert.getTaskName());
                if (timeFixDifference < 1440 && timeFixDifference > 0){
                    if (timeFixDifference < 60 && !taskAlert.getIsHourly()){
                        //Hourly Notification
                        notificationRepository.deleteNotification(taskAlert.getTaskId());
                        sendTaskGroupTaskReminder(taskAlert, dueUtc);
                    } else if (!taskAlert.getIsDaily()){
                        //Daily Notification
                        NotificationUpdateDto updateDto = new NotificationUpdateDto();
                        updateDto.setTaskId(taskAlert.getTaskId());
                        updateDto.setIsDaily(true);
                        updateDto.setIsHourly(false);
                        notificationRepository.updateTaskNotification(updateDto);
                        sendTaskGroupTaskReminder(taskAlert, dueUtc);
                    }
                }
            }
        }

        List<PersonalTaskAlertDto> taskPersonalTaskAlertList = notificationRepository.getPersonalTaskAlertList();

        for (PersonalTaskAlertDto taskAlert: taskPersonalTaskAlertList){
            if (taskAlert.getTaskDue() != null && taskAlert.getAssigneeSlackId() != null) {
                logger.info("<--------------Start Time for task {}------------->", taskAlert.getTaskName());
                long due = taskAlert.getTaskDue().getTime();
                DateTime duedate = new DateTime(due);
                DateTime now = DateTime.now();
                DateTime nowUTC = new DateTime(now, DateTimeZone.forID("UTC"));
                logger.info("nowUTC {}",nowUTC);
                DateTime nowCol = new DateTime(now, DateTimeZone.forID("Asia/Colombo"));
                logger.info("nowCol {}",nowCol);
                DateTime dueUtc = new DateTime(duedate, DateTimeZone.forID("UTC"));
                logger.info("dueUtc {}",dueUtc);
                DateTime dueCol = new DateTime(duedate, DateTimeZone.forID("Asia/Colombo"));
                logger.info("dueCol {}",dueCol);
                Duration duration = new Duration(nowCol, dueUtc);
                int difference = (int) duration.getStandardMinutes();
                logger.info("difference {}",difference);
                int timeFixDifference = difference - 330;
                logger.info("fix difference {}",timeFixDifference);
                logger.info("<--------------END Time for task {}------------->", taskAlert.getTaskName());
                if (timeFixDifference < 1440 && timeFixDifference > 0){
                    if (timeFixDifference < 60 && !taskAlert.getIsHourly()){
                        //Hourly Notification
                        notificationRepository.deleteNotification(taskAlert.getTaskId());
                        sendPersonalTaskReminder(taskAlert, dueUtc);
                    } else if (!taskAlert.getIsDaily()){
                        //Daily Notification
                        NotificationUpdateDto updateDto = new NotificationUpdateDto();
                        updateDto.setTaskId(taskAlert.getTaskId());
                        updateDto.setIsDaily(true);
                        updateDto.setIsHourly(false);
                        notificationRepository.updateTaskNotification(updateDto);
                        sendPersonalTaskReminder(taskAlert, dueUtc);
                    }
                }
            }
        }

    }

    private void sendTaskReminder(TaskAlertDto taskAlert, DateTime dueUtc){
        Project project = projectRepository.getProjectById(taskAlert.getProjectId());
        Task task = taskRepository.getProjectTask(taskAlert.getTaskId());
        JSONObject payload = new JSONObject();
        payload.put(CHANNEL, taskAlert.getAssigneeSlackId());
        payload.put(TEXT, SlackMessages.TASK_REMINDER_TITLE);
        List<SlackBlock> blocks = new ArrayList<>();

        SlackBlock headerBlock = addHeaderBlock(taskAlert.getAssigneeSlackId(), SlackMessages.TASK_REMINDER_GREETING);
        headerBlock.setAccessory(null);
        blocks.add(headerBlock);

        blocks.add(addDivider());

        SlackBlock body = new SlackBlock();
        body.setType(SECTION);
        body.getText().setType(MARK_DOWN);
        StringBuilder bodyText = new StringBuilder();
        bodyText.append(SlackMessages.TASK_ICON);
        bodyText.append(getTaskUrl(task));
        bodyText.append(SlackMessages.PROJECT_ICON);
        bodyText.append(getProjectUrl(project));
        bodyText.append(SlackMessages.DUE_DATE_ICON);
        if (task.getTaskDueDateAt() != null){
            bodyText.append(getDueDate(dueUtc));
        } else {
            bodyText.append("No Due Date Assigned");
        }
        body.getText().setText(bodyText.toString());
        setNotificationThumbnail(body, SlackMessages.REMINDER_THUMBNAIL_TEXT, SlackMessages.CALENDER_THUMBNAIL);
        blocks.add(body);
        blocks.add(getFooter(task.getTaskStatus().toString()));
        blocks.add(addDivider());

        payload.put(BLOCKS,blocks);
        StringBuilder url = new StringBuilder();
        url.append(ENVConfig.SLACK_BASE_URL);
        url.append("/chat.postMessage");
        logger.info("Slack Message Url {}", url);
        HttpEntity<Object> entity = new HttpEntity<>(payload.toString(), getHttpHeaders());
        Object response = restTemplate.exchange(url.toString() , HttpMethod.POST, entity, String.class);
    }

    private void sendTaskGroupTaskReminder(TaskGroupTaskAlertDto taskAlert, DateTime dueUtc){
        TaskGroup taskGroup = taskGroupRepository.getTaskGroupById(taskAlert.getTaskGroupId());
        TaskGroupTask task = taskGroupTaskRepository.getTaskByTaskGroupId(taskAlert.getTaskGroupId(), taskAlert.getTaskId());
        JSONObject payload = new JSONObject();
        payload.put(CHANNEL, taskAlert.getAssigneeSlackId());
        payload.put(TEXT, SlackMessages.TASKGROUP_TASK_REMINDER_TITLE);
        List<SlackBlock> blocks = new ArrayList<>();

        SlackBlock headerBlock = addHeaderBlock(taskAlert.getAssigneeSlackId(), SlackMessages.TASKGROUP_TASK_REMINDER_GREETING);
        headerBlock.setAccessory(null);
        blocks.add(headerBlock);

        blocks.add(addDivider());

        SlackBlock body = new SlackBlock();
        body.setType(SECTION);
        body.getText().setType(MARK_DOWN);
        StringBuilder bodyText = new StringBuilder();
        bodyText.append(SlackMessages.TASKGROUP_TASK_ICON);
        bodyText.append(getTaskGroupTaskUrl(task));
        bodyText.append(SlackMessages.TASKGROUP_ICON);
        bodyText.append(getTaskGroupUrl(taskGroup));
        bodyText.append(SlackMessages.DUE_DATE_ICON);
        if (task.getTaskDueDateAt() != null){
            bodyText.append(getDueDate(dueUtc));
        } else {
            bodyText.append("No Due Date Assigned");
        }
        body.getText().setText(bodyText.toString());
        setNotificationThumbnail(body, SlackMessages.REMINDER_THUMBNAIL_TEXT, SlackMessages.CALENDER_THUMBNAIL);
        blocks.add(body);
        blocks.add(getFooter(task.getTaskStatus().toString()));
        blocks.add(addDivider());

        payload.put(BLOCKS,blocks);
        StringBuilder url = new StringBuilder();
        url.append(ENVConfig.SLACK_BASE_URL);
        url.append("/chat.postMessage");
        logger.info("Slack Message Url {}", url);
        HttpEntity<Object> entity = new HttpEntity<>(payload.toString(), getHttpHeaders());
        Object response = restTemplate.exchange(url.toString() , HttpMethod.POST, entity, String.class);
    }

    private void sendPersonalTaskReminder(PersonalTaskAlertDto taskAlert, DateTime dueUtc){
        PersonalTask task = personalTaskRepository.getPersonalTaskByUserId(taskAlert.getAssigneeId(), taskAlert.getTaskId());
        JSONObject payload = new JSONObject();
        payload.put(CHANNEL, taskAlert.getAssigneeSlackId());
        payload.put(TEXT, SlackMessages.PERSONAL_TASK_REMINDER_TITLE);
        List<SlackBlock> blocks = new ArrayList<>();

        SlackBlock headerBlock = addHeaderBlock(taskAlert.getAssigneeSlackId(), SlackMessages.PERSONAL_TASK_REMINDER_GREETING);
        headerBlock.setAccessory(null);
        blocks.add(headerBlock);

        blocks.add(addDivider());

        SlackBlock body = new SlackBlock();
        body.setType(SECTION);
        body.getText().setType(MARK_DOWN);
        StringBuilder bodyText = new StringBuilder();
        bodyText.append(SlackMessages.PERSONAL_TASK_ICON);
        bodyText.append(getPersonalTaskUrl(task));
//        bodyText.append(SlackMessages.TASKGROUP_ICON);
//        bodyText.append(getTaskGroupUrl(taskGroup));
        bodyText.append(SlackMessages.DUE_DATE_ICON);
        if (task.getTaskDueDateAt() != null){
            bodyText.append(getDueDate(dueUtc));
        } else {
            bodyText.append("No Due Date Assigned");
        }
        body.getText().setText(bodyText.toString());
        setNotificationThumbnail(body, SlackMessages.REMINDER_THUMBNAIL_TEXT, SlackMessages.CALENDER_THUMBNAIL);
        blocks.add(body);
        blocks.add(getFooter(task.getTaskStatus().toString()));
        blocks.add(addDivider());

        payload.put(BLOCKS,blocks);
        StringBuilder url = new StringBuilder();
        url.append(ENVConfig.SLACK_BASE_URL);
        url.append("/chat.postMessage");
        logger.info("Slack Message Url {}", url);
        HttpEntity<Object> entity = new HttpEntity<>(payload.toString(), getHttpHeaders());
        Object response = restTemplate.exchange(url.toString() , HttpMethod.POST, entity, String.class);
    }

    private String getDueDate(DateTime dueUtc){
        DateTimeFormatter fmt = DateTimeFormat.forPattern("dd MMMM, yyyy hh:mma");
        String dueFormatted = fmt.print(dueUtc);

//        int year = dueUtc.getYear();
//        int month = dueUtc.getMonthOfYear();
//        dueUtc.getDayOfMonth();
//        dueUtc.getHourOfDay();
//        dueUtc.getMinuteOfHour();

        return dueFormatted;

    }

    private String getDate(Timestamp date){
        DateTime dateUTC = new DateTime(date, DateTimeZone.forID("UTC"));
        DateTimeFormatter fmt = DateTimeFormat.forPattern("dd MMMM, yyyy hh:mma");
        String dueFormatted = fmt.print(dateUTC);

        return dueFormatted;

    }

    private StringBuilder getWelcomeAddressing(String slackId){
        StringBuilder  welcomeAddressing = new StringBuilder();
        welcomeAddressing.append(SlackMessages.ADDRESS_GREETING);
        welcomeAddressing.append("<@");
        welcomeAddressing.append(slackId);
        welcomeAddressing.append(">!");

        return welcomeAddressing;
    }

    private String getMentionedName(String slackId){
        StringBuilder  mentionName = new StringBuilder();
        mentionName.append("<@");
        mentionName.append(slackId);
        mentionName.append("> ");
        return mentionName.toString();
    }

    private SlackBlock getFooter(String taskStatus){
        SlackBlock footer = new SlackBlock();
        footer.setType("context");
        footer.setText(null);
        footer.setAccessory(null);
        List<SlackElement> elements = new ArrayList<>();
        SlackElement img = new SlackElement();
        img.setType("image");
        img.setImage_url("https://api.slack.com/img/blocks/bkb_template_images/notificationsWarningIcon.png");
        img.setAlt_text("task status");
        elements.add(img);
        SlackElement element = new SlackElement();
        element.setType(MARK_DOWN);
        StringBuilder status = new StringBuilder();
        status.append("Task Status: *");
        status.append(taskStatus);
        status.append("*");
        element.setText(status.toString());
        element.setAlt_text(null);
        elements.add(element);
        footer.setElements(elements);

        return  footer;
    }

    private SlackBlock getTaskGroupTaskFooter(String taskStatus){
        SlackBlock footer = new SlackBlock();
        footer.setType("context");
        footer.setText(null);
        footer.setAccessory(null);
        List<SlackElement> elements = new ArrayList<>();
        SlackElement img = new SlackElement();
        img.setType("image");
        img.setImage_url("https://api.slack.com/img/blocks/bkb_template_images/notificationsWarningIcon.png");
        img.setAlt_text("task status");
        elements.add(img);
        SlackElement element = new SlackElement();
        element.setType(MARK_DOWN);
        StringBuilder status = new StringBuilder();
        status.append("Task Status: *");
        status.append(taskStatus);
        status.append("*");
        element.setText(status.toString());
        element.setAlt_text(null);
        elements.add(element);
        footer.setElements(elements);

        return  footer;
    }

    private String getTaskUrl(Task task){
        StringBuilder taskUrl = new StringBuilder();
        taskUrl.append("*<");
        taskUrl.append(SlackMessages.FRONTEND_URL);
        taskUrl.append("/task/");
        taskUrl.append(task.getTaskId());
        taskUrl.append("/?project=");
        taskUrl.append(task.getProjectId());
        taskUrl.append("|");
        taskUrl.append(task.getTaskName());
        taskUrl.append(">*");

        return taskUrl.toString();
    }

    private String getTaskGroupTaskUrl(TaskGroupTask taskGroupTask){
        StringBuilder taskGroupTaskUrl = new StringBuilder();
        taskGroupTaskUrl.append("*<");
        taskGroupTaskUrl.append(SlackMessages.FRONTEND_URL);
        taskGroupTaskUrl.append("/tasks/tasks");
        taskGroupTaskUrl.append("|");
        taskGroupTaskUrl.append(taskGroupTask.getTaskName());
        taskGroupTaskUrl.append(">*");

        return taskGroupTaskUrl.toString();
    }

    private String getPersonalTaskUrl(PersonalTask personalTask){
        StringBuilder personalTaskUrl = new StringBuilder();
        personalTaskUrl.append("*<");
        personalTaskUrl.append(SlackMessages.FRONTEND_URL);
        personalTaskUrl.append("/tasks/tasks");
        personalTaskUrl.append("|");
        personalTaskUrl.append(personalTask.getTaskName());
        personalTaskUrl.append(">*");

        return personalTaskUrl.toString();
    }

    private String getTaskGroupUrl(TaskGroup taskGroup){
        StringBuilder taskGroupUrl = new StringBuilder();
        taskGroupUrl.append("*<");
        taskGroupUrl.append(SlackMessages.FRONTEND_URL);
        taskGroupUrl.append("/tasks/tasks");
        //taskUrl.append(project.getProjectId());
        taskGroupUrl.append("|");
        taskGroupUrl.append(taskGroup.getTaskGroupName());
        taskGroupUrl.append(">*");

        return taskGroupUrl.toString();
    }

    private String getProjectUrl(Project project){
        StringBuilder taskUrl = new StringBuilder();
        taskUrl.append("*<");
        taskUrl.append(SlackMessages.FRONTEND_URL);
        taskUrl.append("/projects/");
        taskUrl.append(project.getProjectId());
        taskUrl.append("|");
        taskUrl.append(project.getProjectName());
        taskUrl.append(">*");

        return taskUrl.toString();
    }

}
