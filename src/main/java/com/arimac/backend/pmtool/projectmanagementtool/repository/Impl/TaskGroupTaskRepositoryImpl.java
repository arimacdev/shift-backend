package com.arimac.backend.pmtool.projectmanagementtool.repository.Impl;

import com.arimac.backend.pmtool.projectmanagementtool.dtos.Task.TaskParentChildUpdateDto;
import com.arimac.backend.pmtool.projectmanagementtool.dtos.TaskGroupTask.TaskGroupTaskUpdateDto;
import com.arimac.backend.pmtool.projectmanagementtool.dtos.TaskGroupTask.TaskGroupTaskUserResponseDto;
import com.arimac.backend.pmtool.projectmanagementtool.dtos.TaskUserResponseDto;
import com.arimac.backend.pmtool.projectmanagementtool.model.Task;
import com.arimac.backend.pmtool.projectmanagementtool.model.TaskGroupTask;
import com.arimac.backend.pmtool.projectmanagementtool.repository.TaskGroupTaskRepository;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.sql.PreparedStatement;
import java.util.List;

@Service
public class TaskGroupTaskRepositoryImpl implements TaskGroupTaskRepository {
    private final JdbcTemplate jdbcTemplate;

    public TaskGroupTaskRepositoryImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public TaskGroupTask getTaskByTaskGroupId(String TaskGroupId, String taskId) {
        String sql = "SELECT * FROM TaskGroupTask WHERE taskId=? AND taskGroupId=? AND isDeleted=false";
        TaskGroupTask task;
        try {
            task = jdbcTemplate.queryForObject(sql, new TaskGroupTask(), taskId, TaskGroupId);
        } catch (EmptyResultDataAccessException e){
            return null;
        }
        return task;
    }

    @Override
    public List<TaskGroupTask> getAllTaskGroupTasksByUser(String taskGroupId) {
        String sql = "SELECT * FROM TaskGroupTask WHERE taskGroupId=? AND isDeleted=false";
        List<TaskGroupTask> taskList = jdbcTemplate.query(sql, new TaskGroupTask(), taskGroupId);
        return  taskList;
    }

    @Override
    public void addTaskGroupTask(TaskGroupTask task) {
        jdbcTemplate.update(connection -> {
            PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO TaskGroupTask (taskId, taskGroupId, taskName, taskInitiator, taskAssignee, taskNote, taskStatus, taskCreatedAt, taskDueDateAt, taskReminderAt, isDeleted, parentId, isParent) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?)");
            preparedStatement.setString(1, task.getTaskId());
            preparedStatement.setString(2, task.getTaskGroupId());
            preparedStatement.setString(3, task.getTaskName());
            preparedStatement.setString(4, task.getTaskInitiator());
            preparedStatement.setString(5, task.getTaskAssignee());
            preparedStatement.setString(6, task.getTaskNote());
            preparedStatement.setString(7, task.getTaskStatus().toString());
            preparedStatement.setTimestamp(8, task.getTaskCreatedAt());
            preparedStatement.setTimestamp(9, task.getTaskDueDateAt());
            preparedStatement.setTimestamp(10, task.getTaskReminderAt());
            preparedStatement.setBoolean(11, task.getIsDeleted());
            preparedStatement.setString(12, task.getParentId());
            preparedStatement.setBoolean(13, task.getIsParent());

            return preparedStatement;
        });
    }

    @Override
    public TaskGroupTaskUpdateDto updateTaskGroupTask(String taskId, TaskGroupTaskUpdateDto taskUpdateDto) {
        jdbcTemplate.update(connection -> {
            PreparedStatement preparedStatement = connection.prepareStatement("UPDATE TaskGroupTask SET taskName=?, taskAssignee=?, taskNote=?, taskStatus=?, taskDueDateAt=?, taskReminderAt=? WHERE taskId=?");
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
    public void flagTaskGroupTask(String taskId) {
        String sql = "UPDATE TaskGroupTask SET isDeleted=? WHERE taskId=?";
        jdbcTemplate.update(sql,true, taskId);
    }

    @Override
    public List<TaskGroupTaskUserResponseDto> getAllParentTasksWithProfile(String taskGroupId) {
        String sql = "SELECT * FROM TaskGroupTask as t " +
                "LEFT JOIN User AS u ON t.taskAssignee=u.userId " +
                "WHERE t.taskGroupId=? AND t.isDeleted=false AND t.isParent=true";
        List<TaskGroupTaskUserResponseDto> taskList = jdbcTemplate.query(sql, new TaskGroupTaskUserResponseDto(), taskGroupId);
        return  taskList;
    }

    @Override
    public List<TaskGroupTaskUserResponseDto> getAllChildTasksWithProfile(String taskGroupId) {
        String sql = "SELECT * FROM TaskGroupTask as t " +
                "LEFT JOIN User AS u ON t.taskAssignee=u.userId " +
                "WHERE t.taskGroupId=? AND t.isDeleted=false AND t.isParent=false";
        List<TaskGroupTaskUserResponseDto> taskList = jdbcTemplate.query(sql, new TaskGroupTaskUserResponseDto(), taskGroupId);
        return  taskList;
    }

    @Override
    public List<TaskGroupTaskUserResponseDto> getAllUserAssignedTasksWithProfile(String userId, String taskGroupId) {
        String sql = "SELECT * FROM TaskGroupTask as t " +
                "LEFT JOIN User AS u ON t.taskAssignee=u.userId " +
                "WHERE t.taskGroupId=? AND t.isDeleted=false";
        List<TaskGroupTaskUserResponseDto> taskList = jdbcTemplate.query(sql, new TaskGroupTaskUserResponseDto(), taskGroupId);
        return  taskList;
    }

    @Override
    public boolean checkChildTasksOfAParentTask(String taskId) {
        String sql = "SELECT * FROM TaskGroupTask WHERE parentId=? AND isDeleted=false";
        List<TaskGroupTask> children = jdbcTemplate.query(sql, new TaskGroupTask(), taskId);
        if (children.isEmpty())
            return false;
        else
            return true;
    }

    @Override
    public void transitionFromParentToChild(String taskId, TaskParentChildUpdateDto taskParentChildUpdateDto) {
        jdbcTemplate.update(connection -> {
            PreparedStatement preparedStatement = connection.prepareStatement("UPDATE TaskGroupTask SET parentId=?, isParent=? WHERE taskId=?");
            preparedStatement.setString(1, taskParentChildUpdateDto.getNewParent());
            preparedStatement.setBoolean(2, false);
            preparedStatement.setString(3, taskId);

            return preparedStatement;
        });
    }

    @Override
    public List<TaskGroupTask> getAllChildrenOfParentTask(String taskId) {
        String sql = "SELECT * FROM TaskGroupTask WHERE parentId=? AND isDeleted=false";
        List<TaskGroupTask> taskList = jdbcTemplate.query(sql, new TaskGroupTask(), taskId);
        return taskList;
    }

    @Override
    public List<TaskGroupTaskUserResponseDto> getAllChildrenOfParentTaskWithProfile(String taskId) {
        String sql = "SELECT * FROM TaskGroupTask AS TGT LEFT JOIN User AS U ON  TGT.taskAssignee = U.userId WHERE TGT.parentId=? AND TGT.isDeleted=false";
        List<TaskGroupTaskUserResponseDto> taskList = jdbcTemplate.query(sql, new TaskGroupTaskUserResponseDto(), taskId);
        return taskList;
    }


}
