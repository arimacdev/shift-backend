package com.arimac.backend.pmtool.projectmanagementtool.dtos.Meeting;

import javax.validation.constraints.AssertFalse;
import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.NotNull;

public class AddMinute {
    @NotNull
    private String meetingId;
    @NotNull
    private int discussionPoint;
    @NotNull
    private String projectId;
    @NotNull
    private String description;
    @NotNull
    private String remarks;
    @NotNull
    private String actionBy;
    @NotNull
    private boolean actionByGuest;
    @NotNull
    private String addedBy;

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    public int getDiscussionPoint() {
        return discussionPoint;
    }

    public void setDiscussionPoint(int discussionPoint) {
        this.discussionPoint = discussionPoint;
    }

    public String getMeetingId() {
        return meetingId;
    }

    public void setMeetingId(String meetingId) {
        this.meetingId = meetingId;
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

    @Override
    public String toString() {
        return "AddMinute{" +
                "meetingId='" + meetingId + '\'' +
                ", description='" + description + '\'' +
                ", remarks='" + remarks + '\'' +
                ", actionBy='" + actionBy + '\'' +
                ", actionByGuest=" + actionByGuest +
                ", addedBy='" + addedBy + '\'' +
                '}';
    }
}
