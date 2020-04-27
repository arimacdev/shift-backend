package com.arimac.backend.pmtool.projectmanagementtool.repository.Impl;

import com.arimac.backend.pmtool.projectmanagementtool.dtos.*;
import com.arimac.backend.pmtool.projectmanagementtool.dtos.Sprint.TaskSprintUpdateDto;
import com.arimac.backend.pmtool.projectmanagementtool.enumz.TaskTypeEnum;
import com.arimac.backend.pmtool.projectmanagementtool.model.Task;
import com.arimac.backend.pmtool.projectmanagementtool.repository.TaskRepository;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.sql.PreparedStatement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Service
public class TaskRepositoryImpl implements TaskRepository {

    private final JdbcTemplate jdbcTemplate;

    public TaskRepositoryImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private static final String ALL= "all";

    @Override
    public Task addTaskToProject(Task task) {
        jdbcTemplate.update(connection -> {
            PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO Task (taskId, projectId, taskName, taskInitiator, taskAssignee, taskNote, taskStatus, taskCreatedAt, taskDueDateAt, taskReminderAt, isDeleted, taskType, sprintId, issueType, parentId, isParent) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
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
            preparedStatement.setString(12, task.getTaskType().toString());
            preparedStatement.setString(13, task.getSprintId());
            preparedStatement.setString(14, task.getIssueType().toString());
            preparedStatement.setString(15, task.getParentId());
            preparedStatement.setBoolean(16, task.getIsParent());

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
        String sql = "SELECT * FROM Task as t " +
                "LEFT JOIN User AS u ON t.taskAssignee=u.userId " +
                "WHERE t.projectId=? AND t.isDeleted=false";
        List<TaskUserResponseDto> taskList = jdbcTemplate.query(sql, new TaskUserResponseDto(), projectId);
        return  taskList;
    }

    @Override
    public List<TaskUserResponseDto> getAllParentTasksWithProfile(String projectId) {
        String sql = "SELECT * FROM Task as t " +
                "LEFT JOIN User AS u ON t.taskAssignee=u.userId " +
                "WHERE t.projectId=? AND t.isDeleted=false AND t.isParent=true";
        List<TaskUserResponseDto> taskList = jdbcTemplate.query(sql, new TaskUserResponseDto(), projectId);
        return  taskList;
    }

    @Override
    public List<TaskUserResponseDto> getAllChildTasksWithProfile(String projectId) {
        String sql = "SELECT * FROM Task as t " +
                "LEFT JOIN User AS u ON t.taskAssignee=u.userId " +
                "WHERE t.projectId=? AND t.isDeleted=false AND t.isParent=false";
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
        String sql = "SELECT * FROM Task as t " +
                "LEFT JOIN User AS u ON t.taskAssignee=u.userId " +
                "WHERE t.projectId=? AND t.taskAssignee=? AND t.isDeleted=false";
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
    // PERSONAL TASKS and TASK GROUP
    @Override
    public List<Task> getAllPersonalTasks(String userId) {
        String sql = "SELECT * FROM Task WHERE taskAssignee=? AND taskType=? AND isDeleted=false";
        List<Task> personalTaskList = jdbcTemplate.query(sql, new Task(), userId,TaskTypeEnum.personal.toString());
        return personalTaskList;
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
    public List<WorkLoadTaskStatusDto> getAllUsersWithTaskCompletion() {
//        String sql = "SELECT * FROM User AS u LEFT JOIN Task AS t on u.userId = t.taskAssignee LEFT JOIN project AS p ON t.projectId = p.projectId WHERE t.isDeleted = false AND p.isDeleted=false";
        String sql = "SELECT * FROM User AS u LEFT JOIN Task AS t on u.userId = t.taskAssignee LEFT JOIN project AS p ON t.projectId = p.projectId WHERE (t.isDeleted = false OR t.isDeleted IS NULL ) AND (p.isDeleted=false OR p.isDeleted IS NULL)";
        List<WorkLoadTaskStatusDto> workLoadList = jdbcTemplate.query(sql, new WorkLoadTaskStatusDto());
        return workLoadList;
    }

    @Override
    public List<WorkLoadProjectDto> getAllUserAssignedTaskWithCompletion(String userId, String from, String to) {
        String sql;
        if (from.equals(ALL) || to.equals(ALL)) {
             sql = "SELECT * FROM Project_User AS pu\n" +
                    "        LEFT JOIN Task AS t ON (t.projectId = pu.projectId)\n" +
                    "        INNER JOIN project p on pu.projectId = p.projectId\n" +
                    "WHERE (pu.assigneeId=?) AND (p.isDeleted=false) AND (t.isDeleted = false OR t.isDeleted IS NULL )";
            return jdbcTemplate.query(sql, new WorkLoadProjectDto(), userId);
        } else {
            sql = "SELECT * FROM Task AS t\n" +
                    "INNER JOIN project p on t.projectId = p.projectId\n" +
                    "WHERE (t.taskAssignee=?) AND (p.isDeleted=false) AND (t.isDeleted = false OR t.isDeleted IS NULL )" +
                    "AND (t.taskDueDateAt BETWEEN ? AND ?)";
            return jdbcTemplate.query(sql, new WorkLoadProjectDto(), userId, from, to);
        }
    }

    @Override
    public void updateProjectTaskSprint(String taskId, TaskSprintUpdateDto taskSprintUpdateDto) {
        jdbcTemplate.update(connection -> {
            PreparedStatement preparedStatement = connection.prepareStatement("UPDATE Task SET sprintId=? WHERE taskId=?");
            preparedStatement.setString(1, taskSprintUpdateDto.getNewSprint());
            preparedStatement.setString(2, taskId);

            return preparedStatement;
        });
    }



}
