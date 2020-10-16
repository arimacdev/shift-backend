package com.arimac.backend.pmtool.projectmanagementtool.enumz.ServiceDesk;

import com.arimac.backend.pmtool.projectmanagementtool.enumz.Meeting.MemberType;

import java.util.HashMap;

public enum TicketStatusEnum {
    CLOSED(0,"CLOSED"),
    PENDING(1, "PENDING"),
    ACKNOWLEDGED(2,"ACKNOWLEDGED"),
    FIXED(3,"FIXED"),
    SOLVED(4, "SOLVED");

    private final int statusId;
    private final String statusType;


    TicketStatusEnum(int statusId, String statusType) {
        this.statusId = statusId;
        this.statusType = statusType;
    }

    private static final HashMap<Integer, String> ticketTypes = new HashMap<>();

    static {
        for (TicketStatusEnum ticketStatus : TicketStatusEnum.values()){
            ticketTypes.put(ticketStatus.statusId, ticketStatus.statusType);
        }
    }

    public static String getTicketStatus(int id){
        return ticketTypes.get(id);
    }

    public int getStatusId(){
        return statusId;
    }

}
