package com.arimac.backend.pmtool.projectmanagementtool.enumz;

import java.util.HashSet;
import java.util.Set;

public enum  FilterQueryTypeEnum {
    projectId,
    projectName,
    taskAssignee,
    issueType,
    taskDueDateAt,
    taskName,
    taskCreatedAt,
    taskStatus;

    private static Set<String> values = new HashSet<>();

    static {
        for (FilterQueryTypeEnum typeEnum: FilterQueryTypeEnum.values()){
            values.add(typeEnum.name());
        }
    }

    public static boolean contains(String value){
        return  values.contains(value);
    }
}
