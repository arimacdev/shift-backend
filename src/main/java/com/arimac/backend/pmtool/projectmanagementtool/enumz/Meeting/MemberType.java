package com.arimac.backend.pmtool.projectmanagementtool.enumz.Meeting;

public enum  MemberType {
    ATTENDED("ATTENDED",1),
    ABSENT("ABSENT",2),
    SEND_COPIES("SEND_COPIES",3);


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