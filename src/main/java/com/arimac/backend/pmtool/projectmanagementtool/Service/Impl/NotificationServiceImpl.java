package com.arimac.backend.pmtool.projectmanagementtool.Service.Impl;

import com.arimac.backend.pmtool.projectmanagementtool.Response.Response;
import com.arimac.backend.pmtool.projectmanagementtool.Service.NotificationService;
import com.arimac.backend.pmtool.projectmanagementtool.dtos.*;
import com.arimac.backend.pmtool.projectmanagementtool.dtos.Slack.SlackBlock;
import com.arimac.backend.pmtool.projectmanagementtool.enumz.ResponseMessage;
import com.arimac.backend.pmtool.projectmanagementtool.exception.ErrorMessage;
import com.arimac.backend.pmtool.projectmanagementtool.model.Project;
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
            return new ErrorMessage(ResponseMessage.NO_RECORD, HttpStatus.NOT_FOUND);
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
        if (user.getUserSlackId() != null){
            Project project = projectRepository.getProjectById(task.getProjectId());
            User sender = userRepository.getUserByUserId(task.getTaskInitiator());
            JSONObject payload = new JSONObject();
            payload.put(CHANNEL, user.getUserSlackId());
            payload.put(TEXT, SlackMessages.TASK_ASSIGNMENT_TITLE);
            List<SlackBlock> blocks = new ArrayList<>();

            SlackBlock headerBlock = new SlackBlock();
            headerBlock.setType(SECTION);
            headerBlock.getText().setType(PLAIN_TEXT);
            headerBlock.getText().setText(SlackMessages.TASK_ASSIGNMENT_GREETING);
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
            bodyText.append(SlackMessages.ASSIGNED_BY_ICON);
            bodyText.append(sender.getFirstName());
            bodyText.append(" ");
            bodyText.append(sender.getLastName());
            bodyText.append(SlackMessages.DUE_DATE_ICON);
            if (task.getTaskDueDateAt() != null){
                DateTime dueUtc = new DateTime(task.getTaskDueDateAt(), DateTimeZone.forID("UTC"));
                bodyText.append(getDueDate(dueUtc));
            } else {
                bodyText.append("Not Due Date Assigned");
            }
            bodyText.append("*");
            body.getText().setText(bodyText.toString());
            body.getAccessory().setType("image");
            body.getAccessory().setImage_url(SlackMessages.CALENDER_THUMBNAIL);
            body.getAccessory().setAlt_text("Calender Thumbnail");
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
    public void sendTaskAssigneeUpdateNotification(Task task, String newTaskAssignee) {
        User user = userRepository.getUserByUserId(task.getTaskAssignee());
        if (user.getUserSlackId() != null){
            User previous = userRepository.getUserByUserId(task.getTaskAssignee());
            User newAssignee = userRepository.getUserByUserId(newTaskAssignee);
            Project project = projectRepository.getProjectById(task.getProjectId());
            User sender = userRepository.getUserByUserId(task.getTaskInitiator());
            JSONObject payload = new JSONObject();
            payload.put(CHANNEL, newAssignee.getUserSlackId());
            payload.put(TEXT, SlackMessages.TASK_ASSIGNEE_UPDATE_TITLE);
            List<SlackBlock> blocks = new ArrayList<>();

            SlackBlock headerBlock = new SlackBlock();
            headerBlock.setType(SECTION);
            headerBlock.getText().setType(PLAIN_TEXT);
            headerBlock.getText().setText(SlackMessages.TASK_ASSIGNMENT_TRANSITION_GREETING);
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
            bodyText.append(SlackMessages.TRANSITION_ICON);
            bodyText.append(previous.getFirstName());
            bodyText.append(" ");
            bodyText.append(previous.getLastName());
            bodyText.append(SlackMessages.ARROW_ICON);
            bodyText.append(newAssignee.getFirstName());
            bodyText.append(" ");
            bodyText.append(newAssignee.getLastName());
            bodyText.append(SlackMessages.ASSIGNED_BY_ICON);
            bodyText.append(sender.getFirstName());
            bodyText.append(" ");
            bodyText.append(sender.getLastName());
            bodyText.append(SlackMessages.DUE_DATE_ICON);
            if (task.getTaskDueDateAt() != null){
                DateTime dueUtc = new DateTime(task.getTaskDueDateAt(), DateTimeZone.forID("UTC"));
                bodyText.append(getDueDate(dueUtc));
            } else {
                bodyText.append("Not Due Date Assigned");
            }
            bodyText.append("*");
            body.getText().setText(bodyText.toString());
            body.getAccessory().setType("image");
            body.getAccessory().setImage_url(SlackMessages.CALENDER_THUMBNAIL);
            body.getAccessory().setAlt_text("Calender Thumbnail");
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
    public void sendTaskNameModificationNotification(Task task, TaskUpdateDto taskUpdateDto, String type, String taskEditor) {
        User user = userRepository.getUserByUserId(task.getTaskAssignee());
        if (user.getUserSlackId() != null){
            User editor = userRepository.getUserByUserId(taskEditor);
            Project project = projectRepository.getProjectById(task.getProjectId());
            JSONObject payload = new JSONObject();
            payload.put(CHANNEL, user.getUserSlackId());
            payload.put(TEXT, SlackMessages.TASK_MODIFICATION_TITLE);
            List<SlackBlock> blocks = new ArrayList<>();

            SlackBlock headerBlock = new SlackBlock();
            headerBlock.setType(SECTION);
            headerBlock.getText().setType(PLAIN_TEXT);
            headerBlock.getText().setText(SlackMessages.TASK_MODIFICATION_GREETING);
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
            switch (type){
                case "name":
                    bodyText.append(SlackMessages.MODIFIED_NAME_ICON);
                    bodyText.append(taskUpdateDto.getTaskName());
                    bodyText.append(SlackMessages.PREVIOUS_NAME_ICON);
                    bodyText.append(task.getTaskName());
            }
            bodyText.append(SlackMessages.MODIFIED_BY_ICON);
            bodyText.append(editor.getFirstName());
            bodyText.append(" ");
            bodyText.append(editor.getLastName());
//            bodyText.append(SlackMessages.DUE_DATE_ICON);
//            if (task.getTaskDueDateAt() != null){
//                DateTime dueUtc = new DateTime(task.getTaskDueDateAt(), DateTimeZone.forID("UTC"));
//                bodyText.append(getDueDate(dueUtc));
//            } else {
//                bodyText.append("Not Due Date Assigned");
//            }
            bodyText.append("*");
            body.getText().setText(bodyText.toString());
            body.getAccessory().setType("image");
            body.getAccessory().setImage_url(SlackMessages.CALENDER_THUMBNAIL);
            body.getAccessory().setAlt_text("Calender Thumbnail");
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

    @Scheduled(initialDelay = 1000, fixedRate = 60*60*1000)
//    @Scheduled(initialDelay = 1000, fixedRate = 10000)
    public void taskReminderOnDue() {

        List<TaskAlertDto> taskAlertList = notificationRepository.getTaskAlertList();
        for(TaskAlertDto taskAlert : taskAlertList) {
            if (taskAlert.getTaskDue() != null) {
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
        try {
            JSONObject payload = new JSONObject();
            payload.put("channel", taskAlert.getAssigneeSlackId());
            StringBuilder message = new StringBuilder();
            message.append("Your Task: ");
            message.append(taskAlert.getTaskName());
            message.append(" of project ");
            message.append(taskAlert.getProjectName());
            message.append(" will be due at ");
            message.append(getDueDate(dueUtc));
            payload.put("text",message.toString());
            StringBuilder url = new StringBuilder();
            url.append(ENVConfig.SLACK_BASE_URL);
            url.append("/chat.postMessage");
            logger.info("Slack Message Url {}", url);
            HttpEntity<Object> entity = new HttpEntity<>(payload.toString(), getHttpHeaders());
            ResponseEntity<String> exchange = restTemplate.exchange(url.toString() , HttpMethod.POST, entity, String.class);
        } catch (Exception e){
            logger.info("Error calling Slack API");
        }
    }

    private String getDueDate(DateTime dueUtc){
        DateTimeFormatter fmt = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm");
        String dueFormatted = fmt.print(dueUtc);

//        int year = dueUtc.getYear();
//        int month = dueUtc.getMonthOfYear();
//        dueUtc.getDayOfMonth();
//        dueUtc.getHourOfDay();
//        dueUtc.getMinuteOfHour();

        return dueFormatted;

    }

//    private String getTaskAssignmentMessage(TaskAssignNotificationDto notificationDto){
//        StringBuilder message = new StringBuilder();
//        message.append("Task ");
//        message.append(notificationDto.getTaskName());
//        message.append(" of Project");
//        message.append(notificationDto.getProjectName());
//        message.append("is assigned to you by");
//        message.append(notificationDto.getAssignerId());
//        return message.toString();
//    }
}
