package com.arimac.backend.pmtool.projectmanagementtool.repository.Impl;

import com.arimac.backend.pmtool.projectmanagementtool.model.Task;
import com.arimac.backend.pmtool.projectmanagementtool.repository.TaskRepository;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.sql.PreparedStatement;

@Service
public class TaskRepositoryImpl implements TaskRepository {

    private final JdbcTemplate jdbcTemplate;

    public TaskRepositoryImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Task addTaskToProject(Task task) {
        jdbcTemplate.update(connection -> {
            PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO Task (taskId, projectId, taskName, taskInitiator, taskAssignee, taskNote, taskStatus, taskCreatedAt, taskDueDateAt, taskReminderAt) VALUES (?,?,?,?,?,?,?,?,?,?)");
            preparedStatement.setString(1, task.getTaskId());
            preparedStatement.setString(2, task.getProjectId());
            preparedStatement.setString(3, task.getTaskName());
            preparedStatement.setString(4, task.getTaskInitiator());
            preparedStatement.setString(5, task.getTaskAssignee());
            preparedStatement.setString(6, task.getTaskNote());
            preparedStatement.setString(7, task.getTaskStatus());
            preparedStatement.setTimestamp(8, task.getTaskCreatedAt());
            preparedStatement.setTimestamp(9, task.getTaskDueDateAt());
            preparedStatement.setTimestamp(10, task.getTaskReminderAt());

            return preparedStatement;
        });
        return task;
    }
}
