package com.arimac.backend.pmtool.projectmanagementtool.dtos.Meeting;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.sql.Timestamp;
import java.util.List;

public class UpdateMeeting {
    @NotNull
    private String projectId;
    private String meetingTopic;
    private String meetingVenue;
    private Timestamp meetingExpectedTime;
    private Timestamp meetingActualTime;
    @Min(value = 0L, message = "Expected duration must be positive")
    private Long expectedDuration;
    @Min(value = 0L, message = "Actual duration must be positive")
    private Long actualDuration;
//    @NotNull
    private MeetingUpdateUser meetingChaired;
//    @NotNull
    private MeetingUpdateUser meetingAttended;
//    @NotNull
    private MeetingUpdateUser meetingAbsent;
//    @NotNull
    private MeetingUpdateUser meetingCopiesTo;
//    @NotNull
    private MeetingUpdateUser meetingPrepared;

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

    public Long getExpectedDuration() {
        return expectedDuration;
    }

    public void setExpectedDuration(Long expectedDuration) {
        this.expectedDuration = expectedDuration;
    }

    public Long getActualDuration() {
        return actualDuration;
    }

    public void setActualDuration(Long actualDuration) {
        this.actualDuration = actualDuration;
    }

    public MeetingUpdateUser getMeetingChaired() {
        return meetingChaired;
    }

    public void setMeetingChaired(MeetingUpdateUser meetingChaired) {
        this.meetingChaired = meetingChaired;
    }

    public MeetingUpdateUser getMeetingAttended() {
        return meetingAttended;
    }

    public void setMeetingAttended(MeetingUpdateUser meetingAttended) {
        this.meetingAttended = meetingAttended;
    }

    public MeetingUpdateUser getMeetingAbsent() {
        return meetingAbsent;
    }

    public void setMeetingAbsent(MeetingUpdateUser meetingAbsent) {
        this.meetingAbsent = meetingAbsent;
    }

    public MeetingUpdateUser getMeetingCopiesTo() {
        return meetingCopiesTo;
    }

    public void setMeetingCopiesTo(MeetingUpdateUser meetingCopiesTo) {
        this.meetingCopiesTo = meetingCopiesTo;
    }

    public MeetingUpdateUser getMeetingPrepared() {
        return meetingPrepared;
    }

    public void setMeetingPrepared(MeetingUpdateUser meetingPrepared) {
        this.meetingPrepared = meetingPrepared;
    }

    @Override
    public String toString() {
        return "UpdateMeeting{" +
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
