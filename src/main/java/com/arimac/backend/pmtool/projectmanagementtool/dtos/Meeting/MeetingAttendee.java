package com.arimac.backend.pmtool.projectmanagementtool.dtos.Meeting;

import javax.validation.constraints.NotNull;

public class MeetingAttendee {
    @NotNull
    private String attendeeId;
    private boolean isGuest;

    public String getAttendeeId() {
        return attendeeId;
    }

    public void setAttendeeId(String attendeeId) {
        this.attendeeId = attendeeId;
    }

    public boolean getIsGuest() {
        return isGuest;
    }

    public void setIsGuest(boolean guest) {
        isGuest = guest;
    }
}
