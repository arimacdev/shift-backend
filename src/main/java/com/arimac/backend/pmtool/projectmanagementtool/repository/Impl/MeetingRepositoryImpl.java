package com.arimac.backend.pmtool.projectmanagementtool.repository.Impl;

import com.arimac.backend.pmtool.projectmanagementtool.dtos.Meeting.*;
import com.arimac.backend.pmtool.projectmanagementtool.enumz.FilterQueryTypeEnum;
import com.arimac.backend.pmtool.projectmanagementtool.enumz.Meeting.MemberType;
import com.arimac.backend.pmtool.projectmanagementtool.exception.PMException;
import com.arimac.backend.pmtool.projectmanagementtool.model.Meeting;
import com.arimac.backend.pmtool.projectmanagementtool.model.Meeting_Attendee;
import com.arimac.backend.pmtool.projectmanagementtool.model.Minute;
import com.arimac.backend.pmtool.projectmanagementtool.repository.MeetingRepository;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

@Service
public class MeetingRepositoryImpl implements MeetingRepository {
    private final JdbcTemplate jdbcTemplate;

    public MeetingRepositoryImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void addMeeting(Meeting meeting) {
        jdbcTemplate.update(connection -> {
            PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO Meeting (meetingId,projectId,meetingTopic,meetingVenue,meetingExpectedTime,meetingActualTime,expectedDuration,actualDuration,createdAt,meetingCreatedBy,isDeleted) VALUES (?,?,?,?,?,?,?,?,?,?,?)");
            preparedStatement.setString(1, meeting.getMeetingId());
            preparedStatement.setString(2, meeting.getProjectId());
            preparedStatement.setString(3, meeting.getMeetingTopic());
            preparedStatement.setString(4, meeting.getMeetingVenue());
            preparedStatement.setTimestamp(5, meeting.getMeetingExpectedTime());
            preparedStatement.setTimestamp(6, meeting.getMeetingActualTime());
            preparedStatement.setLong(7, meeting.getExpectedDuration());
            preparedStatement.setLong(8, meeting.getActualDuration());
            preparedStatement.setTimestamp(9, meeting.getCreatedAt());
            preparedStatement.setString(10, meeting.getMeetingCreatedBy());
            preparedStatement.setBoolean(11, meeting.getIsDeleted());

            return preparedStatement;
        });
    }

    @Override
    public void addMeetingAttendee(Meeting_Attendee meeting_attendee) {
        jdbcTemplate.update(connection -> {
            PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO Meeting_Attendee (meetingId, attendeeId, attendeeType, isGuest) VALUES (?,?,?,?)");
            preparedStatement.setString(1, meeting_attendee.getMeetingId());
            preparedStatement.setString(2, meeting_attendee.getAttendeeId());
            preparedStatement.setInt(3, meeting_attendee.getMemberType());
            preparedStatement.setBoolean(4, meeting_attendee.isGuest());
            return preparedStatement;
        });
    }

    @Override
    public Meeting getMeetingById(String meetingId, String projectId) {
        String sql = "SELECT * FROM Meeting WHERE meetingId=? AND projectId=? AND isDeleted=false";
        try {
            return jdbcTemplate.queryForObject(sql, new Meeting(), meetingId, projectId);
        } catch (EmptyResultDataAccessException e){
            return null;
        } catch (Exception e){
            throw new PMException(e.getMessage());
        }
    }

