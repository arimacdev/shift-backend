package com.arimac.backend.pmtool.projectmanagementtool.repository.Impl;

import com.arimac.backend.pmtool.projectmanagementtool.dtos.ActivityLog.UserActivityLog;
import com.arimac.backend.pmtool.projectmanagementtool.model.ActivityLog;
import com.arimac.backend.pmtool.projectmanagementtool.repository.ActivityLogRepository;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Service;

import java.sql.PreparedStatement;
import java.util.*;

@Service
public class ActivityLogRepositoryImpl implements ActivityLogRepository {
    private final JdbcTemplate jdbcTemplate;
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;


    public ActivityLogRepositoryImpl(JdbcTemplate jdbcTemplate, NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
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
    public List<UserActivityLog> getTaskActivity(String taskId, int limit, int offset) {
        String sql = "SELECT * FROM ActivityLog AS AL " +
                "LEFT JOIN User as U on AL.actor = U.userId WHERE entityId=? AND isDeleted=false" +
                " ORDER BY actionTimestamp DESC LIMIT ? OFFSET ?";
            return jdbcTemplate.query(sql, new UserActivityLog(), taskId, limit, offset);
    }

    @Override
    public List<UserActivityLog> getProjectActivity(String projectId, List<String> entityIds, int limit, int offset) {
        MapSqlParameterSource parameters = new MapSqlParameterSource();
        parameters.addValue("ids", entityIds);
        parameters.addValue("limit", limit);
        parameters.addValue("offset", offset);
        String sql = "SELECT * FROM ActivityLog AS AL LEFT JOIN User as U ON AL.actor = U.userId " +
                "WHERE entityId IN (:ids) AND isDeleted=false " +
                "ORDER BY AL.actionTimestamp DESC LIMIT :limit OFFSET :offset";
        return namedParameterJdbcTemplate.query(sql,parameters, new UserActivityLog());
    }

    @Override
    public int taskActivityLogCount(String taskId) {
        String sql = "SELECT COUNT(*) FROM ActivityLog WHERE entityId=?";
//        return jdbcTemplate.queryForObject(sql,Integer.class, taskId);
        return jdbcTemplate.queryForObject(sql, new Object[] {taskId} , Integer.class);

    }
}
