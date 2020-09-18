package com.arimac.backend.pmtool.projectmanagementtool.repository;

import com.arimac.backend.pmtool.projectmanagementtool.dtos.Meeting.AddMinute;
import com.arimac.backend.pmtool.projectmanagementtool.model.Meeting;

public interface MeetingRepository {
    void addMeeting(Meeting meeting);
    Meeting getMeetingById(String meetingId, String projectId);
    void addDiscussionPoint(AddMinute addMinute);
}
