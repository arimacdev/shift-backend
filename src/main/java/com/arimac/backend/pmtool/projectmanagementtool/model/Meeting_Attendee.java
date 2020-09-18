package com.arimac.backend.pmtool.projectmanagementtool.model;

import com.arimac.backend.pmtool.projectmanagementtool.enumz.Meeting.MemberType;

public class Meeting_Attendee {
    private String meetingId;
    private String attendeeId;
    private boolean isGuest;
    private int memberType;

    public Meeting_Attendee() {
    }

    public Meeting_Attendee(String meetingId, String attendeeId, boolean isGuest, int memberType) {
        this.meetingId = meetingId;
        this.attendeeId = attendeeId;
        this.isGuest = isGuest;
        this.memberType = memberType;
    }

    public String getMeetingId() {
        return meetingId;
    }

    public void setMeetingId(String meetingId) {
        this.meetingId = meetingId;
    }

    public String getAttendeeId() {
        return attendeeId;
    }

    public void setAttendeeId(String attendeeId) {
        this.attendeeId = attendeeId;
    }

    public boolean isGuest() {
        return isGuest;
    }

    public void setGuest(boolean guest) {
        isGuest = guest;
    }

    public int getMemberType() {
        return memberType;
    }

    public void setMemberType(int memberType) {
        this.memberType = memberType;
    }
}
