package com.arimac.backend.pmtool.projectmanagementtool.repository;

import com.arimac.backend.pmtool.projectmanagementtool.dtos.Meeting.AddMinute;
import com.arimac.backend.pmtool.projectmanagementtool.dtos.Meeting.DiscussionPoint;
import com.arimac.backend.pmtool.projectmanagementtool.model.Meeting;
import com.arimac.backend.pmtool.projectmanagementtool.model.Minute;

import java.util.List;

public interface MeetingRepository {
    void addMeeting(Meeting meeting);
    Meeting getMeetingById(String meetingId, String projectId);
    void addDiscussionPoint(Minute minute);
    List<DiscussionPoint> getDiscussionPointOfMeeting(String meetingId);
}
