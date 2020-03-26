package com.arimac.backend.pmtool.projectmanagementtool.Service.Impl;

import com.arimac.backend.pmtool.projectmanagementtool.Response.Response;
import com.arimac.backend.pmtool.projectmanagementtool.Service.TaskLogService;
import com.arimac.backend.pmtool.projectmanagementtool.enumz.LogEntityEnum;
import com.arimac.backend.pmtool.projectmanagementtool.enumz.ResponseMessage;
import com.arimac.backend.pmtool.projectmanagementtool.model.Task;
import com.arimac.backend.pmtool.projectmanagementtool.model.TaskLog;
import com.arimac.backend.pmtool.projectmanagementtool.repository.TaskLogRespository;
import com.arimac.backend.pmtool.projectmanagementtool.utils.UtilsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TaskLogServiceImpl implements TaskLogService {
    private static final Logger logger = LoggerFactory.getLogger(TaskLogServiceImpl.class);

    private final TaskLogRespository taskLogRespository;
    private final UtilsService utilsService;

    public TaskLogServiceImpl(TaskLogRespository taskLogRespository, UtilsService utilsService) {
        this.taskLogRespository = taskLogRespository;
        this.utilsService = utilsService;
    }

    @Override
    public Object addTaskLog(Task task) {
        TaskLog log = new TaskLog();
        log.setTaskLogId(utilsService.getUUId());
        log.setTasklogInitiator(task.getTaskInitiator());
        log.setProjectId(task.getProjectId());
//        log.setTaskLogEntity(LogEntityEnum.Task.getEntityId());
        log.setTaskLogEntityId(task.getTaskId());
        log.setOperation("CREATE");
        log.setTimestamp(utilsService.getCurrentTimestamp());
        taskLogRespository.addTaskLog(log);
        return null;
    }

    @Override
    public Object getAllLogs(String projectId) {
        List<TaskLog> taskLogList = taskLogRespository.getAllLogs(projectId);
        List<TaskLog> sortedUsers = taskLogList.stream()
                .sorted(Comparator.comparing(TaskLog::getTimestamp))
                .collect(Collectors.toList());
        return new Response(ResponseMessage.SUCCESS, HttpStatus.OK, sortedUsers);
    }
}
