package com.arimac.backend.pmtool.projectmanagementtool.repository.Impl;

import com.arimac.backend.pmtool.projectmanagementtool.model.Notification;
import com.arimac.backend.pmtool.projectmanagementtool.repository.NotificationRepository;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.sql.PreparedStatement;

@Service
public class NotificationRepositoryImpl implements NotificationRepository {

    private final JdbcTemplate jdbcTemplate;

    public NotificationRepositoryImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void addSlackIdToUser(Notification notification) {
       jdbcTemplate.update(connection -> {
           PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO Notification(userId, userSlackId, notificationStatus) VALUES (?,?,?)");
           preparedStatement.setString(1, notification.getUserId());
           preparedStatement.setString(2, notification.getUserSlackId());
           preparedStatement.setBoolean(3, notification.getNotificationStatus());

           return preparedStatement;
       });
    }
}
