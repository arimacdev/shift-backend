package com.arimac.backend.pmtool.projectmanagementtool.Service.Impl;

import com.arimac.backend.pmtool.projectmanagementtool.Response.Response;
import com.arimac.backend.pmtool.projectmanagementtool.Service.NotificationService;
import com.arimac.backend.pmtool.projectmanagementtool.dtos.*;
import com.arimac.backend.pmtool.projectmanagementtool.dtos.Slack.SlackBlock;
import com.arimac.backend.pmtool.projectmanagementtool.dtos.Slack.SlackElement;
import com.arimac.backend.pmtool.projectmanagementtool.enumz.ResponseMessage;
import com.arimac.backend.pmtool.projectmanagementtool.exception.ErrorMessage;
import com.arimac.backend.pmtool.projectmanagementtool.model.Project;
import com.arimac.backend.pmtool.projectmanagementtool.model.SubTask;
import com.arimac.backend.pmtool.projectmanagementtool.model.Task;
import com.arimac.backend.pmtool.projectmanagementtool.model.User;
import com.arimac.backend.pmtool.projectmanagementtool.repository.NotificationRepository;
import com.arimac.backend.pmtool.projectmanagementtool.repository.ProjectRepository;
import com.arimac.backend.pmtool.projectmanagementtool.repository.TaskRepository;
import com.arimac.backend.pmtool.projectmanagementtool.repository.UserRepository;
import com.arimac.backend.pmtool.projectmanagementtool.utils.ENVConfig;
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
import java.util.concurrent.ExecutorService;

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

    private final NotificationRepository notificationRepository;
    private final UserRepository userRepository;
    private final TaskRepository taskRepository;
    private final ProjectRepository projectRepository;

    private final RestTemplate restTemplate;

    public NotificationServiceImpl(NotificationRepository notificationRepository, UserRepository userRepository, TaskRepository taskRepository, ProjectRepository projectRepository, RestTemplate restTemplate) {
        this.notificationRepository = notificationRepository;
        this.userRepository = userRepository;
        this.taskRepository = taskRepository;
        this.projectRepository = projectRepository;
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

    private HttpHeaders getHttpHeaders(){
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set("Authorization", "Bearer " + ENVConfig.SLACK_BOT_TOKEN);
        httpHeaders.set("Content-Type", "application/json");

        return httpHeaders;
    }

    @Override
    public void sendTaskAssignNotification(Task task) {
        User user = userRepository.getUserByUserId(task.getTaskAssignee());
        if (user.getUserSlackId() != null && user.getNotification()){
            Project project = projectRepository.getProjectById(task.getProjectId());
            User sender = userRepository.getUserByUserId(task.getTaskInitiator());
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
            if (sender.getUserSlackId()!= null) {
                bodyText.append(getMentionedName(sender.getUserSlackId()));
            } else {
                bodyText.append(sender.getFirstName());
                bodyText.append(" ");
                bodyText.append(sender.getLastName());
            }
            bodyText.append(SlackMessages.DUE_DATE_ICON);
            if (task.getTaskDueDateAt() != null){
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
    public void sendTaskAssigneeUpdateNotification(Task task, String userId, String newTaskAssignee) {
        User user = userRepository.getUserByUserId(task.getTaskAssignee());
        if (user.getUserSlackId() != null && user.getNotification()){
            User previous = userRepository.getUserByUserId(task.getTaskAssignee());
            User newAssignee = userRepository.getUserByUserId(newTaskAssignee);
            Project project = projectRepository.getProjectById(task.getProjectId());
            User sender = userRepository.getUserByUserId(userId);
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
            if (previous.getUserSlackId()!= null){
                bodyText.append(getMentionedName(previous.getUserSlackId()));
            } else {
                bodyText.append(previous.getFirstName());
                bodyText.append(" ");
                bodyText.append(previous.getLastName());
            }
            bodyText.append(SlackMessages.ARROW_ICON);
            if (newAssignee.getUserSlackId() != null){
                bodyText.append(getMentionedName(newAssignee.getUserSlackId()));
            } else {
                bodyText.append(newAssignee.getFirstName());
                bodyText.append(" ");
                bodyText.append(newAssignee.getLastName());
            }
            bodyText.append(SlackMessages.ASSIGNED_BY_ICON);
            if (sender.getUserSlackId()!= null) {
                bodyText.append(getMentionedName(sender.getUserSlackId()));
            } else {
                bodyText.append(sender.getFirstName());
                bodyText.append(" ");
                bodyText.append(sender.getLastName());
            }
            bodyText.append(SlackMessages.DUE_DATE_ICON);
            if (task.getTaskDueDateAt() != null){
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
    public void sendTaskModificationNotification(Task task, TaskUpdateDto taskUpdateDto, String type, String taskEditor) {
        User user = userRepository.getUserByUserId(task.getTaskAssignee());
        if (user.getUserSlackId() != null && user.getNotification()){
            User editor = userRepository.getUserByUserId(taskEditor);
            Project project = projectRepository.getProjectById(task.getProjectId());
            JSONObject payload = new JSONObject();
            payload.put(CHANNEL, user.getUserSlackId());
            payload.put(TEXT, SlackMessages.TASK_MODIFICATION_TITLE);
            List<SlackBlock> blocks = new ArrayList<>();

            SlackBlock headerBlock = new SlackBlock();
            headerBlock.setType(SECTION);
            headerBlock.getText().setType(MARK_DOWN);
            StringBuilder greeting = new StringBuilder();
            greeting.append(getWelcomeAddressing(user.getUserSlackId()));
            greeting.append(SlackMessages.TASK_MODIFICATION_GREETING);
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
            body.getText().setText(bodyText.toString());
            bodyText.append(SlackMessages.PROJECT_ICON);
            bodyText.append(getProjectUrl(project));
            switch (type){
                case "name":
                    bodyText.append(SlackMessages.MODIFIED_NAME_ICON);
                    bodyText.append(taskUpdateDto.getTaskName());
                    bodyText.append(SlackMessages.PREVIOUS_NAME_ICON);
                    bodyText.append(task.getTaskName());
                    body.getAccessory().setType("image");
                    body.getAccessory().setImage_url(SlackMessages.UPDATE_TASK_NAME_THUMBNAIL);
                    body.getAccessory().setAlt_text("Task Name Modification Thumbnail");
                 break;
                case "notes":
                    bodyText.append(SlackMessages.MODIFIED_NOTES_ICON);
                    bodyText.append(taskUpdateDto.getTaskNotes());
                    bodyText.append(SlackMessages.PREVIOUS_NOTES_ICON);
                    if (task.getTaskNote() == null || task.getTaskNote().isEmpty())
                        bodyText.append("<No Previous Task Note Content>");
                    else
                    bodyText.append(task.getTaskNote());
                    body.getAccessory().setType("image");
                    body.getAccessory().setImage_url(SlackMessages.UPDATE_TASK_NOTE_THUMBNAIL);
                    body.getAccessory().setAlt_text("Task Note Modification Thumbnail");
                 break;
                case "dueDate":
                    bodyText.append(SlackMessages.MODIFIED_DUE_DATE_ICON);
                    bodyText.append(getDate(taskUpdateDto.getTaskDueDate()));
                    bodyText.append(SlackMessages.PREVIOUS_DUE_DATE_ICON);
                    if (task.getTaskDueDateAt() == null)
                        bodyText.append("No Previous Due Date");
                    else
                        bodyText.append(getDate(task.getTaskDueDateAt()));
                    body.getAccessory().setType("image");
                    body.getAccessory().setImage_url(SlackMessages.UPDATE_TASK_DUE_DATE_THUMBNAIL);
                    body.getAccessory().setAlt_text("Task Due Date Modification Thumbnail");
                 break;
                case "status":
                    bodyText.append(SlackMessages.TRANSITION_ICON);
                    bodyText.append(task.getTaskStatus());
                    bodyText.append(SlackMessages.ARROW_ICON);
                    bodyText.append(taskUpdateDto.getTaskStatus());
                    body.getAccessory().setType("image");
                    body.getAccessory().setImage_url(SlackMessages.TRANSITION_THUMBNAIL
                    );
                    body.getAccessory().setAlt_text("Task Transition Thumbnail");
                 break;
            }
            bodyText.append(SlackMessages.MODIFIED_BY_ICON);
            if (editor.getUserSlackId() != null){
                bodyText.append(getMentionedName(editor.getUserSlackId()));
            } else {
                bodyText.append(editor.getFirstName());
                bodyText.append(" ");
                bodyText.append(editor.getLastName());
            }

            body.getText().setText(bodyText.toString());

            blocks.add(body);
            if (type.equals("status"))
                blocks.add(getFooter(taskUpdateDto.getTaskStatus().toString()));
            else
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
    public void sendTaskFileUploadNotification(String userId, String taskId, String file, String fileName) {
        Task task = taskRepository.getProjectTask(taskId);
        User user = userRepository.getUserByUserId(task.getTaskAssignee());
        if (user.getUserSlackId() != null && user.getNotification()){
            Project project = projectRepository.getProjectById(task.getProjectId());
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
            body.getAccessory().setImage_url(SlackMessages.SUBTASK_FLAG_THUMBNAIL);
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

    @Override
    public Object checkSlackNotification() {
        JSONObject payload = new JSONObject();
        payload.put("channel", "UGQ0FGZ5F");
        payload.put("text", "New Task Assignment");
        List<SlackBlock> blocks = new ArrayList<>();

        SlackBlock headerBlock = new SlackBlock();
        headerBlock.setType("section");
        headerBlock.getText().setType("plain_text");
        headerBlock.getText().setText(":wave: Hey There! You have been assigned to the following Task");
        headerBlock.setAccessory(null);
        blocks.add(headerBlock);

        SlackBlock divider = new SlackBlock();
        divider.setType("divider");
        divider.setText(null);
        divider.setAccessory(null);
        blocks.add(divider);

        SlackBlock body = new SlackBlock();
        body.setType("section");
        body.getText().setType("mrkdwn");
        body.getText().setText(":gear: Task: *Notification API development*\n :briefcase: Project: *PM-Tool*\n:speaking_head_in_silhouette: Assigned By: *Naveen Perera* \n:hourglass_flowing_sand: Due Date: *2020/04/06*");
        body.getAccessory().setType("image");
        body.getAccessory().setImage_url("https://api.slack.com/img/blocks/bkb_template_images/notifications.png");
        body.getAccessory().setAlt_text("Calender Thumbnail");
        blocks.add(body);
        blocks.add(divider);

        payload.put("blocks",blocks);
        StringBuilder url = new StringBuilder();
        url.append(ENVConfig.SLACK_BASE_URL);
        url.append("/chat.postMessage");
        logger.info("Slack Message Url {}", url);
        HttpEntity<Object> entity = new HttpEntity<>(payload.toString(), getHttpHeaders());
        Object response = restTemplate.exchange(url.toString() , HttpMethod.POST, entity, String.class);
        return null;
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
//                int mins = (int)duration.getStandardMinutes();
//                Minutes minutes = Minutes.minutesBetween(dt, duedate);
                int difference = (int) duration.getStandardMinutes();
//                logger.info("days {} | {}", minutes,(int) duration.getStandardMinutes());
//                logger.info("days {} | {}", minutes, duration.getStandardMinutes());
//                logger.info("task {} || minutes left {}", taskAlert.getTaskId(),minutes);
                logger.info("difference {}",difference);
                int timeFixDifference = difference - 330;
                logger.info("fix difference {}",timeFixDifference);
                logger.info("<--------------END Time for task {}------------->", taskAlert.getTaskName());
                if(timeFixDifference < 60 && !taskAlert.getIsDaily()){
                    //send notification
                    NotificationUpdateDto updateDto = new NotificationUpdateDto();
                    updateDto.setTaskId(taskAlert.getTaskId());
                    updateDto.setIsDaily(true);
                    updateDto.setIsHourly(true);
                    notificationRepository.updateTaskNotification(updateDto);
                }
                if (timeFixDifference < 1440 && timeFixDifference > 0){
                    NotificationUpdateDto updateDto = new NotificationUpdateDto();
                    updateDto.setTaskId(taskAlert.getTaskId());
                    if (timeFixDifference < 60){
                        //Hourly Notification
                        updateDto.setIsDaily(true);
                        updateDto.setIsHourly(true);
                        notificationRepository.updateTaskNotification(updateDto);
                        sendSlackNotification(taskAlert, dueUtc);
                    } else if (!taskAlert.getIsDaily()){
                        //Daily Notification
                        updateDto.setIsDaily(true);
                        updateDto.setIsHourly(false);
                        notificationRepository.updateTaskNotification(updateDto);
                        sendSlackNotification(taskAlert, dueUtc);
                    }
                }
            }
        }
    }

    private void sendSlackNotification(TaskAlertDto taskAlert, DateTime dueUtc){
        Project project = projectRepository.getProjectById(taskAlert.getProjectId());
        Task task = taskRepository.getProjectTask(taskAlert.getTaskId());
        JSONObject payload = new JSONObject();
        payload.put(CHANNEL, taskAlert.getAssigneeSlackId());
        payload.put(TEXT, SlackMessages.TASK_REMINDER_TITLE);
        List<SlackBlock> blocks = new ArrayList<>();

        SlackBlock headerBlock = new SlackBlock();
        headerBlock.setType(SECTION);
        headerBlock.getText().setType(MARK_DOWN);
        StringBuilder addressing = new StringBuilder();
        addressing.append(getWelcomeAddressing(taskAlert.getAssigneeSlackId()));
        addressing.append(SlackMessages.TASK_REMINDER_GREETING);
        headerBlock.getText().setText(addressing.toString());
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
        bodyText.append(task.getTaskName());
        bodyText.append(SlackMessages.PROJECT_ICON);
        bodyText.append(project.getProjectName());
        bodyText.append(SlackMessages.DUE_DATE_ICON);
        if (task.getTaskDueDateAt() != null){
//            DateTime dueUtc = new DateTime(due, DateTimeZone.forID("UTC"));
            bodyText.append(getDueDate(dueUtc));
        } else {
            bodyText.append("No Due Date Assigned");
        }
        body.getText().setText(bodyText.toString());
        body.getAccessory().setType("image");
        body.getAccessory().setImage_url(SlackMessages.CALENDER_THUMBNAIL);
        body.getAccessory().setAlt_text("Calender Thumbnail");
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
//        try {
//            JSONObject payload = new JSONObject();
//            payload.put("channel", taskAlert.getAssigneeSlackId());
//            StringBuilder message = new StringBuilder();
//            message.append("Your Task: ");
//            message.append(taskAlert.getTaskName());
//            message.append(" of project ");
//            message.append(taskAlert.getProjectName());
//            message.append(" will be due at ");
//            message.append(getDueDate(dueUtc));
//            payload.put("text",message.toString());
//            StringBuilder url = new StringBuilder();
//            url.append(ENVConfig.SLACK_BASE_URL);
//            url.append("/chat.postMessage");
//            logger.info("Slack Message Url {}", url);
//            HttpEntity<Object> entity = new HttpEntity<>(payload.toString(), getHttpHeaders());
//            ResponseEntity<String> exchange = restTemplate.exchange(url.toString() , HttpMethod.POST, entity, String.class);
//        } catch (Exception e){
//            logger.info("Error calling Slack API");
//        }
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
