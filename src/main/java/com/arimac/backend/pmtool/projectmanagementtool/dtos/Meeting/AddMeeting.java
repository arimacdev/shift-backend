package com.arimac.backend.pmtool.projectmanagementtool.dtos.Meeting;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.sql.Timestamp;
import java.util.List;

public class AddMeeting {
    @NotNull
    private String projectId;
    @NotNull
    private String meetingTopic;
    @NotNull
    private String meetingVenue;
    private Timestamp meetingExpectedTime;
    private Timestamp meetingActualTime;
    @Min(value = 0L, message = "Expected duration must be positive")
    private long expectedDuration;
    @Min(value = 0L, message = "Actual duration must be positive")
    private long actualDuration;
    @NotNull
    private List<MeetingAttendee> meetingChaired;
    @NotNull
    private List<MeetingAttendee> meetingAttended;
    @NotNull
    private List<MeetingAttendee> meetingAbsent;
    @NotNull
    private List<MeetingAttendee> meetingCopiesTo;
    @NotNull
    private List<MeetingAttendee> meetingPrepared;


    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    public String getMeetingTopic() {
        return meetingTopic;
    }

    public void setMeetingTopic(String meetingTopic) {
        this.meetingTopic = meetingTopic;
    }

    public String getMeetingVenue() {
        return meetingVenue;
    }

    public void setMeetingVenue(String meetingVenue) {
        this.meetingVenue = meetingVenue;
    }

    public Timestamp getMeetingExpectedTime() {
        return meetingExpectedTime;
    }

    public void setMeetingExpectedTime(Timestamp meetingExpectedTime) {
        this.meetingExpectedTime = meetingExpectedTime;
    }

    public Timestamp getMeetingActualTime() {
        return meetingActualTime;
    }

    public void setMeetingActualTime(Timestamp meetingActualTime) {
        this.meetingActualTime = meetingActualTime;
    }

    public long getExpectedDuration() {
        return expectedDuration;
    }

    public void setExpectedDuration(long expectedDuration) {
        this.expectedDuration = expectedDuration;
    }

    public long getActualDuration() {
        return actualDuration;
    }

    public void setActualDuration(long actualDuration) {
        this.actualDuration = actualDuration;
    }

    public List<MeetingAttendee> getMeetingChaired() {
        return meetingChaired;
    }

    public void setMeetingChaired(List<MeetingAttendee> meetingChaired) {
        this.meetingChaired = meetingChaired;
    }

    public List<MeetingAttendee> getMeetingAttended() {
        return meetingAttended;
    }

    public void setMeetingAttended(List<MeetingAttendee> meetingAttended) {
        this.meetingAttended = meetingAttended;
    }

    public List<MeetingAttendee> getMeetingAbsent() {
        return meetingAbsent;
    }

    public void setMeetingAbsent(List<MeetingAttendee> meetingAbsent) {
        this.meetingAbsent = meetingAbsent;
    }

    public List<MeetingAttendee> getMeetingCopiesTo() {
        return meetingCopiesTo;
    }

    public void setMeetingCopiesTo(List<MeetingAttendee> meetingCopiesTo) {
        this.meetingCopiesTo = meetingCopiesTo;
    }

    public List<MeetingAttendee> getMeetingPrepared() {
        return meetingPrepared;
    }

    public void setMeetingPrepared(List<MeetingAttendee> meetingPrepared) {
        this.meetingPrepared = meetingPrepared;
    }

    @Override
    public String toString() {
        return "AddMeeting{" +
                "projectId='" + projectId + '\'' +
                ", meetingTopic='" + meetingTopic + '\'' +
                ", meetingVenue='" + meetingVenue + '\'' +
                ", meetingExpectedTime=" + meetingExpectedTime +
                ", meetingActualTime=" + meetingActualTime +
                ", expectedDuration=" + expectedDuration +
                ", actualDuration=" + actualDuration +
                ", meetingChaired=" + meetingChaired +
                ", meetingAttended=" + meetingAttended +
                ", meetingAbsent=" + meetingAbsent +
                ", meetingCopiesTo=" + meetingCopiesTo +
                ", meetingPrepared=" + meetingPrepared +
                '}';
    }
}
