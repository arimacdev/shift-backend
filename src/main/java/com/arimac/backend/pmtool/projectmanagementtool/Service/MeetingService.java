package com.arimac.backend.pmtool.projectmanagementtool.Service;

import com.arimac.backend.pmtool.projectmanagementtool.dtos.Meeting.AddMeeting;

public interface MeetingService {
    Object addMeeting(String userId, AddMeeting addMeeting);
}
