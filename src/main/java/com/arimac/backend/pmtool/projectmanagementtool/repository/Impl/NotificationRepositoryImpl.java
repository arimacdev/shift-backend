package com.arimac.backend.pmtool.projectmanagementtool.repository.Impl;

import com.arimac.backend.pmtool.projectmanagementtool.dtos.Notification.TaskGroupTaskAlertDto;
import com.arimac.backend.pmtool.projectmanagementtool.dtos.NotificationUpdateDto;
import com.arimac.backend.pmtool.projectmanagementtool.dtos.TaskAlertDto;
import com.arimac.backend.pmtool.projectmanagementtool.model.Notification;
import com.arimac.backend.pmtool.projectmanagementtool.repository.NotificationRepository;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.sql.PreparedStatement;
import java.util.List;

@Service
public class NotificationRepositoryImpl implements NotificationRepository {

    private final JdbcTemplate jdbcTemplate;

    public NotificationRepositoryImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }


    @Override
    public void addTaskNotification(Notification notification) {
        jdbcTemplate.update(connection -> {
            PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO Notification(notificationId, assigneeId, taskId, taskDueDate, hourly, daily) values (?,?,?,?,?,?)");
            preparedStatement.setString(1, notification.getNotificationId());
            preparedStatement.setString(2, notification.getAssigneeId());
            preparedStatement.setString(3, notification.getTaskId());
            preparedStatement.setTimestamp(4, notification.getTaskDueDateAt());
            preparedStatement.setBoolean(5, notification.getIsHourly());
            preparedStatement.setBoolean(6, notification.getIsDaily());

            return preparedStatement;
        });
    }

    @Override
    public Notification getNotificationByTaskId(String taskId) {
        String sql = "SELECT * FROM Notification WHERE taskId=?";
        try {
            return jdbcTemplate.queryForObject(sql, new Notification(), taskId);
        } catch (EmptyResultDataAccessException e){
            return null;
        }
    }

    @Override
    public void updateTaskNotification(NotificationUpdateDto notificationUpdateDto) {
        jdbcTemplate.update(connection -> {
            PreparedStatement preparedStatement = connection.prepareStatement("UPDATE Notification SET hourly=?, daily=? WHERE taskId=?");
            preparedStatement.setBoolean(1, notificationUpdateDto.getIsHourly());
            preparedStatement.setBoolean(2, notificationUpdateDto.getIsDaily());
            preparedStatement.setString(3, notificationUpdateDto.getTaskId());

            return preparedStatement;
        });
    }

    @Override
    public List<TaskAlertDto> getTaskAlertList() {
        String sql = "SELECT * FROM Task as t\n" +
                "    INNER JOIN project as p ON t.projectId = p.project\n" +
                "    INNER JOIN Notification as n ON n.taskId = t.taskId\n" +
                "    INNER JOIN User as u ON t.taskAssignee = u.userId WHERE t.taskStatus !=? AND t.isDeleted=false AND u.userSlackId IS NOT NULL AND  u.notification = true AND (n.hourly = false)";
        return jdbcTemplate.query(sql, new TaskAlertDto(), "closed");
    }

    public List<TaskGroupTaskAlertDto> getTaskGroupTaskAlertList(){
        String sql = "SELECT * FROM TaskGroupTask as tgt\n" +
                "    INNER JOIN TaskGroup as tg ON tg.taskGroupId = tgt.taskGroupId\n" +
                "    INNER JOIN Notification as n ON n.taskId = tgt.taskId\n" +
                "    INNER JOIN User as u ON tgt.taskAssignee = u.userId WHERE tgt.taskStatus !=? AND tgt.isDeleted=false AND u.userSlackId IS NOT NULL AND  u.notification = true AND (n.hourly = false)";
        return jdbcTemplate.query(sql, new TaskGroupTaskAlertDto(), "closed");

    }

    @Override
    public void deleteNotification(String taskId) {
        String sql = "DELETE FROM Notification WHERE taskId=?";
        jdbcTemplate.update(sql, taskId);
    }
}
