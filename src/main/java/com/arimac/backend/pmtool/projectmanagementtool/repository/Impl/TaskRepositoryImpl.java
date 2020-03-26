package com.arimac.backend.pmtool.projectmanagementtool.repository.Impl;

import com.arimac.backend.pmtool.projectmanagementtool.dtos.TaskAlertDto;
import com.arimac.backend.pmtool.projectmanagementtool.dtos.TaskUpdateDto;
import com.arimac.backend.pmtool.projectmanagementtool.dtos.TaskUserResponseDto;
import com.arimac.backend.pmtool.projectmanagementtool.model.Task;
import com.arimac.backend.pmtool.projectmanagementtool.repository.TaskRepository;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.sql.PreparedStatement;
import java.sql.Timestamp;
import java.util.List;

@Service
public class TaskRepositoryImpl implements TaskRepository {

    private final JdbcTemplate jdbcTemplate;

    public TaskRepositoryImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Task addTaskToProject(Task task) {
        jdbcTemplate.update(connection -> {
            PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO Task (taskId, projectId, taskName, taskInitiator, taskAssignee, taskNote, taskStatus, taskCreatedAt, taskDueDateAt, taskReminderAt, isDeleted) VALUES (?,?,?,?,?,?,?,?,?,?,?)");
            preparedStatement.setString(1, task.getTaskId());
            preparedStatement.setString(2, task.getProjectId());
            preparedStatement.setString(3, task.getTaskName());
            preparedStatement.setString(4, task.getTaskInitiator());
            preparedStatement.setString(5, task.getTaskAssignee());
            preparedStatement.setString(6, task.getTaskNote());
            preparedStatement.setString(7, task.getTaskStatus().toString());
            preparedStatement.setTimestamp(8, task.getTaskCreatedAt());
            preparedStatement.setTimestamp(9, task.getTaskDueDateAt());
            preparedStatement.setTimestamp(10, task.getTaskReminderAt());
            preparedStatement.setBoolean(11, task.getIsDeleted());

            return preparedStatement;
        });
        return task;
    }

    @Override
    public List<Task> getAllProjectTasksByUser(String projectId) {
        String sql = "SELECT * FROM Task WHERE projectId=? AND isDeleted=false";
        List<Task> taskList = jdbcTemplate.query(sql, new Task(), projectId);
        return  taskList;
    }

    @Override
    public List<TaskUserResponseDto> getAllProjectTasksWithProfile(String projectId) {
        String sql = "SELECT * FROM Task as t LEFT JOIN User AS u ON t.taskAssignee=u.userId WHERE t.projectId=? AND t.isDeleted=false";
        List<TaskUserResponseDto> taskList = jdbcTemplate.query(sql, new TaskUserResponseDto(), projectId);
        return  taskList;
    }

    @Override
    public List<Task> getAllUserAssignedTasks(String userId, String projectId) {
       String sql = "SELECT * FROM Task WHERE projectId=? AND taskAssignee=? AND isDeleted=false";
       List<Task> taskList = jdbcTemplate.query(sql, new Task(), projectId, userId);
       return taskList;
    }

    @Override
    public List<TaskUserResponseDto> getAllUserAssignedTasksWithProfile(String userId, String projectId) {
        String sql = "SELECT * FROM Task as t LEFT JOIN User AS u ON t.taskAssignee=u.userId WHERE t.projectId=? AND t.taskAssignee=? AND t.isDeleted=false";
        List<TaskUserResponseDto> taskList = jdbcTemplate.query(sql, new TaskUserResponseDto(), projectId, userId);
        return  taskList;
    }

    @Override
    public Task getProjectTask(String taskId) {
        String sql = "SELECT * FROM Task WHERE taskId=? AND isDeleted=false";
        Task task;
        try {
            task = jdbcTemplate.queryForObject(sql, new Task(), taskId);
        } catch (EmptyResultDataAccessException e){
            return null;
        }
        return task;
    }

    @Override
    public Task getProjectTaskWithDeleted(String taskId) {
        String sql = "SELECT * FROM Task WHERE taskId=?";
        Task task;
        try {
            task = jdbcTemplate.queryForObject(sql, new Task(), taskId);
        } catch (EmptyResultDataAccessException e){
            return null;
        }
        return task;
    }

    @Override
    public Object updateProjectTask(String taskId, TaskUpdateDto taskUpdateDto) {
        jdbcTemplate.update(connection -> {
            PreparedStatement preparedStatement = connection.prepareStatement("UPDATE Task SET taskName=?, taskAssignee=?, taskNote=?, taskStatus=?, taskDueDateAt=?, taskReminderAt=? WHERE taskId=?");
            preparedStatement.setString(1, taskUpdateDto.getTaskName());
            preparedStatement.setString(2, taskUpdateDto.getTaskAssignee());
            preparedStatement.setString(3, taskUpdateDto.getTaskNotes());
            preparedStatement.setString(4, taskUpdateDto.getTaskStatus().toString());
            preparedStatement.setTimestamp(5, taskUpdateDto.getTaskDueDate());
            preparedStatement.setTimestamp(6, taskUpdateDto.getTaskRemindOnDate());
            preparedStatement.setString(7, taskId);

            return preparedStatement;
        });
        return taskUpdateDto;
    }

    @Override
    public void flagProjectTask(String taskId) {
        String sql = "UPDATE Task SET isDeleted=true WHERE taskId=?";
        jdbcTemplate.update(sql, taskId);
    }

    @Override
    public void flagProjectBoundTasks(String projectId) {
        String sql = "UPDATE Task SET isDeleted=true WHERE projectId=?";
        jdbcTemplate.update(sql, projectId);
    }

    @Override
    public List<TaskAlertDto> getTaskAlertList() {
//        String sql = "SELECT * FROM Task as t LEFT JOIN project as p ON t.projectId = p.projectId LEFT JOIN User as u ON t.taskAssignee = u.userId WHERE t.taskStatus !=? AND t.isDeleted=false";
        String sql = "SELECT * FROM Task as t LEFT JOIN project as p ON t.projectId = p.projectId LEFT JOIN User as u ON t.taskAssignee = u.userId WHERE t.taskStatus !=? AND t.isDeleted=false AND u.userSlackId IS NOT NULL and  u.notification = true";
        return jdbcTemplate.query(sql, new TaskAlertDto(), "closed");
    }
}
