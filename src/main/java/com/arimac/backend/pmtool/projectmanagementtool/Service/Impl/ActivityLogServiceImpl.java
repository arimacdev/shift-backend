package com.arimac.backend.pmtool.projectmanagementtool.Service.Impl;

import com.arimac.backend.pmtool.projectmanagementtool.Response.Response;
import com.arimac.backend.pmtool.projectmanagementtool.Service.ActivityLogService;
import com.arimac.backend.pmtool.projectmanagementtool.dtos.ActivityLog.FieldValue;
import com.arimac.backend.pmtool.projectmanagementtool.dtos.ActivityLog.TaskLogResposeDto;
import com.arimac.backend.pmtool.projectmanagementtool.dtos.ActivityLog.UserActivityLog;
import com.arimac.backend.pmtool.projectmanagementtool.enumz.ActivityLog.LogOperationEnum;
import com.arimac.backend.pmtool.projectmanagementtool.enumz.ActivityLog.TaskUpdateTypeEnum;
import com.arimac.backend.pmtool.projectmanagementtool.enumz.ResponseMessage;
import com.arimac.backend.pmtool.projectmanagementtool.exception.ErrorMessage;
import com.arimac.backend.pmtool.projectmanagementtool.model.ActivityLog;
import com.arimac.backend.pmtool.projectmanagementtool.model.User;
import com.arimac.backend.pmtool.projectmanagementtool.repository.ActivityLogRepository;
import com.arimac.backend.pmtool.projectmanagementtool.repository.ProjectRepository;
import com.arimac.backend.pmtool.projectmanagementtool.repository.TaskRepository;
import com.arimac.backend.pmtool.projectmanagementtool.repository.UserRepository;
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
    private final UtilsService utilsService;


    public ActivityLogServiceImpl(ActivityLogRepository activityLogRepository, TaskRepository taskRepository, ProjectRepository projectRepository, UserRepository userRepository, UtilsService utilsService) {
        this.activityLogRepository = activityLogRepository;
        this.taskRepository = taskRepository;
        this.projectRepository = projectRepository;
        this.userRepository = userRepository;
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
        List<UserActivityLog> activityLogList = activityLogRepository.getTaskActivity(taskId, limit, startIndex);
        List<TaskLogResposeDto> taskLogResposeList = new ArrayList<>();
        for (UserActivityLog activityLog : activityLogList){
            TaskLogResposeDto taskLog = new TaskLogResposeDto();
            taskLog.setLogId(activityLog.getLogId());
            taskLog.setEntityType(activityLog.getEntityType());
            taskLog.setEntityId(activityLog.getEntityId());
            taskLog.setOperation(activityLog.getOperation());
            taskLog.setActionTimestamp(activityLog.getActionTimestamp());
            taskLog.setActor(activityLog.getActor());
            taskLog.setActorFirstName(activityLog.getFirstName());
            taskLog.setActorLastName(activityLog.getLastName());
            taskLog.setActorProfileImage(activityLog.getActorProfileImage());
            if (activityLog.getOperation().equals(LogOperationEnum.UPDATE)) {
                taskLog.setUpdateType(activityLog.getUpdateType());
                FieldValue previous = new FieldValue();
                previous.setDisplayValue(activityLog.getPreviousValue());
                FieldValue updated = new FieldValue();
                updated.setDisplayValue(activityLog.getUpdatedvalue());
                if (activityLog.getUpdateType().equals(TaskUpdateTypeEnum.ASSIGNEE.toString())){
                    previous.setValue(activityLog.getPreviousValue());
                    updated.setValue(activityLog.getUpdatedvalue());
                    User previousUser;
                    User updatedUser;
                    if (activityLog.getPreviousValue()!= null) {
                        previousUser = userRepository.getUserByUserId(activityLog.getPreviousValue());
                        previous.setDisplayValue(previousUser.getFirstName() + " " + previousUser.getLastName());
                    }
                    if (activityLog.getUpdatedvalue() != null) {
                        updatedUser = userRepository.getUserByUserId(activityLog.getUpdatedvalue());
                        updated.setDisplayValue(updatedUser.getFirstName() + " " + updatedUser.getLastName());
                    }
                }
                taskLog.setPreviousValue(previous);
                taskLog.setUpdatedvalue(updated);
            }
            taskLogResposeList.add(taskLog);
        }
        return new Response(ResponseMessage.SUCCESS, HttpStatus.OK, taskLogResposeList);
    }

