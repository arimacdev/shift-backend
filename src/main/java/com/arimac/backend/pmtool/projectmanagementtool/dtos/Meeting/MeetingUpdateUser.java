package com.arimac.backend.pmtool.projectmanagementtool.dtos.Meeting;

import javax.validation.constraints.NotNull;
import java.util.List;

public class MeetingUpdateUser {
    @NotNull
    private boolean isUpdated;
    @NotNull
    private List<MeetingAttendee> attendees;

    public boolean getIsUpdated() {
        return isUpdated;
    }

    public void setIsUpdated(boolean updated) {
        isUpdated = updated;
    }

    public List<MeetingAttendee> getAttendees() {
        return attendees;
    }

    public void setAttendees(List<MeetingAttendee> attendees) {
        this.attendees = attendees;
    }
}
