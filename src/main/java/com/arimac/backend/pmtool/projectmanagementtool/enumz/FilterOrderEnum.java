package com.arimac.backend.pmtool.projectmanagementtool.enumz;

import java.util.HashSet;
import java.util.Set;

public enum  FilterOrderEnum {
    ASC,
    DESC;

    private static Set<String> values = new HashSet<>();

    static {
        for (FilterOrderEnum orderEnum: FilterOrderEnum.values()){
            values.add(orderEnum.name());
        }
    }

    public static boolean contains(String value){
        return  values.contains(value);
    }
}
