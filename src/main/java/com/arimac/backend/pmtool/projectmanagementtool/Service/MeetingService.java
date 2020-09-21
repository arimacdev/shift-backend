package com.arimac.backend.pmtool.projectmanagementtool.Service;

import com.arimac.backend.pmtool.projectmanagementtool.dtos.Meeting.AddMeeting;
import com.arimac.backend.pmtool.projectmanagementtool.dtos.Meeting.AddMinute;
import com.arimac.backend.pmtool.projectmanagementtool.dtos.Meeting.UpdateMeeting;
import com.arimac.backend.pmtool.projectmanagementtool.dtos.Meeting.UpdateMinute;

public interface MeetingService {
    Object addMeeting(String userId, AddMeeting addMeeting);
    Object updateMeeting(String userId, String meetingId, UpdateMeeting updateMeeting);
    Object deleteMeeting(String userId, String meetingId, String projectId);
    Object addDiscussionPoint(String userId, AddMinute addMinute);
    Object updateDiscussionPoint(String userId, String meetingId, String discussionId, UpdateMinute updateMinute);
    Object deleteDiscussionPoint(String userId, String meetingId, String discussionId, String projectId);
    Object getDiscussionPointOfMeeting(String userId, String meetingId, String projectId);
}
