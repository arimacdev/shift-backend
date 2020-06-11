package com.arimac.backend.pmtool.projectmanagementtool.Service.Impl;

import com.arimac.backend.pmtool.projectmanagementtool.Response.Response;
import com.arimac.backend.pmtool.projectmanagementtool.Service.ActivityLogService;
import com.arimac.backend.pmtool.projectmanagementtool.dtos.ActivityLog.ActivityLogCountResponse;
import com.arimac.backend.pmtool.projectmanagementtool.dtos.ActivityLog.ActivityLogResposeDto;
import com.arimac.backend.pmtool.projectmanagementtool.dtos.ActivityLog.FieldValue;
import com.arimac.backend.pmtool.projectmanagementtool.dtos.ActivityLog.UserActivityLog;
import com.arimac.backend.pmtool.projectmanagementtool.enumz.ActivityLog.EntityEnum;
import com.arimac.backend.pmtool.projectmanagementtool.enumz.ActivityLog.LogOperationEnum;
import com.arimac.backend.pmtool.projectmanagementtool.enumz.ActivityLog.ProjectUpdateTypeEnum;
import com.arimac.backend.pmtool.projectmanagementtool.enumz.ActivityLog.TaskUpdateTypeEnum;
import com.arimac.backend.pmtool.projectmanagementtool.enumz.ResponseMessage;
import com.arimac.backend.pmtool.projectmanagementtool.exception.ErrorMessage;
import com.arimac.backend.pmtool.projectmanagementtool.model.*;
import com.arimac.backend.pmtool.projectmanagementtool.repository.*;
import com.arimac.backend.pmtool.projectmanagementtool.utils.UtilsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ActivityLogServiceImpl implements ActivityLogService {
    private static final Logger logger = LoggerFactory.getLogger(ActivityLogServiceImpl.class);

    private final ActivityLogRepository activityLogRepository;
    private final TaskRepository taskRepository;
    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;
    private final TaskFileRepository taskFileRepository;
    private final ProjectFileRepository projectFileRepository;
    private final UtilsService utilsService;

    public ActivityLogServiceImpl(ActivityLogRepository activityLogRepository, TaskRepository taskRepository, ProjectRepository projectRepository, UserRepository userRepository, TaskFileRepository taskFileRepository, ProjectFileRepository projectFileRepository, UtilsService utilsService) {
        this.activityLogRepository = activityLogRepository;
        this.taskRepository = taskRepository;
        this.projectRepository = projectRepository;
        this.userRepository = userRepository;
        this.taskFileRepository = taskFileRepository;
        this.projectFileRepository = projectFileRepository;
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
        activityLogCountResponse.setActivityLogList(getLogEntryList(activityLogList, EntityEnum.TASK));
        return new Response(ResponseMessage.SUCCESS, HttpStatus.OK, activityLogCountResponse);
    }

    @Override
    public Object getAllProjectLogsWithTasks(String userId, String projectId, int startIndex, int endIndex) {
        if (startIndex < 0 || endIndex < 0 || endIndex < startIndex)
            return new ErrorMessage("Invalid Start/End Index", HttpStatus.BAD_REQUEST);
        int limit = endIndex - startIndex;
        Project project = projectRepository.getProjectById(projectId);
        if (project == null)
            return new ErrorMessage(ResponseMessage.PROJECT_NOT_FOUND, HttpStatus.NOT_FOUND);
        List<String> entityIds = projectRepository.getProjectTaskIds(projectId);
        entityIds.add(projectId);
        List<UserActivityLog> activityLogList = activityLogRepository.getProjectActivity(projectId, entityIds,  limit, startIndex);
        ActivityLogCountResponse activityLogCountResponse = new ActivityLogCountResponse();
        activityLogCountResponse.setActivityLogCount(activityLogRepository.projectActivityLogCount(projectId, entityIds));
        activityLogCountResponse.setActivityLogList(getLogEntryList(activityLogList, EntityEnum.PROJECT));
        return new Response(ResponseMessage.SUCCESS, HttpStatus.OK, activityLogCountResponse);
    }

    @Override
    public void flagEntityActivityLogs(String entityId) {
        activityLogRepository.flagEntityActivityLogs(entityId);
    }

    private List<ActivityLogResposeDto> getLogEntryList(List<UserActivityLog> activityLogList, EntityEnum entity){
        List<ActivityLogResposeDto> taskLogResposeList = new ArrayList<>();
        Map<String, String> taskMap = new HashMap<>();
        Map<String, User> userMap = new HashMap<>();
        for (UserActivityLog activityLog : activityLogList){
            ActivityLogResposeDto logResponse = new ActivityLogResposeDto();
            logResponse.setLogId(activityLog.getLogId());
            if (entity.equals(EntityEnum.PROJECT) && activityLog.getEntityType().equals(EntityEnum.TASK)) {
                if (taskMap.get(activityLog.getEntityId()) != null)
                    logResponse.setEntityName(taskMap.get(activityLog.getEntityId()));
                else {
                    Task task = taskRepository.getProjectTasksWithFlag(activityLog.getEntityId());
                    taskMap.put(activityLog.getEntityId(), task.getTaskName());
                    logResponse.setEntityName(task.getTaskName());
                }
            }
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
                setTaskUpdateValues(activityLog, previous, updated, userMap);
                else if (activityLog.getEntityType().equals(EntityEnum.PROJECT))
                setProjectUpdateValues(activityLog, previous, updated, userMap);
                logResponse.setPreviousValue(previous);
                logResponse.setUpdatedvalue(updated);
            }
            taskLogResposeList.add(logResponse);
        }

        return taskLogResposeList;
    }

    private void setTaskUpdateValues(UserActivityLog activityLog, FieldValue previous, FieldValue updated, Map<String, User> userMap){
        if (activityLog.getUpdateType().equals(TaskUpdateTypeEnum.ASSIGNEE.toString())){
            previous.setValue(activityLog.getPreviousValue());
            updated.setValue(activityLog.getUpdatedvalue());
            User previousUser;
            User updatedUser;
            if (activityLog.getPreviousValue()!= null) {
                if (userMap.containsKey(activityLog.getPreviousValue()))
                    previousUser = userMap.get(activityLog.getPreviousValue());
                else {
                    previousUser = userRepository.getUserByUserId(activityLog.getPreviousValue());
                    userMap.put(activityLog.getPreviousValue(), previousUser);
                }
                previous.setDisplayValue(previousUser.getFirstName() + " " + previousUser.getLastName());
                previous.setProfileImage(previousUser.getProfileImage());
            }
            if (activityLog.getUpdatedvalue() != null) {
                if (userMap.containsKey(activityLog.getUpdatedvalue()))
                    updatedUser = userMap.get(activityLog.getUpdatedvalue());
                else {
                    updatedUser = userRepository.getUserByUserId(activityLog.getUpdatedvalue());
                    userMap.put(activityLog.getUpdatedvalue(), updatedUser);
                }
                updated.setDisplayValue(updatedUser.getFirstName() + " " + updatedUser.getLastName());
                updated.setProfileImage(updatedUser.getProfileImage());
            }
        } else if (activityLog.getUpdateType().equals(TaskUpdateTypeEnum.FILE.toString())){
            TaskFile taskFile;
            if (activityLog.getUpdatedvalue()!= null){
                taskFile = taskFileRepository.getTaskFileWithFlag(activityLog.getUpdatedvalue());
                if (taskFile != null) {
                    updated.setDisplayValue(taskFile.getTaskFileName());
                    updated.setValue(taskFile.getTaskFileUrl());
                }
            } else {
                taskFile = taskFileRepository.getTaskFileWithFlag(activityLog.getPreviousValue());
                if (taskFile != null) {
                    previous.setDisplayValue(taskFile.getTaskFileName());
                   // previous.setValue(taskFile.getTaskFileUrl());
                }
            }
        }
    }

    private void setProjectUpdateValues(UserActivityLog activityLog, FieldValue previous, FieldValue updated, Map<String, User> userMap){
        if (activityLog.getUpdateType().equals(ProjectUpdateTypeEnum.ADD_USER.toString())){
            User addedUser;
            if (userMap.containsKey(activityLog.getUpdatedvalue())){
                addedUser = userMap.get(activityLog.getUpdatedvalue());
            } else {
            addedUser = userRepository.getUserByUserId(activityLog.getUpdatedvalue());
            userMap.put(activityLog.getUpdatedvalue(), addedUser);
            }
            if (addedUser != null){
                updated.setValue(activityLog.getUpdatedvalue());
                updated.setDisplayValue(addedUser.getFirstName() + " " + addedUser.getLastName());
                updated.setProfileImage(addedUser.getProfileImage());
            }
        } else if (activityLog.getUpdateType().equals(ProjectUpdateTypeEnum.REMOVE_USER.toString())){
            User removeUser;
            if (userMap.containsKey(activityLog.getPreviousValue())){
                removeUser = userMap.get(activityLog.getPreviousValue());
            } else {
                removeUser = userRepository.getUserByUserId(activityLog.getPreviousValue());
                userMap.put(activityLog.getPreviousValue(), removeUser);
            }
            if (removeUser != null){
                previous.setDisplayValue(removeUser.getFirstName() + " " + removeUser.getLastName());
                previous.setValue(removeUser.getUserId());
                previous.setProfileImage(removeUser.getProfileImage());
            }
        } else if (activityLog.getUpdateType().equals(ProjectUpdateTypeEnum.FILE.toString())){
            if (activityLog.getUpdatedvalue() != null){
                ProjectFile projectFile = projectFileRepository.getProjectFileWithFlag(activityLog.getUpdatedvalue());
                if (projectFile != null){
                    updated.setDisplayValue(projectFile.getProjectFileName());
                    updated.setValue(projectFile.getProjectFileUrl());
                }
            } else if (activityLog.getPreviousValue() != null){
                ProjectFile projectFile = projectFileRepository.getProjectFileWithFlag(activityLog.getPreviousValue());
                if (projectFile != null){
                    previous.setDisplayValue(projectFile.getProjectFileName());
                }
            }
        }
    }


}

