package com.arimac.backend.pmtool.projectmanagementtool.repository.Impl;

import com.arimac.backend.pmtool.projectmanagementtool.model.TaskFile;
import com.arimac.backend.pmtool.projectmanagementtool.repository.TaskFileRepository;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.sql.PreparedStatement;
import java.util.List;

@Service
public class TaskFileRepositoryImpl implements TaskFileRepository {
    private final JdbcTemplate jdbcTemplate;

    public TaskFileRepositoryImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Object uploadTaskFile(TaskFile taskFile) {
        jdbcTemplate.update(connection -> {
            PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO TaskFile (taskFileId, taskId, taskFileName, taskFileUrl, taskFileCreator, taskFileDate, isDeleted) VALUES(?,?,?,?,?,?,?)");
            preparedStatement.setString(1, taskFile.getTaskFileId());
            preparedStatement.setString(2, taskFile.getTaskId());
            preparedStatement.setString(3, taskFile.getTaskFileName());
            preparedStatement.setString(4, taskFile.getTaskFileUrl());
            preparedStatement.setString(5, taskFile.getTaskFileCreator());
            preparedStatement.setTimestamp(6, taskFile.getTaskFileDate());
            preparedStatement.setBoolean(7, false);

            return preparedStatement;
        });
        return taskFile;
    }

    @Override
    public List<TaskFile> getAllTaskFiles(String taskId) {
        String sql = "SELECT * FROM TaskFile WHERE taskId=? AND isDeleted=false";
        List<TaskFile> taskFileList = jdbcTemplate.query(sql, new TaskFile(), taskId);
        return taskFileList;
    }

    @Override
    public void flagTaskFile(String taskFileId) {
        String sql = "UPDATE TaskFile SET isDeleted=? WHERE taskFileId=?";
        jdbcTemplate.update(sql,true, taskFileId);
    }
}