//    @Override
//    public Object getAllLogs(String projectId) {
//        //Check project existence
//        List<TaskLogUser> taskLogList = taskLogRespository.getAllLogs(projectId);
//        List<TaskLogResponse> taskLogResponseList = new ArrayList<>();
//        for (TaskLogUser taskLog : taskLogList){
//            switch (taskLog.getTaskLogEntity()){
//                case (2):
//                    Task task = taskRepository.getProjectTaskWithDeleted(taskLog.getTaskLogEntityId());
//                    if (task != null){
//                        TaskLogResponse taskLogResponse = new TaskLogResponse();
//                        //Entity Details
//                        taskLogResponse.setTaskLogEntity(taskLog.getTaskLogEntity());
//                        taskLogResponse.setTaskLogEntityId(taskLog.getTaskLogEntityId());
//                        taskLogResponse.setOperation(taskLog.getOperation());
//                        taskLogResponse.setTaskLogEntityName(task.getTaskName());
//                        //Initiator Details
//                        taskLogResponse.setTasklogInitiator(taskLog.getTasklogInitiator());
//                        taskLogResponse.setUserId(taskLog.getTasklogInitiator());
//                        taskLogResponse.setFirstName(taskLog.getFirstName());
//                        taskLogResponse.setLastName(taskLog.getLastName());
//                        taskLogResponse.setProfileImage(taskLog.getProfileImage());
//                        //Task Log details
//                        taskLogResponse.setTaskLogId(taskLog.getTaskLogId());
//                        if (taskLog.getOperation() == UPDATE){ // If operation is update
//                            taskLogResponse.setModifiedField(taskLog.getModified());
//                            taskLogResponse.setPrevious(taskLog.getPrevious());
//                            taskLogResponse.setModified(taskLog.getModified());
//                        }
//                        taskLogResponse.setTimestamp(taskLog.getTimestamp());
//                        taskLogResponseList.add(taskLogResponse);
//                        break;
//                    }
//                case (1):
//                    Project project = projectRepository.getProjectById(projectId);
//                    //Entity Details
//                    TaskLogResponse taskLogResponse = new TaskLogResponse();
//                    taskLogResponse.setTaskLogEntity(taskLog.getTaskLogEntity());
//                    taskLogResponse.setTaskLogEntityId(taskLog.getTaskLogEntityId());
//                    taskLogResponse.setOperation(taskLog.getOperation());
//                    taskLogResponse.setTaskLogEntityName(project.getProjectName());
//                    //Initiator Details
//                    taskLogResponse.setTasklogInitiator(taskLog.getTasklogInitiator());
//                    taskLogResponse.setUserId(taskLog.getTasklogInitiator());
//                    taskLogResponse.setFirstName(taskLog.getFirstName());
//                    taskLogResponse.setLastName(taskLog.getLastName());
//                    taskLogResponse.setProfileImage(taskLog.getProfileImage());
//                    //Task Log details
//                    taskLogResponse.setTaskLogId(taskLog.getTaskLogId());
//                    if (taskLog.getOperation() == UPDATE){ // If operation is update
//                        taskLogResponse.setPrevious(taskLog.getPrevious());
//                        taskLogResponse.setModified(taskLog.getModified());
//                    }
//                    taskLogResponse.setTimestamp(taskLog.getTimestamp());
//
//                    taskLogResponseList.add(taskLogResponse);
//            }
//
//        }
////        List<TaskLog> sortedUsers = taskLogList.stream()
////                .sorted(Comparator.comparing(TaskLog::getTimestamp))
////                .collect(Collectors.toList());
////        return new Response(ResponseMessage.SUCCESS, HttpStatus.OK, sortedUsers);
//        return new Response(ResponseMessage.SUCCESS, HttpStatus.OK, taskLogResponseList);
//    }

}
