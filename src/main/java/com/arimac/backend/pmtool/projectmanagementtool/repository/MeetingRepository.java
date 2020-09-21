package com.arimac.backend.pmtool.projectmanagementtool.repository;

import com.arimac.backend.pmtool.projectmanagementtool.dtos.Meeting.AddMinute;
import com.arimac.backend.pmtool.projectmanagementtool.dtos.Meeting.DiscussionPoint;
import com.arimac.backend.pmtool.projectmanagementtool.model.Meeting;
import com.arimac.backend.pmtool.projectmanagementtool.model.Minute;

import java.util.List;

public interface MeetingRepository {
    void addMeeting(Meeting meeting);
    Meeting getMeetingById(String meetingId, String projectId);
    void updateMeeting(Meeting meeting);
    void flagMeeting(String meetingId);
    void flagMeetingAssociatedDiscussionPoints(String meetingId);
    void addDiscussionPoint(Minute minute);
    Minute getDiscussionPoint(String discussionId);
    void updateDiscussionPoint(Minute minute);
    void flagDiscussionPoint(String discussionId);
    List<DiscussionPoint> getDiscussionPointOfMeeting(String meetingId);
}