    @Override
    public MeetingResponse getCompleteMeetingById(String meetingId, String projectId) {
        String sql = "SELECT * FROM Meeting LEFT JOIN Meeting_Attendee ON Meeting.meetingId = Meeting_Attendee.meetingId " +
                "LEFT JOIN User ON userId = Meeting_Attendee.attendeeId " +
                "WHERE Meeting.meetingId =? AND Meeting.projectId =? AND Meeting.isDeleted=false";
        MeetingResponse meetingResponse = new MeetingResponse();

        return jdbcTemplate.query(sql, new Object[]{meetingId, projectId}, (ResultSet rs) -> {
           while (rs.next()){
               MeetingResponseUser meetingResponseUser = new MeetingResponseUser();
               meetingResponseUser.setAttendeeId(rs.getString("attendeeId"));
               meetingResponseUser.setGuest(rs.getBoolean("isGuest"));
               meetingResponseUser.setMemberType(rs.getInt("attendeeType"));

               meetingResponseUser.setMemberTypeName(MemberType.getMemberType(rs.getInt("attendeeType")));
               if (!rs.getBoolean("isGuest")){
                   meetingResponseUser.setFirstName(rs.getString("firstName"));
                   meetingResponseUser.setLastName(rs.getString("lastName"));
                   meetingResponseUser.setProfileImage(rs.getString("profileImage"));
               }
               if (meetingResponse.getMeetingId() == null) {
                   meetingResponse.setProjectId(rs.getString("projectId"));
                   meetingResponse.setMeetingId(rs.getString("meetingId"));
                   meetingResponse.setMeetingTopic(rs.getString("meetingTopic"));
                   meetingResponse.setMeetingVenue(rs.getString("meetingVenue"));
                   meetingResponse.setMeetingExpectedTime(rs.getTimestamp("meetingExpectedTime"));
                   meetingResponse.setMeetingActualTime(rs.getTimestamp("meetingActualTime"));
                   meetingResponse.setExpectedDuration(rs.getLong("expectedDuration"));
                   meetingResponse.setActualDuration(rs.getLong("actualDuration"));
                   meetingResponse.setCreatedAt(rs.getTimestamp("createdAt"));
                   meetingResponse.setMeetingCreatedBy(rs.getString("meetingCreatedBy"));

                   setMemberType(rs.getInt("attendeeType"), meetingResponse, meetingResponseUser);
               } else {
                   setMemberType(rs.getInt("attendeeType"), meetingResponse, meetingResponseUser);
               }
           }
            return meetingResponse;
        });
    }

    @Override
    public HashMap<String, MeetingResponse> getMeetingsOfProject(String projectId, int startIndex, int limit, boolean filter, String filterKey, String filterDate) {
        String sql = "SELECT * FROM Meeting LEFT JOIN Meeting_Attendee ON Meeting.meetingId = Meeting_Attendee.meetingId " +
                "LEFT JOIN User ON userId = Meeting_Attendee.attendeeId " +
                "WHERE projectId = ?";
        List<Object> parameters = new ArrayList<>();
        parameters.add(projectId);
        String filterQuery = "";
        if (filter && !filterDate.isEmpty()) {
            filterQuery = filterQuery + " AND DATE_FORMAT(meetingActualTime,'%Y-%m-%d') =?";
            parameters.add(filterDate);
        }
        if (filter && !filterKey.isEmpty()){
            filterQuery = filterQuery + " AND MeetingTopic LIKE ?";
            parameters.add("%" +filterKey + "%");
        }
        parameters.add(limit);
        parameters.add(startIndex);
        return jdbcTemplate.query(sql + filterQuery + " LIMIT ? OFFSET ?", parameters.toArray(), (ResultSet rs) -> {
            HashMap<String, MeetingResponse> meetingResponseMap = new HashMap<>();
            while (rs.next()){
                MeetingResponseUser meetingResponseUser = new MeetingResponseUser();
                meetingResponseUser.setAttendeeId(rs.getString("attendeeId"));
                meetingResponseUser.setGuest(rs.getBoolean("isGuest"));
                meetingResponseUser.setMemberType(rs.getInt("attendeeType"));

                meetingResponseUser.setMemberTypeName(MemberType.getMemberType(rs.getInt("attendeeType")));
                if (!rs.getBoolean("isGuest")){
                    meetingResponseUser.setFirstName(rs.getString("firstName"));
                    meetingResponseUser.setLastName(rs.getString("lastName"));
                    meetingResponseUser.setProfileImage(rs.getString("profileImage"));
                }
                if (!meetingResponseMap.containsKey(rs.getString("meetingId")) ){
                    MeetingResponse meetingResponse = new MeetingResponse();
                    meetingResponse.setProjectId(rs.getString("projectId"));
                    meetingResponse.setMeetingId(rs.getString("meetingId"));
                    meetingResponse.setMeetingTopic(rs.getString("meetingTopic"));
                    meetingResponse.setMeetingVenue(rs.getString("meetingVenue"));
                    meetingResponse.setMeetingExpectedTime(rs.getTimestamp("meetingExpectedTime"));
                    meetingResponse.setMeetingActualTime(rs.getTimestamp("meetingActualTime"));
                    meetingResponse.setExpectedDuration(rs.getLong("expectedDuration"));
                    meetingResponse.setActualDuration(rs.getLong("actualDuration"));
                    meetingResponse.setCreatedAt(rs.getTimestamp("createdAt"));
                    meetingResponse.setMeetingCreatedBy(rs.getString("meetingCreatedBy"));

                    setMemberType(rs.getInt("attendeeType"), meetingResponse, meetingResponseUser);
                    meetingResponseMap.put(meetingResponse.getMeetingId(), meetingResponse);

                } else {
                    MeetingResponse meetingResponse = meetingResponseMap.get(rs.getString("meetingId"));
                    setMemberType(rs.getInt("attendeeType"), meetingResponse, meetingResponseUser);
                    meetingResponseMap.put(meetingResponse.getMeetingId(), meetingResponse);

                }
            }
            return meetingResponseMap;
        });
    }

