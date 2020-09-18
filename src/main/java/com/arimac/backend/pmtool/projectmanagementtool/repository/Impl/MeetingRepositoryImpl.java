package com.arimac.backend.pmtool.projectmanagementtool.repository.Impl;

import com.arimac.backend.pmtool.projectmanagementtool.model.Meeting;
import com.arimac.backend.pmtool.projectmanagementtool.repository.MeetingRepository;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.sql.PreparedStatement;

@Service
public class MeetingRepositoryImpl implements MeetingRepository {
    private final JdbcTemplate jdbcTemplate;

    public MeetingRepositoryImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void addMeeting(Meeting meeting) {
        jdbcTemplate.update(connection -> {
            PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO Meeting (meetingId,projectId,meetingTopic,meetingVenue,meetingExpectedTime,meetingActualTime,expectedDuration,actualDuration,createdAt,meetingCreatedBy) VALUES (?,?,?,?,?,?,?,?,?,?)");
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

            return preparedStatement;
        });
    }
}
