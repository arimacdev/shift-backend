package com.arimac.backend.pmtool.projectmanagementtool.Service.Impl;

import com.arimac.backend.pmtool.projectmanagementtool.Response.Response;
import com.arimac.backend.pmtool.projectmanagementtool.Service.MeetingService;
import com.arimac.backend.pmtool.projectmanagementtool.Service.TaskService;
import com.arimac.backend.pmtool.projectmanagementtool.dtos.Meeting.*;
import com.arimac.backend.pmtool.projectmanagementtool.dtos.TaskDto;
import com.arimac.backend.pmtool.projectmanagementtool.enumz.Meeting.MemberType;
import com.arimac.backend.pmtool.projectmanagementtool.enumz.ResponseMessage;
import com.arimac.backend.pmtool.projectmanagementtool.exception.ErrorMessage;
import com.arimac.backend.pmtool.projectmanagementtool.model.*;
import com.arimac.backend.pmtool.projectmanagementtool.repository.MeetingRepository;
import com.arimac.backend.pmtool.projectmanagementtool.repository.ProjectRepository;
import com.arimac.backend.pmtool.projectmanagementtool.repository.UserRepository;
import com.arimac.backend.pmtool.projectmanagementtool.utils.UtilsService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

@Service
public class MeetingServiceImpl implements MeetingService {

    private final MeetingRepository meetingRepository;
    private final UserRepository userRepository;
    private final ProjectRepository projectRepository;
    private final TaskService taskService;
    private final UtilsService utilsService;

    public MeetingServiceImpl(MeetingRepository meetingRepository, UserRepository userRepository, ProjectRepository projectRepository, TaskService taskService, UtilsService utilsService) {
        this.meetingRepository = meetingRepository;
        this.userRepository = userRepository;
        this.projectRepository = projectRepository;
        this.taskService = taskService;
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

        if (!addMeeting.getMeetingAttended().isEmpty())
           addMeetingAttendees(addMeeting.getMeetingAttended(), meeting.getMeetingId(), MemberType.ATTENDED.getEntityId());
        if (!addMeeting.getMeetingChaired().isEmpty())
            addMeetingAttendees(addMeeting.getMeetingChaired(), meeting.getMeetingId(), MemberType.CHAIRED.getEntityId());
        if (!addMeeting.getMeetingAbsent().isEmpty())
            addMeetingAttendees(addMeeting.getMeetingAbsent(), meeting.getMeetingId(), MemberType.ABSENT.getEntityId());
        if (!addMeeting.getMeetingCopiesTo().isEmpty())
            addMeetingAttendees(addMeeting.getMeetingCopiesTo(), meeting.getMeetingId(), MemberType.SEND_COPIES.getEntityId());
        if (!addMeeting.getMeetingPrepared().isEmpty())
            addMeetingAttendees(addMeeting.getMeetingPrepared(), meeting.getMeetingId(), MemberType.MINUTES_PREPARED.getEntityId());

        return new Response(ResponseMessage.SUCCESS, HttpStatus.OK);
    }

    private void addMeetingAttendees(List<MeetingAttendee> attendees, String meetingId, int memberType){
        for(MeetingAttendee meetingAttendee : attendees) {
            if (meetingAttendee.getAttendeeId() != null) {
                Meeting_Attendee meeting_attendee = new Meeting_Attendee();
                meeting_attendee.setAttendeeId(meetingAttendee.getAttendeeId());
                meeting_attendee.setMeetingId(meetingId);
                meeting_attendee.setGuest(meetingAttendee.getIsGuest());
                meeting_attendee.setMemberType(memberType);
                meetingRepository.addMeetingAttendee(meeting_attendee);
            }
        }
    }

