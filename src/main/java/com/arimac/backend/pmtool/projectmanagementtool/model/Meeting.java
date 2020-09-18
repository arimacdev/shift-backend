package com.arimac.backend.pmtool.projectmanagementtool.model;

import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

public class Meeting implements RowMapper<Meeting> {
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


    public Meeting() {
    }

    public Meeting(String meetingId, String projectId, String meetingTopic, String meetingVenue, Timestamp meetingExpectedTime, Timestamp meetingActualTime, long expectedDuration, long actualDuration, Timestamp createdAt, String meetingCreatedBy) {
        this.meetingId = meetingId;
        this.projectId = projectId;
        this.meetingTopic = meetingTopic;
        this.meetingVenue = meetingVenue;
        this.meetingExpectedTime = meetingExpectedTime;
        this.meetingActualTime = meetingActualTime;
        this.expectedDuration = expectedDuration;
        this.actualDuration = actualDuration;
        this.createdAt = createdAt;
        this.meetingCreatedBy = meetingCreatedBy;
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

    @Override
    public Meeting mapRow(ResultSet resultSet, int i) throws SQLException {
        return new Meeting(
                resultSet.getString("meetingId"),
                resultSet.getString("projectId"),
                resultSet.getString("meetingTopic"),
                resultSet.getString("meetingVenue"),
                resultSet.getTimestamp("meetingExpectedTime"),
                resultSet.getTimestamp("meetingActualTime"),
                resultSet.getLong("expectedDuration"),
                resultSet.getLong("actualDuration"),
                resultSet.getTimestamp("createdAt"),
                resultSet.getString("meetingCreatedBy"));
    }

}
