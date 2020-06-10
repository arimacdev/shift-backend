package com.arimac.backend.pmtool.projectmanagementtool.Service.Impl;

import com.arimac.backend.pmtool.projectmanagementtool.Response.Response;
import com.arimac.backend.pmtool.projectmanagementtool.Service.ActivityLogService;
import com.arimac.backend.pmtool.projectmanagementtool.dtos.ActivityLog.ActivityLogCountResponse;
import com.arimac.backend.pmtool.projectmanagementtool.dtos.ActivityLog.ActivityLogResposeDto;
import com.arimac.backend.pmtool.projectmanagementtool.dtos.ActivityLog.FieldValue;
import com.arimac.backend.pmtool.projectmanagementtool.dtos.ActivityLog.UserActivityLog;
import com.arimac.backend.pmtool.projectmanagementtool.enumz.ActivityLog.EntityEnum;
import com.arimac.backend.pmtool.projectmanagementtool.enumz.ActivityLog.LogOperationEnum;
import com.arimac.backend.pmtool.projectmanagementtool.enumz.ActivityLog.TaskUpdateTypeEnum;
import com.arimac.backend.pmtool.projectmanagementtool.enumz.ResponseMessage;
import com.arimac.backend.pmtool.projectmanagementtool.exception.ErrorMessage;
import com.arimac.backend.pmtool.projectmanagementtool.model.ActivityLog;
import com.arimac.backend.pmtool.projectmanagementtool.model.Task;
import com.arimac.backend.pmtool.projectmanagementtool.model.TaskFile;
import com.arimac.backend.pmtool.projectmanagementtool.model.User;
import com.arimac.backend.pmtool.projectmanagementtool.repository.*;
import com.arimac.backend.pmtool.projectmanagementtool.utils.UtilsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ActivityLogServiceImpl implements ActivityLogService {
    private static final Logger logger = LoggerFactory.getLogger(ActivityLogServiceImpl.class);

    private final ActivityLogRepository activityLogRepository;
    private final TaskRepository taskRepository;
    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;
    private final TaskFileRepository taskFileRepository;
    private final UtilsService utilsService;


    public ActivityLogServiceImpl(ActivityLogRepository activityLogRepository, TaskRepository taskRepository, ProjectRepository projectRepository, UserRepository userRepository, TaskFileRepository taskFileRepository, UtilsService utilsService) {
        this.activityLogRepository = activityLogRepository;
        this.taskRepository = taskRepository;
        this.projectRepository = projectRepository;
        this.userRepository = userRepository;
        this.taskFileRepository = taskFileRepository;
        this.utilsService = utilsService;
    }

    @Override
    public void addTaskLog(ActivityLog activityLog) {
          activityLogRepository.addActivityLogEntry(activityLog);
    }

    @Override
    public Object getTaskActivity(String userId, String taskId, int startIndex, int endIndex) {
        if (startIndex < 0 || endIndex < 0 || endIndex < startIndex)
            return new ErrorMessage("Invalid Start/End Index", HttpStatus.BAD_REQUEST);
        int limit = endIndex - startIndex;
        Task task = taskRepository.getProjectTask(taskId);
        if (task == null) //////// CHECK
            return new ErrorMessage(ResponseMessage.TASK_NOT_FOUND, HttpStatus.NOT_FOUND);
        List<UserActivityLog> activityLogList = activityLogRepository.getTaskActivity(taskId, limit, startIndex);
        ActivityLogCountResponse activityLogCountResponse = new ActivityLogCountResponse();
        activityLogCountResponse.setActivityLogCount(activityLogRepository.taskActivityLogCount(taskId));
        activityLogCountResponse.setActivityLogList(getLogEntryList(activityLogList));
        return new Response(ResponseMessage.SUCCESS, HttpStatus.OK, activityLogCountResponse);
    }

    private List<ActivityLogResposeDto> getLogEntryList(List<UserActivityLog> activityLogList){
        List<ActivityLogResposeDto> taskLogResposeList = new ArrayList<>();
        for (UserActivityLog activityLog : activityLogList){
            ActivityLogResposeDto logResponse = new ActivityLogResposeDto();
            logResponse.setLogId(activityLog.getLogId());
            logResponse.setEntityType(activityLog.getEntityType());
            logResponse.setEntityId(activityLog.getEntityId());
            logResponse.setOperation(activityLog.getOperation());
            logResponse.setActionTimestamp(activityLog.getActionTimestamp());
            logResponse.setActor(activityLog.getActor());
            logResponse.setActorFirstName(activityLog.getFirstName());
            logResponse.setActorLastName(activityLog.getLastName());
            logResponse.setActorProfileImage(activityLog.getActorProfileImage());
            if (activityLog.getOperation().equals(LogOperationEnum.UPDATE)) {
                logResponse.setUpdateType(activityLog.getUpdateType());
                FieldValue previous = new FieldValue();
                previous.setDisplayValue(activityLog.getPreviousValue());
                FieldValue updated = new FieldValue();
                updated.setDisplayValue(activityLog.getUpdatedvalue());
                if (activityLog.getEntityType().equals(EntityEnum.TASK))
                setTaskUpdateValues(activityLog, previous, updated);
//                else if (activityLog.getEntityType().equals(EntityEnum.PROJECT))
                logResponse.setPreviousValue(previous);
                logResponse.setUpdatedvalue(updated);
            }
            taskLogResposeList.add(logResponse);
        }

        return taskLogResposeList;
    }

    private void setTaskUpdateValues(UserActivityLog activityLog, FieldValue previous, FieldValue updated){
        if (activityLog.getUpdateType().equals(TaskUpdateTypeEnum.ASSIGNEE.toString())){
            previous.setValue(activityLog.getPreviousValue());
            updated.setValue(activityLog.getUpdatedvalue());
            User previousUser;
            User updatedUser;
            if (activityLog.getPreviousValue()!= null) {
                previousUser = userRepository.getUserByUserId(activityLog.getPreviousValue());
                previous.setDisplayValue(previousUser.getFirstName() + " " + previousUser.getLastName());
                previous.setProfileImage(previousUser.getProfileImage());
            }
            if (activityLog.getUpdatedvalue() != null) {
                updatedUser = userRepository.getUserByUserId(activityLog.getUpdatedvalue());
                updated.setDisplayValue(updatedUser.getFirstName() + " " + updatedUser.getLastName());
                updated.setProfileImage(updatedUser.getProfileImage());
            }
        } else if (activityLog.getUpdateType().equals(TaskUpdateTypeEnum.FILE.toString())){
            TaskFile taskFile;
            if (activityLog.getUpdatedvalue()!= null){
                taskFile = taskFileRepository.getTaskFileById(activityLog.getUpdatedvalue());
                if (taskFile != null) {
                    updated.setDisplayValue(taskFile.getTaskFileName());
                    updated.setValue(taskFile.getTaskFileUrl());
                } else {
                    updated.setDisplayValue("DELETED");
                }
            }
        }
    }

    private void setProjectUpdateValues(UserActivityLog activityLog, FieldValue previous, FieldValue updated){

    }


}

