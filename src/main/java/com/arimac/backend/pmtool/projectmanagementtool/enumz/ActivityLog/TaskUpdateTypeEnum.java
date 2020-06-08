package com.arimac.backend.pmtool.projectmanagementtool.enumz.ActivityLog;

import java.util.HashSet;
import java.util.Set;

public enum TaskUpdateTypeEnum {
    ASSIGNEE,
    ISSUE_TYPE,
    TASK_STATUS,
    TASK_NAME;

    private static Set<String> taskTypes = new HashSet<>();

    static {
        for (TaskUpdateTypeEnum taskUpdateType : TaskUpdateTypeEnum.values()){
            taskTypes.add(taskUpdateType.name());
        }
    }

    public static boolean contains(String updateType){
        return  taskTypes.contains(updateType);
    }

}
