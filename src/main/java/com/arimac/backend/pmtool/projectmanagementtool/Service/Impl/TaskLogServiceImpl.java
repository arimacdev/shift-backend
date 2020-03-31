package com.arimac.backend.pmtool.projectmanagementtool.Service.Impl;

import com.arimac.backend.pmtool.projectmanagementtool.Response.Response;
import com.arimac.backend.pmtool.projectmanagementtool.Service.TaskLogService;
import com.arimac.backend.pmtool.projectmanagementtool.dtos.TaskLogResponse;
import com.arimac.backend.pmtool.projectmanagementtool.dtos.TaskLogUser;
import com.arimac.backend.pmtool.projectmanagementtool.enumz.LogEntityEnum;
import com.arimac.backend.pmtool.projectmanagementtool.enumz.LogOperationEnum;
import com.arimac.backend.pmtool.projectmanagementtool.enumz.ResponseMessage;
import com.arimac.backend.pmtool.projectmanagementtool.model.Project;
import com.arimac.backend.pmtool.projectmanagementtool.model.Task;
import com.arimac.backend.pmtool.projectmanagementtool.model.TaskLog;
import com.arimac.backend.pmtool.projectmanagementtool.repository.ProjectRepository;
import com.arimac.backend.pmtool.projectmanagementtool.repository.TaskLogRespository;
import com.arimac.backend.pmtool.projectmanagementtool.repository.TaskRepository;
import com.arimac.backend.pmtool.projectmanagementtool.utils.UtilsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class TaskLogServiceImpl implements TaskLogService {
    private static final Logger logger = LoggerFactory.getLogger(TaskLogServiceImpl.class);

    private static final int ADD = 1;
    private static final int UPDATE = 2;
    private static final int DELETE = 3;
    private static final int ASSIGN = 4;

    private final TaskLogRespository taskLogRespository;
    private final TaskRepository taskRepository;
    private final ProjectRepository projectRepository;
    private final UtilsService utilsService;

    public TaskLogServiceImpl(TaskLogRespository taskLogRespository, TaskRepository taskRepository, ProjectRepository projectRepository, UtilsService utilsService) {
        this.taskLogRespository = taskLogRespository;
        this.taskRepository = taskRepository;
        this.projectRepository = projectRepository;
        this.utilsService = utilsService;
    }

//    @Override
//    public Object addTaskLog(Task task) {
//        TaskLog log = new TaskLog();
//        log.setTaskLogId(utilsService.getUUId());
//        log.setTasklogInitiator(task.getTaskInitiator());
//        log.setProjectId(task.getProjectId());
//        log.setTaskLogEntity(LogEntityEnum.Task.getEntityId());
//        log.setTaskLogEntityId(task.getTaskId());
//        log.setOperation(LogOperationEnum.CREATE.getOperationId());
//        log.setTimestamp(utilsService.getCurrentTimestamp());
//        taskLogRespository.addTaskLog(log);
//        return null;
//    }
//
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
//
//                        taskLogResponseList.add(taskLogResponse);
//                        break;
//
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
