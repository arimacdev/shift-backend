package com.arimac.backend.pmtool.projectmanagementtool.repository.Impl;

import com.arimac.backend.pmtool.projectmanagementtool.Service.Impl.NotificationServiceImpl;
import com.arimac.backend.pmtool.projectmanagementtool.exception.PMException;
import com.arimac.backend.pmtool.projectmanagementtool.model.UserNotification;
import com.arimac.backend.pmtool.projectmanagementtool.repository.UserNotificationRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.sql.PreparedStatement;

@Service
public class UserNotificationRepositoryImpl implements UserNotificationRepository {
    private static final Logger logger = LoggerFactory.getLogger(UserNotificationRepositoryImpl.class);

    private final JdbcTemplate jdbcTemplate;

    public UserNotificationRepositoryImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void registerForNotifications(UserNotification userNotification) {
        jdbcTemplate.update(connection -> {
            PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO UserNotification(userId, subscriptionId, provider, notificationStatus, platform) VALUES (?,?,?,?,?)");
            preparedStatement.setString(1, userNotification.getUserId());
            preparedStatement.setString(2, userNotification.getSubscriptionId());
            preparedStatement.setString(3, userNotification.getProvider());
            preparedStatement.setBoolean(4, userNotification.getNotificationStatus());
            preparedStatement.setString(5, userNotification.getPlatform());

            return preparedStatement;
        });
    }
    @Override
    public UserNotification getNotificationUserByProviderAndStatus(String userId, String provider, boolean status) {
        String sql = "SELECT * FROM UserNotification WHERE userId=? AND provider=? AND notificationStatus=?";
        try {
            return jdbcTemplate.queryForObject(sql, new UserNotification(), userId, provider, status);
        } catch (EmptyResultDataAccessException e) {
           return null;
        }
    }
}
