package com.arimac.backend.pmtool.projectmanagementtool.repository;

import com.arimac.backend.pmtool.projectmanagementtool.dtos.Meeting.AddMinute;
import com.arimac.backend.pmtool.projectmanagementtool.dtos.Meeting.DiscussionPoint;
import com.arimac.backend.pmtool.projectmanagementtool.dtos.Meeting.MeetingResponse;
import com.arimac.backend.pmtool.projectmanagementtool.model.Meeting;
import com.arimac.backend.pmtool.projectmanagementtool.model.Meeting_Attendee;
import com.arimac.backend.pmtool.projectmanagementtool.model.Minute;

import java.util.HashMap;
import java.util.List;

public interface MeetingRepository {
    void addMeeting(Meeting meeting);
    void addMeetingAttendee(Meeting_Attendee meeting_attendee);
    Meeting getMeetingById(String meetingId, String projectId);
    HashMap<String, MeetingResponse> getMeetingsOfProject(String projectId, int startIndex, int endIndex);
    void updateMeeting(Meeting meeting);
    void flagMeeting(String meetingId);
    void flagMeetingAssociatedDiscussionPoints(String meetingId);
    void addDiscussionPoint(Minute minute);
    Minute getDiscussionPoint(String discussionId);
    void updateDiscussionPoint(Minute minute);
    void flagDiscussionPoint(String discussionId);
    List<DiscussionPoint> getDiscussionPointOfMeeting(String meetingId);
}