    private void setMemberType(int attendeeType, MeetingResponse meetingResponse, MeetingResponseUser meetingResponseUser){
        switch (attendeeType){
            case 1:
                meetingResponse.getMeetingChaired().add(meetingResponseUser);
                break;
            case 2:
                meetingResponse.getMeetingAttended().add(meetingResponseUser);
                break;
            case 3:
                meetingResponse.getMeetingAbsent().add(meetingResponseUser);
                break;
            case 4:
                meetingResponse.getMeetingCopiesTo().add(meetingResponseUser);
                break;
            case 5:
                meetingResponse.getMeetingPrepared().add(meetingResponseUser);
                break;
        }
    }

    @Override
    public void updateMeeting(Meeting meeting) {
      jdbcTemplate.update(connection -> {
          PreparedStatement preparedStatement = connection.prepareStatement("UPDATE Meeting SET meetingTopic=?, meetingVenue=?, meetingExpectedTime=?, meetingActualTime=?, expectedDuration=?, actualDuration=? WHERE meetingId=?");
          preparedStatement.setString(1, meeting.getMeetingTopic());
          preparedStatement.setString(2, meeting.getMeetingVenue());
          preparedStatement.setTimestamp(3, meeting.getMeetingExpectedTime());
          preparedStatement.setTimestamp(4, meeting.getMeetingActualTime());
          preparedStatement.setLong(5, meeting.getExpectedDuration());
          preparedStatement.setLong(6, meeting.getActualDuration());
          preparedStatement.setString(7, meeting.getMeetingId());
          return preparedStatement;
      });
    }

    @Override
    public void removeAttendeesOfMeeting(String meetingId, int attendeeType) {
        String sql = "DELETE FROM Meeting_Attendee WHERE meetingId=? AND attendeeType=?";
        try {
            jdbcTemplate.update(sql, meetingId, attendeeType);
        } catch (Exception e){
            throw new PMException(e.getMessage());
        }
    }

    @Override
    public void flagMeeting(String meetingId) {
        String sql = "UPDATE Meeting SET isDeleted=true WHERE meetingId=?";
        try {
            jdbcTemplate.update(sql, meetingId);
        } catch (Exception e){
            throw new PMException(e.getMessage());
        }
    }

    @Override
    public void flagMeetingAssociatedDiscussionPoints(String meetingId) {
        String sql = "UPDATE Minute SET isDeleted=true WHERE meetingId=?";
        try {
            jdbcTemplate.update(sql, meetingId);
        } catch (Exception e){
            throw new PMException(e.getMessage());
        }
    }

