package com.arimac.backend.pmtool.projectmanagementtool.dtos.Meeting;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.sql.Timestamp;

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
                '}';
    }
}
