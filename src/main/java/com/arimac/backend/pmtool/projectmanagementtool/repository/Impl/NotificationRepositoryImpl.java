package com.arimac.backend.pmtool.projectmanagementtool.repository.Impl;

import com.arimac.backend.pmtool.projectmanagementtool.dtos.NotificationUpdateDto;
import com.arimac.backend.pmtool.projectmanagementtool.dtos.TaskAlertDto;
import com.arimac.backend.pmtool.projectmanagementtool.model.Notification;
import com.arimac.backend.pmtool.projectmanagementtool.repository.NotificationRepository;
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
//        String sql = "SELECT * FROM Task as t LEFT JOIN project as p ON t.projectId = p.projectId LEFT JOIN User as u ON t.taskAssignee = u.userId WHERE t.taskStatus !=? AND t.isDeleted=false";
//        String sql = "SELECT * FROM Task as t LEFT JOIN project as p ON t.projectId = p.projectId LEFT JOIN User as u ON t.taskAssignee = u.userId WHERE t.taskStatus !=? AND t.isDeleted=false AND u.userSlackId IS NOT NULL and  u.notification = true";
        String sql = "SELECT * FROM Task as t\n" +
                "    INNER JOIN project as p ON t.projectId = p.projectId\n" +
                "    INNER JOIN Notification as n ON n.taskId = t.taskId\n" +
                "    INNER JOIN User as u ON t.taskAssignee = u.userId WHERE t.taskStatus !=? AND t.isDeleted=false AND u.userSlackId IS NOT NULL AND  u.notification = true AND (n.daily = false OR n.hourly = false)";
        return jdbcTemplate.query(sql, new TaskAlertDto(), "closed");
    }

    @Override
    public void deleteNotification(String taskId) {
        String sql = "DELETE FROM Notification WHERE taskId=?";
        jdbcTemplate.update(sql, taskId);
    }
}
