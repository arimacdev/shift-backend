package com.arimac.backend.pmtool.projectmanagementtool.Service.Impl;

import com.arimac.backend.pmtool.projectmanagementtool.Response.Response;
import com.arimac.backend.pmtool.projectmanagementtool.Service.ActivityLogService;
import com.arimac.backend.pmtool.projectmanagementtool.dtos.ActivityLog.UserActivityLog;
import com.arimac.backend.pmtool.projectmanagementtool.enumz.ResponseMessage;
import com.arimac.backend.pmtool.projectmanagementtool.exception.ErrorMessage;
import com.arimac.backend.pmtool.projectmanagementtool.model.ActivityLog;
import com.arimac.backend.pmtool.projectmanagementtool.repository.ActivityLogRepository;
import com.arimac.backend.pmtool.projectmanagementtool.repository.ProjectRepository;
import com.arimac.backend.pmtool.projectmanagementtool.repository.TaskRepository;
import com.arimac.backend.pmtool.projectmanagementtool.utils.UtilsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ActivityLogServiceImpl implements ActivityLogService {
    private static final Logger logger = LoggerFactory.getLogger(ActivityLogServiceImpl.class);

    private final ActivityLogRepository activityLogRepository;
    private final TaskRepository taskRepository;
    private final ProjectRepository projectRepository;
    private final UtilsService utilsService;


    public ActivityLogServiceImpl(ActivityLogRepository activityLogRepository, TaskRepository taskRepository, ProjectRepository projectRepository, UtilsService utilsService) {
        this.activityLogRepository = activityLogRepository;
        this.taskRepository = taskRepository;
        this.projectRepository = projectRepository;
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
        return new Response(ResponseMessage.SUCCESS, HttpStatus.OK, activityLogList);
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
