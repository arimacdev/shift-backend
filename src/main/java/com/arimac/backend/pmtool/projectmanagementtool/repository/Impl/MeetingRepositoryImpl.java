package com.arimac.backend.pmtool.projectmanagementtool.repository.Impl;

import com.arimac.backend.pmtool.projectmanagementtool.dtos.Meeting.AddMinute;
import com.arimac.backend.pmtool.projectmanagementtool.dtos.Meeting.DiscussionPoint;
import com.arimac.backend.pmtool.projectmanagementtool.dtos.Meeting.MeetingUser;
import com.arimac.backend.pmtool.projectmanagementtool.exception.PMException;
import com.arimac.backend.pmtool.projectmanagementtool.model.Meeting;
import com.arimac.backend.pmtool.projectmanagementtool.model.Minute;
import com.arimac.backend.pmtool.projectmanagementtool.repository.MeetingRepository;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
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
    public Meeting getMeetingById(String meetingId, String projectId) {
        String sql = "SELECT * FROM Meeting WHERE meetingId=? AND projectId=?";
        try {
            return jdbcTemplate.queryForObject(sql, new Meeting(), meetingId, projectId);
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
