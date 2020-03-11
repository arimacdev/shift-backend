package com.arimac.backend.pmtool.projectmanagementtool.repository.Impl;

import com.arimac.backend.pmtool.projectmanagementtool.model.SubTask;
import com.arimac.backend.pmtool.projectmanagementtool.repository.SubTaskRepository;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.sql.PreparedStatement;

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
}