    @Override
    public Object getMeetingsOfProject(String userId, String projectId, int startIndex, int endIndex, boolean filter, String filterKey, String filterDate) {
        if (filter && !filterDate.isEmpty()){
                DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                try {
                    dateFormat.parse(filterDate);
                } catch (ParseException e) {
                    return new ErrorMessage(ResponseMessage.INVALID_DATE_FORMAT, HttpStatus.BAD_REQUEST);
                }
        }
        User user = userRepository.getUserByUserId(userId);
        if (user == null)
            return new ErrorMessage(ResponseMessage.USER_NOT_FOUND, HttpStatus.NOT_FOUND);
        Project_User  project_user = projectRepository.getProjectUser(projectId, userId);
        if (project_user == null)
            return new ErrorMessage(ResponseMessage.USER_NOT_MEMBER, HttpStatus.NOT_FOUND);
        if ((endIndex - startIndex) > 10)
            return new ErrorMessage(ResponseMessage.REQUEST_ITEM_LIMIT_EXCEEDED, HttpStatus.UNPROCESSABLE_ENTITY);
        return new Response(ResponseMessage.SUCCESS, HttpStatus.OK, new ArrayList<>(meetingRepository.getMeetingsOfProject(projectId, startIndex, (endIndex-startIndex), filter, filterKey, filterDate).values()));
    }

    @Override
    public Object updateMeeting(String userId, String meetingId, UpdateMeeting updateMeeting) {
        User user = userRepository.getUserByUserId(userId);
        if (user == null)
            return new ErrorMessage(ResponseMessage.USER_NOT_FOUND, HttpStatus.NOT_FOUND);
        Project_User project_user = projectRepository.getProjectUser(updateMeeting.getProjectId(), userId);
        if (project_user == null)
            return new ErrorMessage(ResponseMessage.USER_NOT_MEMBER, HttpStatus.NOT_FOUND);
        Meeting meeting = meetingRepository.getMeetingById(meetingId, updateMeeting.getProjectId());
        if (meeting == null)
            return new ErrorMessage(ResponseMessage.MEETING_NOT_FOUND, HttpStatus.NOT_FOUND);
        if (updateMeeting.getMeetingTopic() != null)
            meeting.setMeetingTopic(updateMeeting.getMeetingTopic());
        if (updateMeeting.getMeetingVenue() != null)
            meeting.setMeetingVenue(updateMeeting.getMeetingVenue());
        if (updateMeeting.getMeetingExpectedTime() != null)
            meeting.setMeetingExpectedTime(updateMeeting.getMeetingExpectedTime());
        if (updateMeeting.getMeetingActualTime() != null)
            meeting.setMeetingActualTime(updateMeeting.getMeetingActualTime());
        if (updateMeeting.getActualDuration() != null)
            meeting.setActualDuration(updateMeeting.getActualDuration());
        if (updateMeeting.getExpectedDuration() != null)
            meeting.setExpectedDuration(updateMeeting.getExpectedDuration());

        meetingRepository.updateMeeting(meeting);

        return new Response(ResponseMessage.SUCCESS, HttpStatus.OK);
    }

    @Override
    public Object deleteMeeting(String userId, String meetingId, String projectId) {
        User user = userRepository.getUserByUserId(userId);
        if (user == null)
            return new ErrorMessage(ResponseMessage.USER_NOT_FOUND, HttpStatus.NOT_FOUND);
        Project_User project_user = projectRepository.getProjectUser(projectId, userId);
        if (project_user == null)
            return new ErrorMessage(ResponseMessage.USER_NOT_MEMBER, HttpStatus.NOT_FOUND);
        Meeting meeting = meetingRepository.getMeetingById(meetingId, projectId);
        if (meeting == null)
            return new ErrorMessage(ResponseMessage.MEETING_NOT_FOUND, HttpStatus.NOT_FOUND);
        meetingRepository.flagMeeting(meetingId);
        meetingRepository.flagMeetingAssociatedDiscussionPoints(meetingId);
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
        minute.setDueDate(addMinute.getDueDate());
        minute.setIsDeleted(false);

        meetingRepository.addDiscussionPoint(minute);

        return new Response(ResponseMessage.SUCCESS, HttpStatus.OK);
    }

