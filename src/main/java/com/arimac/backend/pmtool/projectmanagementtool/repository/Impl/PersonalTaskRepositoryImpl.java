package com.arimac.backend.pmtool.projectmanagementtool.repository.Impl;

import com.arimac.backend.pmtool.projectmanagementtool.dtos.PersonalTask.PersonalTask;
import com.arimac.backend.pmtool.projectmanagementtool.repository.PersonalTaskRepository;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.sql.PreparedStatement;

@Service
public class PersonalTaskRepositoryImpl implements PersonalTaskRepository {
    private final JdbcTemplate jdbcTemplate;

    public PersonalTaskRepositoryImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void addPersonalTask(PersonalTask personalTask) {
        jdbcTemplate.update(connection -> {
            PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO PersonalTask (taskId, taskName, taskAssignee, taskNote, taskCreatedAt, taskDueDateAt, taskReminderAt, isDeleted, taskStatus) VALUES (?,?,?,?,?,?,?,?,?)");
            preparedStatement.setString(1, personalTask.getTaskId());
            preparedStatement.setString(2, personalTask.getTaskName());
            preparedStatement.setString(3, personalTask.getTaskAssignee());
            preparedStatement.setString(4, personalTask.getTaskNote());
            preparedStatement.setTimestamp(5, personalTask.getTaskCreatedAt());
            preparedStatement.setTimestamp(6, personalTask.getTaskDueDateAt());
            preparedStatement.setTimestamp(7, personalTask.getTaskReminderAt());
            preparedStatement.setBoolean(8, personalTask.getIsDeleted());
            preparedStatement.setString(9, personalTask.getTaskStatus().toString());

            return preparedStatement;
        });
    }
}
