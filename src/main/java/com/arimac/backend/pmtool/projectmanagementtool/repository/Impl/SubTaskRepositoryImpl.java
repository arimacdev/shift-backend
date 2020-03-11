package com.arimac.backend.pmtool.projectmanagementtool.repository.Impl;

import com.arimac.backend.pmtool.projectmanagementtool.model.SubTask;
import com.arimac.backend.pmtool.projectmanagementtool.repository.SubTaskRepository;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.sql.PreparedStatement;
import java.util.List;

@Service
public class SubTaskRepositoryImpl implements SubTaskRepository {

    private final JdbcTemplate jdbcTemplate;

    public SubTaskRepositoryImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Object addSubTaskToProject(SubTask subTask) {
        jdbcTemplate.update(connection -> {
            PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO SubTask (subtaskId, taskId, subtaskName, subtaskStatus) VALUES (?,?,?,?)");
            preparedStatement.setString(1, subTask.getSubtaskId());
            preparedStatement.setString(2, subTask.getTaskId());
            preparedStatement.setString(3, subTask.getSubtaskName());
            preparedStatement.setBoolean(4, subTask.isSubtaskStatus());

            return preparedStatement;
        });
        return subTask;
    }

    @Override
    public List<SubTask> getAllSubTaksOfATask(String taskId) {
        String sql = "SELECT * FROM SubTask WHERE taskId=?";
        List<SubTask> subTaskList = jdbcTemplate.query(sql, new SubTask(), taskId);
        return  subTaskList;
    }

    @Override
    public SubTask getSubTaskById(String subTaskId) {
        String sql = "SELECT * FROM SubTask WHERE subTaskId=?";
        SubTask subTask = null;
        try {
            subTask = jdbcTemplate.queryForObject(sql, new SubTask(), subTaskId);
        } catch (EmptyResultDataAccessException e){
            return subTask;
        }
        return subTask;
    }

    @Override
    public SubTask updateSubTaskById(SubTask subTask) {
        jdbcTemplate.update(connection -> {
            PreparedStatement preparedStatement = connection.prepareStatement("UPDATE SubTask SET subTaskId=?, taskId=?, subTaskName=?, subTaskStatus=? WHERE subTaskId=? ");
            preparedStatement.setString(1, subTask.getSubtaskId());
            preparedStatement.setString(2, subTask.getTaskId());
            preparedStatement.setString(3, subTask.getSubtaskName());
            preparedStatement.setBoolean(4, subTask.isSubtaskStatus());
            preparedStatement.setString(5, subTask.getSubtaskId());

            return preparedStatement;
        });
        return subTask;
    }

    @Override
    public void deleteSubTaskOfaTask(String subTaskId) {
        String sql = "DELETE FROM SubTask WHERE subTaskId=?";
        jdbcTemplate.update(sql, subTaskId);
    }
}