    @Override
    public Minute getDiscussionPoint(String discussionId) {
        String sql = "SELECT * FROM Minute WHERE minuteId=? AND isDeleted=false";
        try {
            return jdbcTemplate.queryForObject(sql, new Minute(), discussionId);
        } catch (EmptyResultDataAccessException e){
            return null;
        } catch (Exception e){
            throw new PMException(e.getMessage());
        }
    }

    @Override
    public void addDiscussionPoint(Minute minute) {
        jdbcTemplate.update(connection -> {
            PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO Minute (minuteId, meetingId, discussionPoint, description, remarks, actionBy, actionByGuest, addedBy, isDeleted, dueDate ) VALUES(?,?,?,?,?,?,?,?,?,?)");
            preparedStatement.setString(1, minute.getMinuteId());
            preparedStatement.setString(2, minute.getMeetingId());
            preparedStatement.setInt(3, minute.getDiscussionPoint());
            preparedStatement.setString(4, minute.getDescription());
            preparedStatement.setString(5, minute.getRemarks());
            preparedStatement.setString(6, minute.getActionBy());
            preparedStatement.setBoolean(7, minute.isActionByGuest());
            preparedStatement.setString(8, minute.getAddedBy());
            preparedStatement.setBoolean(9, minute.getIsDeleted());
            preparedStatement.setTimestamp(10, minute.getDueDate());
            return preparedStatement;
        });
    }

    @Override
    public void updateDiscussionPoint(Minute minute) {
        jdbcTemplate.update(connection -> {
            PreparedStatement preparedStatement = connection.prepareStatement("UPDATE Minute SET description=?, remarks=?, actionBy=?, actionByGuest=?, dueDate=? WHERE minuteId=?");
            preparedStatement.setString(1, minute.getDescription());
            preparedStatement.setString(2, minute.getRemarks());
            preparedStatement.setString(3, minute.getActionBy());
            preparedStatement.setBoolean(4, minute.isActionByGuest());
            preparedStatement.setTimestamp(5, minute.getDueDate());
            preparedStatement.setString(6, minute.getMinuteId());
            return preparedStatement;
        });
    }

    @Override
    public void flagDiscussionPoint(String discussionId) {
        String sql = "UPDATE Minute SET isDeleted=true WHERE minuteId=?";
        try {
            jdbcTemplate.update(sql, discussionId);
        } catch (Exception e){
            throw new PMException(e.getMessage());
        }
    }


    @Override
    public List<DiscussionPoint> getDiscussionPointOfMeeting(String meetingId) {
        String sql = "SELECT * FROM Minute AS M LEFT JOIN User AS U ON U.userId = M.actionBy WHERE M.meetingId=? AND M.isDeleted=false";
        List<DiscussionPoint> discussionPoints = new ArrayList<>();
        return jdbcTemplate.query(sql, new Object[] {meetingId}, (ResultSet rs) -> {
            while (rs.next()){
                DiscussionPoint discussionPoint = new DiscussionPoint();
                discussionPoint.setMeetingId(rs.getString("meetingId"));
                discussionPoint.setMinuteId(rs.getString("minuteId"));
                discussionPoint.setDiscussionPoint(rs.getInt("discussionPoint"));
                discussionPoint.setDescription(rs.getString("description"));
                discussionPoint.setRemarks(rs.getString("remarks"));
                discussionPoint.setActionBy(rs.getString("actionBy"));
                discussionPoint.setActionByGuest(rs.getBoolean("actionByGuest"));
                discussionPoint.setDueDate(rs.getTimestamp("dueDate"));
                if (!discussionPoint.isActionByGuest()){
                    discussionPoint.setMeetingUser(new MeetingUser(
                            rs.getString("userId"),
                            rs.getString("firstName"),
                            rs.getString("lastName"),
                            rs.getString("profileImage")
                    ));
                }
                discussionPoints.add(discussionPoint);
            }
            return discussionPoints;
        });
    }
}
