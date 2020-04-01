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
import org.joda.time.*;
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

    @Scheduled(initialDelay = 1000, fixedRate = 60*60*1000)
    public void run() {

        Date date = new Date();
        long currentTime = new Timestamp(date.getTime()).getTime();
        List<TaskAlertDto> taskAlertList = taskRepository.getTaskAlertList();
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
                if (timeFixDifference < 60 && timeFixDifference > 0){
                    try {
                        HttpHeaders httpHeaders = new HttpHeaders();
                        httpHeaders.set("Authorization", "Bearer " + ENVConfig.SLACK_BOT_TOKEN);
                        httpHeaders.set("Content-Type", "application/json");
                        JSONObject payload = new JSONObject();
                        payload.put("channel", taskAlert.getAssigneeSlackId());
                        StringBuilder message = new StringBuilder();
                        message.append("Your Task: ");
                        message.append(taskAlert.getTaskName());
                        message.append(" of project ");
                        message.append(taskAlert.getProjectName());
                        message.append(" will be due at ");
                        message.append(dueUtc.toString());
                        payload.put("text",message.toString());
                        StringBuilder url = new StringBuilder();
                        url.append(ENVConfig.SLACK_BASE_URL);
                        url.append("/chat.postMessage");
                        logger.info("Slack Message Url {}", url);
                        HttpEntity<Object> entity = new HttpEntity<>(payload.toString(), httpHeaders);
                        ResponseEntity<String> exchange = restTemplate.exchange(url.toString() , HttpMethod.POST, entity, String.class);
                    } catch (Exception e){
                        logger.info("Error calling Slack API");
                    }

                }
            }
        }

    }
}
