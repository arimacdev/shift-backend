package com.arimac.backend.pmtool.projectmanagementtool.enumz;

import java.util.HashSet;
import java.util.Set;

public enum  FilterQueryOperatorEnum {
    IN,
    AND,
    BETWEEN,
    LIKE;

    private static Set<String> values = new HashSet<>();

    static {
        for (FilterQueryOperatorEnum typeEnum: FilterQueryOperatorEnum.values()){
            values.add(typeEnum.name());
        }
    }

    public static boolean contains(String value){
        return  values.contains(value);
    }
}
