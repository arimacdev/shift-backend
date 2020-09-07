package com.arimac.backend.pmtool.projectmanagementtool.repository.Impl;

import com.arimac.backend.pmtool.projectmanagementtool.dtos.ActivityLog.UserActivityLog;
import com.arimac.backend.pmtool.projectmanagementtool.enumz.AnalyticsEnum.ChartCriteriaEnum;
import com.arimac.backend.pmtool.projectmanagementtool.exception.PMException;
import com.arimac.backend.pmtool.projectmanagementtool.model.ActivityLog;
import com.arimac.backend.pmtool.projectmanagementtool.repository.ActivityLogRepository;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.List;

@Service
public class ActivityLogRepositoryImpl implements ActivityLogRepository {
    private final JdbcTemplate jdbcTemplate;
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    private static final String ALL = "all";


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
    public int projectActivityLogCount(String projectId, List<String> entityIds) {
        String sql = "SELECT COUNT(*) FROM ActivityLog AS AL LEFT JOIN User as U ON AL.actor = U.userId " +
                "WHERE entityId IN (:ids) AND isDeleted=false ";
        MapSqlParameterSource parameters = new MapSqlParameterSource();
        parameters.addValue("ids", entityIds);
        return namedParameterJdbcTemplate.queryForObject(sql, parameters ,  Integer.class);
    }

    @Override
    public int taskActivityLogCount(String taskId) {
        String sql = "SELECT COUNT(*) FROM ActivityLog WHERE entityId=? AND isDeleted=false";
        return jdbcTemplate.queryForObject(sql, new Object[] {taskId} , Integer.class);
    }

    @Override
    public void flagEntityActivityLogs(String entityId) {
        String sql = "UPDATE ActivityLog SET isDeleted=true WHERE entityId=? AND operation!=?";
        jdbcTemplate.update(sql, entityId, "FLAG");
    }

    @Override
    public HashMap<String, Integer> getClosedTaskCount(String from, String to, ChartCriteriaEnum criteria) {
        String sql;
        String dateFormat;
        if (criteria.equals(ChartCriteriaEnum.DAY))
            dateFormat = "DATE_FORMAT(actionTimestamp,'%Y-%m-%d') ";
        else if (criteria.equals(ChartCriteriaEnum.MONTH))
            dateFormat = "DATE_FORMAT(actionTimestamp,'%Y-%m') ";
        else
            dateFormat = "DATE_FORMAT(actionTimestamp,'%Y') ";
        try {
            if (from.equals(ALL) && to.equals(ALL)){
                sql = "SELECT " + dateFormat +  "as Date, COUNT(entityId) as taskCount " +
                        "FROM ActivityLog WHERE entityType = 'Task' AND operation = 'UPDATE' AND updatedvalue = 'closed' AND isDeleted=false" +
                        "GROUP BY " + dateFormat;
                return jdbcTemplate.query(sql, (ResultSet rs) -> {
                    HashMap<String,Integer> dateCountMap = new HashMap<>();
                    while (rs.next()) {
                        dateCountMap.put(rs.getString("date"), rs.getInt("taskCount"));
                    }
                    return dateCountMap;
                });
            } else {
                sql = "SELECT " + dateFormat +  "as Date, COUNT(entityId) as taskCount " +
                        "FROM ActivityLog WHERE entityType = 'Task' AND operation = 'UPDATE' AND updatedvalue = 'closed' AND isDeleted=false AND actionTimestamp BETWEEN ? AND ?" +
                        "GROUP BY " + dateFormat;
//                 jdbcTemplate.query(sql,  (ResultSet rs) -> {
//                    while (rs.next()) {
//                        dateCountMap.put(rs.getString("date"), rs.getInt("taskCount"));
//                    }
//                }, from, to);
//                return dateCountMap;
                return jdbcTemplate.query(sql,new Object[] { from, to }, (ResultSet rs) -> {
                    HashMap<String,Integer> dateCountMap = new HashMap<>();
                    while (rs.next()) {
                        dateCountMap.put(rs.getString("date"), rs.getInt("taskCount"));
                    }
                    return dateCountMap;
                });
            }

        } catch (Exception e){
            throw new PMException(e.getMessage());
        }
    }
}
