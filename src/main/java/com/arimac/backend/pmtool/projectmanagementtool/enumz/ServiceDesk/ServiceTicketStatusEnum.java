package com.arimac.backend.pmtool.projectmanagementtool.enumz.ServiceDesk;

import java.util.HashMap;

public enum ServiceTicketStatusEnum {
    CLOSED(0,"CLOSED"),
    PENDING(1, "PENDING"),
    ACKNOWLEDGED(2,"ACKNOWLEDGED"),
    FIXED(3,"FIXED"),
    SOLVED(4, "SOLVED"),
    REOPEN(5, "REOPEN");

    private final int statusId;
    private final String statusType;


    ServiceTicketStatusEnum(int statusId, String statusType) {
        this.statusId = statusId;
        this.statusType = statusType;
    }

    private static final HashMap<Integer, String> ticketTypes = new HashMap<>();

    static {
        for (ServiceTicketStatusEnum ticketStatus : ServiceTicketStatusEnum.values()){
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
