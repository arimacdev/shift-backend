package com.arimac.backend.pmtool.projectmanagementtool.dtos.SupportProject;

import java.util.HashMap;

public enum SeverityEnum {
    LOW(1,"LOW"),
    MEDIUM(2, "MEDIUM"),
    HIGH(3,"HIGH"),
    HIGHER(4,"HIGHER");

    private final int severityId;
    private final String severityType;


    SeverityEnum(int severityId, String severityType) {
        this.severityId = severityId;
        this.severityType = severityType;
    }

    private static final HashMap<Integer, String> severityTypes = new HashMap<>();

    static {
        for (SeverityEnum severityEnum : SeverityEnum.values()){
            severityTypes.put(severityEnum.severityId, severityEnum.severityType);
        }
    }

    public static SeverityEnum getSeverity(int id){
        return SeverityEnum.valueOf(severityTypes.get(id));
    }

    public int getSeverityId(){
        return severityId;
    }
}
