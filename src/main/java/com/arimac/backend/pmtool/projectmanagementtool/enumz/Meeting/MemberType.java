package com.arimac.backend.pmtool.projectmanagementtool.enumz.Meeting;

public enum  MemberType {
    CHAIRED("CHAIRED", 1),
    ATTENDED("ATTENDED",2),
    ABSENT("ABSENT",3),
    SEND_COPIES("SEND_COPIES",4),
    MINUTES_PREPARED("MINUTES_PREPARED", 5);


    private final String memberType;
    private final int memberTypeId;

    MemberType(String memberType, int memberTypeId){
        this.memberType = memberType;
        this.memberTypeId = memberTypeId;
    }

    public String getEntity() {
        return memberType;
    }

    public int getEntityId() {
        return memberTypeId;
    }

}