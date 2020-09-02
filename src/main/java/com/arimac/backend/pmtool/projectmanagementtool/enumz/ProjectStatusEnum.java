package com.arimac.backend.pmtool.projectmanagementtool.enumz;

import java.util.HashSet;
import java.util.Set;

public enum  ProjectStatusEnum {
    presales,
    presalesPD,
    preSalesQS,
    preSalesN,
    preSalesC,
    preSalesL,
    ongoing,
    support,
    finished;

    private static Set<String> values = new HashSet<>();

    static {
        for (ProjectStatusEnum orderEnum: ProjectStatusEnum.values()){
            values.add(orderEnum.name());
        }
    }

    public static boolean contains(String value){
        return  values.contains(value);
    }
}
