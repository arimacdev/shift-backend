package com.arimac.backend.pmtool.projectmanagementtool.repository.Impl;

import com.arimac.backend.pmtool.projectmanagementtool.dtos.TaskLogUser;
import com.arimac.backend.pmtool.projectmanagementtool.model.TaskLog;
import com.arimac.backend.pmtool.projectmanagementtool.repository.TaskLogRespository;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.sql.PreparedStatement;
import java.util.List;

@Service
public class TaskLogRepositoryImpl implements TaskLogRespository {

    private final JdbcTemplate jdbcTemplate;

    public TaskLogRepositoryImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void addTaskLog(TaskLog taskLog) {
        jdbcTemplate.update(connection -> {
            PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO TaskLog (taskLogId, projectId, tasklogInitiator, taskLogEntity, taskLogEntityId, operation, previous, modified, timestamp) VALUES (?,?,?,?,?,?,?,?,?)");
            preparedStatement.setString(1, taskLog.getTaskLogId());
            preparedStatement.setString(2, taskLog.getProjectId());
            preparedStatement.setString(3, taskLog.getTasklogInitiator());
            preparedStatement.setInt(4, taskLog.getTaskLogEntity());
            preparedStatement.setString(5, taskLog.getTaskLogEntityId());
            preparedStatement.setInt(6, taskLog.getOperation());
            preparedStatement.setString(7, taskLog.getPrevious());
            preparedStatement.setString(8, taskLog.getModified());
            preparedStatement.setTimestamp(9, taskLog.getTimestamp());

            return preparedStatement;
        });

    }

    @Override
    public List<TaskLogUser> getAllLogs(String projectId) {
        String sql = "SELECT * FROM TaskLog AS tl LEFT JOIN User AS u ON tl.tasklogInitiator = u.userId WHERE tl.projectId=?";
//        String sql = "SELECT * FROM TaskLog AS tl WHERE projectId=? LEFT JOIN User AS u ON tl.tasklogInitiator = u.userId";
        List<TaskLogUser> taskLogs = jdbcTemplate.query(sql, new TaskLogUser(), projectId);

        return taskLogs;
    }
}