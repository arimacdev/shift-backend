package com.arimac.backend.pmtool.projectmanagementtool.model;

import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

public class Minute implements RowMapper<Minute> {
    private String minuteId;
    private String meetingId;
    private int discussionPoint;
    private String description;
    private String remarks;
    private String actionBy;
    private boolean actionByGuest;
    private String addedBy;
    private Timestamp dueDate;
    private boolean isDeleted;

    public Minute() {
    }

    public Minute(String minuteId, String meetingId, int discussionPoint, String description, String remarks, String actionBy, boolean actionByGuest, String addedBy, Timestamp dueDate, boolean isDeleted) {
        this.minuteId = minuteId;
        this.meetingId = meetingId;
        this.discussionPoint = discussionPoint;
        this.description = description;
        this.remarks = remarks;
        this.actionBy = actionBy;
        this.actionByGuest = actionByGuest;
        this.addedBy = addedBy;
        this.dueDate = dueDate;
        this.isDeleted = isDeleted;
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

    public String getAddedBy() {
        return addedBy;
    }

    public void setAddedBy(String addedBy) {
        this.addedBy = addedBy;
    }

    public Timestamp getDueDate() {
        return dueDate;
    }

    public void setDueDate(Timestamp dueDate) {
        this.dueDate = dueDate;
    }

    public boolean getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(boolean deleted) {
        isDeleted = deleted;
    }

    @Override
    public Minute mapRow(ResultSet resultSet, int i) throws SQLException {
        return new Minute(
                resultSet.getString("minuteId"),
                resultSet.getString("meetingId"),
                resultSet.getInt("discussionPoint"),
                resultSet.getString("description"),
                resultSet.getString("remarks"),
                resultSet.getString("actionBy"),
                resultSet.getBoolean("actionByGuest"),
                resultSet.getString("addedBy"),
                resultSet.getTimestamp("dueDate"),
                resultSet.getBoolean("isDeleted")
                );
    }
}

