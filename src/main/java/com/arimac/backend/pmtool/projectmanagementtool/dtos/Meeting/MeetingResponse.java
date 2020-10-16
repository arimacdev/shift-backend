package com.arimac.backend.pmtool.projectmanagementtool.dtos.Meeting;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class MeetingResponse {
    private String meetingId;
    private String projectId;
    private String meetingTopic;
    private String meetingVenue;
    private Timestamp meetingExpectedTime;
    private Timestamp meetingActualTime;
    private long expectedDuration;
    private long actualDuration;
    private Timestamp createdAt;
    private String meetingCreatedBy;

    private List<MeetingResponseUser> meetingChaired;
    private List<MeetingResponseUser> meetingAttended;
    private List<MeetingResponseUser> meetingAbsent;
    private List<MeetingResponseUser> meetingCopiesTo;
    private List<MeetingResponseUser> meetingPrepared;

    public MeetingResponse() {
        this.meetingChaired = new ArrayList<>();
        this.meetingAttended = new ArrayList<>();
        this.meetingAbsent = new ArrayList<>();
        this.meetingCopiesTo = new ArrayList<>();
        this.meetingPrepared = new ArrayList<>();
    }

    public String getMeetingId() {
        return meetingId;
    }

    public void setMeetingId(String meetingId) {
        this.meetingId = meetingId;
    }

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

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    public String getMeetingCreatedBy() {
        return meetingCreatedBy;
    }

    public void setMeetingCreatedBy(String meetingCreatedBy) {
        this.meetingCreatedBy = meetingCreatedBy;
    }

    public List<MeetingResponseUser> getMeetingChaired() {
        return meetingChaired;
    }

    public void setMeetingChaired(List<MeetingResponseUser> meetingChaired) {
        this.meetingChaired = meetingChaired;
    }

    public List<MeetingResponseUser> getMeetingAttended() {
        return meetingAttended;
    }

    public void setMeetingAttended(List<MeetingResponseUser> meetingAttended) {
        this.meetingAttended = meetingAttended;
    }

    public List<MeetingResponseUser> getMeetingAbsent() {
        return meetingAbsent;
    }

    public void setMeetingAbsent(List<MeetingResponseUser> meetingAbsent) {
        this.meetingAbsent = meetingAbsent;
    }

    public List<MeetingResponseUser> getMeetingCopiesTo() {
        return meetingCopiesTo;
    }

    public void setMeetingCopiesTo(List<MeetingResponseUser> meetingCopiesTo) {
        this.meetingCopiesTo = meetingCopiesTo;
    }

    public List<MeetingResponseUser> getMeetingPrepared() {
        return meetingPrepared;
    }

    public void setMeetingPrepared(List<MeetingResponseUser> meetingPrepared) {
        this.meetingPrepared = meetingPrepared;
    }
}
