package com.arimac.backend.pmtool.projectmanagementtool.Service;

import com.arimac.backend.pmtool.projectmanagementtool.dtos.Meeting.AddMeeting;
import com.arimac.backend.pmtool.projectmanagementtool.dtos.Meeting.AddMinute;
import com.arimac.backend.pmtool.projectmanagementtool.dtos.Meeting.UpdateMeeting;
import com.arimac.backend.pmtool.projectmanagementtool.dtos.Meeting.UpdateMinute;
import com.arimac.backend.pmtool.projectmanagementtool.dtos.TaskDto;

public interface MeetingService {
    Object addMeeting(String userId, AddMeeting addMeeting);
    Object getMeetingsOfProject(String userId, String projectId, int startIndex, int endIndex);
    Object updateMeeting(String userId, String meetingId, UpdateMeeting updateMeeting);
    Object deleteMeeting(String userId, String meetingId, String projectId);
    Object addDiscussionPoint(String userId, AddMinute addMinute);
    Object updateDiscussionPoint(String userId, String meetingId, String discussionId, UpdateMinute updateMinute);
    Object deleteDiscussionPoint(String userId, String meetingId, String discussionId, String projectId);
    Object transitionToTask(String userId, String meetingId, String discussionId, TaskDto taskDto);
    Object getDiscussionPointOfMeeting(String userId, String meetingId, String projectId);
}
