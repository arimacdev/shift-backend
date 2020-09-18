package com.arimac.backend.pmtool.projectmanagementtool.model;

import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class Minute{
    private String minuteId;
    private String meetingId;
    private int discussionPoint;
    private String description;
    private String remarks;
    private String actionBy;
    private boolean actionByGuest;

    public Minute() {
    }

    public Minute(String minuteId, String meetingId, int discussionPoint, String description, String remarks, String actionBy, boolean actionByGuest) {
        this.minuteId = minuteId;
        this.meetingId = meetingId;
        this.discussionPoint = discussionPoint;
        this.description = description;
        this.remarks = remarks;
        this.actionBy = actionBy;
        this.actionByGuest = actionByGuest;
    }

    public String getMinuteId() {
        return minuteId;
    }

    public void setMinuteId(String minuteId) {
        this.minuteId = minuteId;
    }

    public String getMeetingId() {
        return meetingId;
    }

    public void setMeetingId(String meetingId) {
        this.meetingId = meetingId;
    }

    public int getDiscussionPoint() {
        return discussionPoint;
    }

    public void setDiscussionPoint(int discussionPoint) {
        this.discussionPoint = discussionPoint;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getActionBy() {
        return actionBy;
    }

    public void setActionBy(String actionBy) {
        this.actionBy = actionBy;
    }

    public boolean isActionByGuest() {
        return actionByGuest;
    }

    public void setActionByGuest(boolean actionByGuest) {
        this.actionByGuest = actionByGuest;
    }

}
