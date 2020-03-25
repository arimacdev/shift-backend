package com.arimac.backend.pmtool.projectmanagementtool.Service.Impl;

import com.arimac.backend.pmtool.projectmanagementtool.Response.Response;
import com.arimac.backend.pmtool.projectmanagementtool.Service.NotificationService;
import com.arimac.backend.pmtool.projectmanagementtool.dtos.SlackNotificationDto;
import com.arimac.backend.pmtool.projectmanagementtool.dtos.TaskAlertDto;
import com.arimac.backend.pmtool.projectmanagementtool.enumz.ResponseMessage;
import com.arimac.backend.pmtool.projectmanagementtool.exception.ErrorMessage;
import com.arimac.backend.pmtool.projectmanagementtool.model.User;
import com.arimac.backend.pmtool.projectmanagementtool.repository.TaskRepository;
import com.arimac.backend.pmtool.projectmanagementtool.repository.UserRepository;
import com.arimac.backend.pmtool.projectmanagementtool.utils.ENVConfig;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.*;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.sql.Timestamp;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

@Service
public class NotificationServiceImpl implements NotificationService {
    private static final Logger logger = LoggerFactory.getLogger(NotificationServiceImpl.class);

    private final UserRepository userRepository;
    private final TaskRepository taskRepository;

    private final RestTemplate restTemplate;

    public NotificationServiceImpl(UserRepository userRepository, TaskRepository taskRepository, RestTemplate restTemplate) {
        this.userRepository = userRepository;
        this.taskRepository = taskRepository;
        this.restTemplate = restTemplate;
    }

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

    @Scheduled(initialDelay = 1000, fixedDelay = 100000)
    public void run() {

        Date date = new Date();
        long currentTime = new Timestamp(date.getTime()).getTime();
        List<TaskAlertDto> taskAlertList = taskRepository.getTaskAlertList();
        for(TaskAlertDto taskAlert : taskAlertList) {
            if (taskAlert.getTaskDue() != null) {
//                long taskReminder = taskAlert.getTaskDue().getTime();
                long taskReminder = taskAlert.getTaskDue().toInstant().atZone( ZoneId.of( "UTC" )).toEpochSecond();

//                if (taskReminder > currentTime) {
//                    logger.info("current time ---> {}", currentTime);
//                    logger.info("reminder time ---> {}", taskReminder);
//                    long difference = ((taskReminder - currentTime)) / (1000 * 60);
//                    logger.info("Time difference in minutes --> {}", difference);
//                    if (difference < 30) {
                    HttpHeaders httpHeaders = new HttpHeaders();
                    httpHeaders.set("Authorization", "Bearer " + ENVConfig.SLACK_BOT_TOKEN);
                    httpHeaders.set("Content-Type", "application/json");
                    JSONObject payload = new JSONObject();
                    payload.put("channel", "UGQ0FGZ5F");
                    StringBuilder message = new StringBuilder();
                    message.append("Your Task: ");
                    message.append(taskAlert.getTaskName());
                    message.append(" of project ");
                    message.append(taskAlert.getProjectId());
                    message.append(" will be due in 30 minutes");
                    payload.put("text",message.toString());
                    StringBuilder url = new StringBuilder();
                    url.append(ENVConfig.SLACK_BASE_URL);
                    url.append("/chat.postMessage");
                    logger.info("Slack Message Url {}", url);
                    HttpEntity<Object> entity = new HttpEntity<>(payload.toString(), httpHeaders);
                    ResponseEntity<String> exchange = restTemplate.exchange(url.toString() , HttpMethod.POST, entity, String.class);
//                    }
//                }
            }
        }

//        HttpHeaders httpHeaders = new HttpHeaders();
//        httpHeaders.set("Authorization", "Bearer " + "xoxb-345426929140-1018006515684-vthqTkN55akhuMZdMmrcMwug");
//        httpHeaders.set("Content-Type", "application/json");
//        JSONObject payload = new JSONObject();
//        payload.put("text", "task reminder");
//        String url = "https://hooks.slack.com/services/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";
//        HttpEntity<Object> entity = new HttpEntity<>(payload.toString(), httpHeaders);
//        ResponseEntity<String> exchange = restTemplate.exchange(url , HttpMethod.POST, entity, String.class);

        logger.info("Current time is :: " + System.currentTimeMillis());
    }
}
