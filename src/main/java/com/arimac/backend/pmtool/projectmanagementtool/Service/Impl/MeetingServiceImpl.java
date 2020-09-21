package com.arimac.backend.pmtool.projectmanagementtool.Service.Impl;

import com.arimac.backend.pmtool.projectmanagementtool.Response.Response;
import com.arimac.backend.pmtool.projectmanagementtool.Service.MeetingService;
import com.arimac.backend.pmtool.projectmanagementtool.dtos.Meeting.AddMeeting;
import com.arimac.backend.pmtool.projectmanagementtool.dtos.Meeting.AddMinute;
import com.arimac.backend.pmtool.projectmanagementtool.enumz.ResponseMessage;
import com.arimac.backend.pmtool.projectmanagementtool.exception.ErrorMessage;
import com.arimac.backend.pmtool.projectmanagementtool.model.Meeting;
import com.arimac.backend.pmtool.projectmanagementtool.model.Minute;
import com.arimac.backend.pmtool.projectmanagementtool.model.Project_User;
import com.arimac.backend.pmtool.projectmanagementtool.model.User;
import com.arimac.backend.pmtool.projectmanagementtool.repository.MeetingRepository;
import com.arimac.backend.pmtool.projectmanagementtool.repository.ProjectRepository;
import com.arimac.backend.pmtool.projectmanagementtool.repository.UserRepository;
import com.arimac.backend.pmtool.projectmanagementtool.utils.UtilsService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MeetingServiceImpl implements MeetingService {

    private final MeetingRepository meetingRepository;
    private final UserRepository userRepository;
    private final ProjectRepository projectRepository;
    private final UtilsService utilsService;

    public MeetingServiceImpl(MeetingRepository meetingRepository, UserRepository userRepository, ProjectRepository projectRepository, UtilsService utilsService) {
        this.meetingRepository = meetingRepository;
        this.userRepository = userRepository;
        this.projectRepository = projectRepository;
        this.utilsService = utilsService;
    }

    @Override
    public Object addMeeting(String userId, AddMeeting addMeeting) {
        User user = userRepository.getUserByUserId(userId);
        if (user == null)
            return new ErrorMessage(ResponseMessage.USER_NOT_FOUND, HttpStatus.NOT_FOUND);
        Project_User  project_user = projectRepository.getProjectUser(addMeeting.getProjectId(), userId);
        if (project_user == null)
            return new ErrorMessage(ResponseMessage.USER_NOT_MEMBER, HttpStatus.NOT_FOUND);
        Meeting meeting = new Meeting();
        meeting.setMeetingId(utilsService.getUUId());
        meeting.setProjectId(addMeeting.getProjectId());
        meeting.setExpectedDuration(addMeeting.getExpectedDuration());
        meeting.setActualDuration(addMeeting.getActualDuration());
        meeting.setMeetingActualTime(addMeeting.getMeetingActualTime());
        meeting.setMeetingExpectedTime(addMeeting.getMeetingExpectedTime());
        meeting.setMeetingTopic(addMeeting.getMeetingTopic());
        meeting.setMeetingVenue(addMeeting.getMeetingVenue());
        meeting.setMeetingCreatedBy(userId);
        meeting.setCreatedAt(utilsService.getCurrentTimestamp());
        meeting.setIsDeleted(false);
        meetingRepository.addMeeting(meeting);

        return new Response(ResponseMessage.SUCCESS, HttpStatus.OK);
    }

    @Override
    public Object addDiscussionPoint(String userId, AddMinute addMinute) {
        User user = userRepository.getUserByUserId(userId);
        if (user == null)
            return new ErrorMessage(ResponseMessage.USER_NOT_MEMBER, HttpStatus.NOT_FOUND);
        Meeting meeting = meetingRepository.getMeetingById(addMinute.getMeetingId(), addMinute.getProjectId());
        if (meeting == null)
            return new ErrorMessage(ResponseMessage.MEETING_NOT_FOUND, HttpStatus.NOT_FOUND);
        Project_User project_user = projectRepository.getProjectUser(addMinute.getProjectId(), userId);
        if (project_user == null)
            return new ErrorMessage(ResponseMessage.USER_NOT_MEMBER, HttpStatus.NOT_FOUND);
        Minute minute = new Minute();
        minute.setMeetingId(addMinute.getMeetingId());
        minute.setMinuteId(utilsService.getUUId());
        minute.setAddedBy(userId);
        minute.setRemarks(addMinute.getRemarks());
        minute.setActionBy(addMinute.getActionBy());
        minute.setDescription(addMinute.getDescription());
        minute.setDiscussionPoint(addMinute.getDiscussionPoint());
        minute.setActionByGuest(addMinute.isActionByGuest());
        minute.setIsDeleted(false);
        meetingRepository.addDiscussionPoint(minute);

        return new Response(ResponseMessage.SUCCESS, HttpStatus.OK);
    }

    @Override
    public Object getDiscussionPointOfMeeting(String userId, String meetingId, String projectId) {
        User user = userRepository.getUserByUserId(userId);
        if (user == null)
            return new ErrorMessage(ResponseMessage.USER_NOT_FOUND, HttpStatus.NOT_FOUND);
        Project_User project_user = projectRepository.getProjectUser(projectId, userId);
        if (project_user == null)
            return new ErrorMessage(ResponseMessage.USER_NOT_MEMBER, HttpStatus.NOT_FOUND);
        Meeting meeting = meetingRepository.getMeetingById(meetingId, projectId);
        if (meeting == null)
            return new ErrorMessage(ResponseMessage.MEETING_NOT_FOUND, HttpStatus.NOT_FOUND);
        return new Response(ResponseMessage.SUCCESS, HttpStatus.OK, meetingRepository.getDiscussionPointOfMeeting(meetingId));
    }
}
