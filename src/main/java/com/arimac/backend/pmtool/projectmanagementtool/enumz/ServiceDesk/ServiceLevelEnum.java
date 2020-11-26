package com.arimac.backend.pmtool.projectmanagementtool.enumz.ServiceDesk;

import java.util.HashMap;

public enum  ServiceLevelEnum {
    LOW(1,"LOW"),
    MEDIUM(2, "MEDIUM"),
    HIGH(3,"HIGH"),
    HIGHER(4,"HIGHER");

    private final int serviceLevelId;
    private final String serviceLevel;


    ServiceLevelEnum(int serviceLevelId, String serviceLevel) {
        this.serviceLevelId = serviceLevelId;
        this.serviceLevel = serviceLevel;
    }

    private static final HashMap<Integer, String> serviceLevelTypes = new HashMap<>();

    static {
        for (ServiceLevelEnum serviceLevelEnum : ServiceLevelEnum.values()){
            serviceLevelTypes.put(serviceLevelEnum.serviceLevelId, serviceLevelEnum.serviceLevel);
        }
    }

    public static ServiceLevelEnum getServiceLevel(int id){
        return ServiceLevelEnum.valueOf(serviceLevelTypes.get(id));
    }

    public int getServiceLevelId(){
        return serviceLevelId;
    }
}
