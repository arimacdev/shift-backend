package com.arimac.backend.pmtool.projectmanagementtool.Service;

import com.arimac.backend.pmtool.projectmanagementtool.dtos.Meeting.AddMeeting;
import com.arimac.backend.pmtool.projectmanagementtool.dtos.Meeting.AddMinute;

public interface MeetingService {
    Object addMeeting(String userId, AddMeeting addMeeting);
    Object addDiscussionPoint(String userId, AddMinute addMinute);
}
