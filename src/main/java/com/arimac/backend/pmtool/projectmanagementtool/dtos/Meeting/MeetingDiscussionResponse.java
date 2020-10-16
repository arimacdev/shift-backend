package com.arimac.backend.pmtool.projectmanagementtool.dtos.Meeting;

import java.util.ArrayList;
import java.util.List;

public class MeetingDiscussionResponse {
    private MeetingResponse meeting;
    private List<DiscussionPoint> discussionPoints;


    public MeetingDiscussionResponse(MeetingResponse meeting, List<DiscussionPoint> discussionPoints) {
        this.meeting = meeting;
        this.discussionPoints = discussionPoints;
    }

    public MeetingResponse getMeeting() {
        return meeting;
    }

    public void setMeeting(MeetingResponse meeting) {
        this.meeting = meeting;
    }

    public List<DiscussionPoint> getDiscussionPoints() {
        return discussionPoints;
    }

    public void setDiscussionPoints(List<DiscussionPoint> discussionPoints) {
        this.discussionPoints = discussionPoints;
    }
}
