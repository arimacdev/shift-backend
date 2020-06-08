package com.arimac.backend.pmtool.projectmanagementtool.repository.Impl;

import com.arimac.backend.pmtool.projectmanagementtool.dtos.ActivityLog.UserActivityLog;
import com.arimac.backend.pmtool.projectmanagementtool.model.ActivityLog;
import com.arimac.backend.pmtool.projectmanagementtool.repository.ActivityLogRepository;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.sql.PreparedStatement;
import java.util.List;

@Service
public class ActivityLogRepositoryImpl implements ActivityLogRepository {
    private final JdbcTemplate jdbcTemplate;

    public ActivityLogRepositoryImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void addActivityLogEntry(ActivityLog activityLog) {
        jdbcTemplate.update(connection -> {
            PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO ActivityLog (logId, entityType, entityId, operation, actionTimestamp, actor, previousValue, updatedvalue, updateType) VALUES (?,?,?,?,?,?,?,?,?)");
            preparedStatement.setString(1, activityLog.getLogId());
            preparedStatement.setString(2, activityLog.getEntityType().toString());
            preparedStatement.setString(3, activityLog.getEntityId());
            preparedStatement.setString(4, activityLog.getOperation().toString());
            preparedStatement.setTimestamp(5, activityLog.getActionTimestamp());
            preparedStatement.setString(6, activityLog.getActor());
            preparedStatement.setString(7, activityLog.getPreviousValue());
            preparedStatement.setString(8, activityLog.getUpdatedvalue());
            preparedStatement.setString(9, activityLog.getUpdateType());

            return preparedStatement;
        });
    }

    @Override
    public List<UserActivityLog> getTaskActivity(String taskId) {
        String sql = "SELECT * FROM ActivityLog AS AL LEFT JOIN User as U on AL.actor = U.userId WHERE entityId=?";
        try {
            return jdbcTemplate.query(sql, new UserActivityLog(), taskId);
        } catch (EmptyResultDataAccessException e){
            return null;
        }
    }
}
