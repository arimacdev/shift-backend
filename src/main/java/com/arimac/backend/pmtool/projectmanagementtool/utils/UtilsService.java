package com.arimac.backend.pmtool.projectmanagementtool.utils;

import com.arimac.backend.pmtool.projectmanagementtool.enumz.ActivityLog.EntityEnum;
import com.arimac.backend.pmtool.projectmanagementtool.enumz.ActivityLog.LogOperationEnum;
import com.arimac.backend.pmtool.projectmanagementtool.enumz.ActivityLog.ProjectUpdateTypeEnum;
import com.arimac.backend.pmtool.projectmanagementtool.enumz.ActivityLog.TaskUpdateTypeEnum;
import com.arimac.backend.pmtool.projectmanagementtool.model.ActivityLog;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.LocalDateTime;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.util.UUID;

@Component
public class UtilsService {

    public String getUUId(){
        UUID uuid = UUID.randomUUID();
        return uuid.toString();
    }

    public Timestamp getCurrentTimestamp(){
        LocalDateTime currentDateAndTime = LocalDateTime.now();
        return new Timestamp(currentDateAndTime.toDateTime().getMillis());
    }

    public DateTime getCurrentDateTime(){
        DateTime now = DateTime.now();
        DateTime nowCol = new DateTime(now, DateTimeZone.forID("Asia/Colombo"));
        return nowCol;
    }

    public ActivityLog addTaskUpdateLog(LogOperationEnum operation, String actor, String taskId, TaskUpdateTypeEnum updateType, String previous, String updated){
        ActivityLog activityLog = new ActivityLog();
        activityLog.setLogId(getUUId());
        activityLog.setEntityType(EntityEnum.TASK);
        activityLog.setEntityId(taskId);
        activityLog.setActionTimestamp(getCurrentTimestamp());
        activityLog.setOperation(operation);
        activityLog.setUpdateType(updateType.toString());
        activityLog.setPreviousValue(previous);
        activityLog.setUpdatedvalue(updated);
        activityLog.setActor(actor);

        return  activityLog;
    }

    public ActivityLog addTaskAddorFlagLog(LogOperationEnum operation, String actor, String taskId){
        ActivityLog activityLog = new ActivityLog();
        activityLog.setLogId(getUUId());
        activityLog.setEntityType(EntityEnum.TASK);
        activityLog.setEntityId(taskId);
        activityLog.setActionTimestamp(getCurrentTimestamp());
        activityLog.setOperation(operation);
        activityLog.setActor(actor);

        return  activityLog;
    }

    public ActivityLog addProjectUpdateLog(LogOperationEnum operation, String actor, String projectId, ProjectUpdateTypeEnum updateType, String previous, String updated){
        ActivityLog activityLog = new ActivityLog();
        activityLog.setLogId(getUUId());
        activityLog.setEntityType(EntityEnum.PROJECT);
        activityLog.setEntityId(projectId);
        activityLog.setActionTimestamp(getCurrentTimestamp());
        activityLog.setOperation(operation);
        activityLog.setUpdateType(updateType.toString());
        activityLog.setPreviousValue(previous);
        activityLog.setUpdatedvalue(updated);
        activityLog.setActor(actor);

        return  activityLog;
    }

    public ActivityLog addProjectAddorFlagLog(LogOperationEnum operation, String actor, String projectId){
        ActivityLog activityLog = new ActivityLog();
        activityLog.setLogId(getUUId());
        activityLog.setEntityType(EntityEnum.PROJECT);
        activityLog.setEntityId(projectId);
        activityLog.setActionTimestamp(getCurrentTimestamp());
        activityLog.setOperation(operation);
        activityLog.setActor(actor);

        return  activityLog;
    }
}
