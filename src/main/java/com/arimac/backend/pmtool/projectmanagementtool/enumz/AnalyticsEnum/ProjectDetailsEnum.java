package com.arimac.backend.pmtool.projectmanagementtool.enumz.AnalyticsEnum;

import com.arimac.backend.pmtool.projectmanagementtool.enumz.ProjectStatusEnum;

import java.util.HashSet;
import java.util.Set;

public enum ProjectDetailsEnum {
    projectName,
    projectStartDate,
    projectStatus,
    taskcount,
    memberCount,
    timeTaken;

    private static final Set<String> values = new HashSet<>();

    static {
        for (ProjectDetailsEnum detailsEnum: ProjectDetailsEnum.values()){
            values.add(detailsEnum.name());
        }
    }

    public static boolean contains(String value){
        return  values.contains(value);
    }
}
