package com.arimac.backend.pmtool.projectmanagementtool.repository.Impl;

import com.arimac.backend.pmtool.projectmanagementtool.model.UserNotification;
import com.arimac.backend.pmtool.projectmanagementtool.repository.UserNotificationRepository;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.sql.PreparedStatement;

@Service
public class UserNotificationRepositoryImpl implements UserNotificationRepository {
    private final JdbcTemplate jdbcTemplate;

    public UserNotificationRepositoryImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void registerForNotifications(UserNotification userNotification) {
        jdbcTemplate.update(connection -> {
            PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO UserNotification(userId, subscriptionId, provider, notificationStatus) VALUES (?,?,?,?)");
            preparedStatement.setString(1, userNotification.getUserId());
            preparedStatement.setString(2, userNotification.getSubscriptionId());
            preparedStatement.setString(3, userNotification.getProvider());
            preparedStatement.setBoolean(4, userNotification.getNotificationStatus());

            return preparedStatement;
        });
    }
}
