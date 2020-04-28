package com.arimac.backend.pmtool.projectmanagementtool.repository.Impl;

import com.arimac.backend.pmtool.projectmanagementtool.dtos.PersonalTask.PersonalTask;
import com.arimac.backend.pmtool.projectmanagementtool.dtos.PersonalTask.PersonalTaskUpdateDto;
import com.arimac.backend.pmtool.projectmanagementtool.enumz.TaskTypeEnum;
import com.arimac.backend.pmtool.projectmanagementtool.model.Task;
import com.arimac.backend.pmtool.projectmanagementtool.repository.PersonalTaskRepository;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.sql.PreparedStatement;
import java.util.List;

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

    @Override
    public List<PersonalTask> getAllPersonalTasks(String userId) {
            String sql = "SELECT * FROM PersonalTask WHERE taskAssignee=? AND isDeleted=false";
            List<PersonalTask> personalTaskList = jdbcTemplate.query(sql, new PersonalTask(), userId);
            return personalTaskList;
        }

    @Override
    public PersonalTask getPersonalTaskByUserId(String userId, String taskId) {
        String sql = "SELECT * FROM PersonalTask WHERE taskAssignee=? AND taskId=? AND isDeleted=false";
        try {
            PersonalTask personalTask = jdbcTemplate.queryForObject(sql, new PersonalTask(), userId, taskId);
            return personalTask;
        } catch (EmptyResultDataAccessException e){
            return null;
        }

    }

    @Override
    public void updatePersonalTask(String taskId, PersonalTaskUpdateDto personalTaskUpdateDto) {
        jdbcTemplate.update(connection -> {
            PreparedStatement preparedStatement = connection.prepareStatement("UPDATE PersonalTask SET taskName=?, taskNote=?, taskDueDateAt=?, taskReminderAt=?, taskStatus=?");
            preparedStatement.setString(1, personalTaskUpdateDto.getTaskName());
            preparedStatement.setString(2, personalTaskUpdateDto.getTaskNotes());
            preparedStatement.setTimestamp(3, personalTaskUpdateDto.getTaskDueDate());
            preparedStatement.setTimestamp(4, personalTaskUpdateDto.getTaskRemindOnDate());
            preparedStatement.setString(5, personalTaskUpdateDto.getTaskStatus().toString());

            return preparedStatement;
        });
    }

    @Override
    public void flagPersonalTask(String taskId) {
        String sql = "UPDATE PersonalTask SET isDeleted=? WHERE taskId=?";
        jdbcTemplate.update(sql,true, taskId);
    }
}

