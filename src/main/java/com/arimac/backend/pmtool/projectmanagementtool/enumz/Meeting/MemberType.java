package com.arimac.backend.pmtool.projectmanagementtool.enumz.Meeting;

import com.arimac.backend.pmtool.projectmanagementtool.enumz.ActivityLog.TaskUpdateTypeEnum;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public enum  MemberType {
    CHAIRED("CHAIRED", 1),
    ATTENDED("ATTENDED",2),
    ABSENT("ABSENT",3),
    SEND_COPIES("SEND_COPIES",4),
    MINUTES_PREPARED("MINUTES_PREPARED", 5);


    private final String memberType;
    private final int memberTypeId;

    private static final HashMap<Integer, String> memberTypes = new HashMap<>();

    static {
        for (MemberType memberType : MemberType.values()){
            memberTypes.put(memberType.memberTypeId, memberType.memberType);
        }
    }

    MemberType(String memberType, int memberTypeId){
        this.memberType = memberType;
        this.memberTypeId = memberTypeId;
    }

    public static String getMemberType(int id){
        return memberTypes.get(id);
    }

    public String getEntity() {
        return memberType;
    }

    public int getEntityId() {
        return memberTypeId;
    }

    public static boolean contains(String value){
        return  true;
    }

}