    @Override
    public Object updateDiscussionPoint(String userId, String meetingId, String discussionId, UpdateMinute updateMinute) {
        User user = userRepository.getUserByUserId(userId);
        if (user == null)
            return new ErrorMessage(ResponseMessage.USER_NOT_FOUND, HttpStatus.NOT_FOUND);
        Project_User project_user = projectRepository.getProjectUser(updateMinute.getProjectId(), userId);
        if (project_user == null)
            return new ErrorMessage(ResponseMessage.USER_NOT_MEMBER, HttpStatus.NOT_FOUND);
        Meeting meeting = meetingRepository.getMeetingById(meetingId, updateMinute.getProjectId());
        if (meeting == null)
            return new ErrorMessage(ResponseMessage.MEETING_NOT_FOUND, HttpStatus.NOT_FOUND);
        Minute minute = meetingRepository.getDiscussionPoint(discussionId);
        if (minute == null)
            return new ErrorMessage(ResponseMessage.DISCUSSION_NOT_FOUND, HttpStatus.NOT_FOUND);
        if (updateMinute.getDescription() != null)
            minute.setDescription(updateMinute.getDescription());
        if (updateMinute.getRemarks() != null)
            minute.setRemarks(updateMinute.getRemarks());
        if (updateMinute.getActionBy() != null)
            minute.setActionBy(updateMinute.getActionBy());
        if (updateMinute.isActionByGuest() != null)
            minute.setActionByGuest(updateMinute.isActionByGuest());
        if (updateMinute.getDueDate() != null)
            minute.setDueDate(updateMinute.getDueDate());

        meetingRepository.updateDiscussionPoint(minute);
        return new Response(ResponseMessage.SUCCESS, HttpStatus.OK);

    }

    @Override
    public Object deleteDiscussionPoint(String userId, String meetingId, String discussionId, String projectId) {
        User user = userRepository.getUserByUserId(userId);
        if (user == null)
            return new ErrorMessage(ResponseMessage.USER_NOT_FOUND, HttpStatus.NOT_FOUND);
        Project_User project_user = projectRepository.getProjectUser(projectId, userId);
        if (project_user == null)
            return new ErrorMessage(ResponseMessage.USER_NOT_MEMBER, HttpStatus.NOT_FOUND);
        Meeting meeting = meetingRepository.getMeetingById(meetingId, projectId);
        if (meeting == null)
            return new ErrorMessage(ResponseMessage.MEETING_NOT_FOUND, HttpStatus.NOT_FOUND);
        Minute minute = meetingRepository.getDiscussionPoint(discussionId);
        if (minute == null)
            return new ErrorMessage(ResponseMessage.DISCUSSION_NOT_FOUND, HttpStatus.NOT_FOUND);
        meetingRepository.flagDiscussionPoint(discussionId);

        return new Response(ResponseMessage.SUCCESS, HttpStatus.OK);
    }

    @Override
    public Object transitionToTask(String userId, String meetingId, String discussionId, TaskDto taskDto) {
        User user = userRepository.getUserByUserId(userId);
        if (user == null)
            return new ErrorMessage(ResponseMessage.USER_NOT_FOUND, HttpStatus.NOT_FOUND);
        Project_User project_user = projectRepository.getProjectUser(taskDto.getProjectId(), userId);
        if (project_user == null)
            return new ErrorMessage(ResponseMessage.USER_NOT_MEMBER, HttpStatus.NOT_FOUND);
        Meeting meeting = meetingRepository.getMeetingById(meetingId,taskDto.getProjectId());
        if (meeting == null)
            return new ErrorMessage(ResponseMessage.MEETING_NOT_FOUND, HttpStatus.NOT_FOUND);
        Minute minute = meetingRepository.getDiscussionPoint(discussionId);
        if (minute == null)
            return new ErrorMessage(ResponseMessage.DISCUSSION_NOT_FOUND, HttpStatus.NOT_FOUND);
        taskService.addTaskToProject(taskDto.getProjectId(),  taskDto);
        return new Response(ResponseMessage.SUCCESS,HttpStatus.OK);
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
