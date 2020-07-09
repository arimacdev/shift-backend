package com.arimac.backend.pmtool.projectmanagementtool.repository.Impl;

import com.arimac.backend.pmtool.projectmanagementtool.dtos.SlackNotificationDto;
import com.arimac.backend.pmtool.projectmanagementtool.dtos.TaskGroup.UserTaskGroupDto;
import com.arimac.backend.pmtool.projectmanagementtool.dtos.UserProjectDto;
import com.arimac.backend.pmtool.projectmanagementtool.dtos.UserUpdateDto;
import com.arimac.backend.pmtool.projectmanagementtool.exception.PMException;
import com.arimac.backend.pmtool.projectmanagementtool.model.User;
import com.arimac.backend.pmtool.projectmanagementtool.repository.UserRepository;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;

import java.sql.PreparedStatement;
import java.util.List;

@Service
public class UserRepositoryImpl implements UserRepository {

    private final JdbcTemplate jdbcTemplate;
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;


    public UserRepositoryImpl(JdbcTemplate jdbcTemplate, NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    @Override
    public Object createUser(User user) {
        jdbcTemplate.update(connection -> {
            PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO User(userId, firstName, lastName, email, idpUserId, isActive, username) values (?,?,?,?,?,?,?)");
            preparedStatement.setString(1, user.getUserId());
            preparedStatement.setString(2, user.getFirstName());
            preparedStatement.setString(3, user.getLastName());
            preparedStatement.setString(4, user.getEmail());
            preparedStatement.setString(5, user.getIdpUserId());
            preparedStatement.setBoolean(6, true);
            preparedStatement.setString(7, user.getUsername());

            return preparedStatement;
        });
        return user;
    }

    @Override
    public List<User> getAllUsers() {
        String sql = "SELECT * FROM User";
        List<User> userList = jdbcTemplate.query(sql, new User());
        return userList;
    }

    @Override
    public List<User> getUserListById(List<String> userId) {
        String sql = "SELECT * FROM User WHERE userId IN (:ids) AND isActive=true";
        MapSqlParameterSource parameters = new MapSqlParameterSource();
        parameters.addValue("ids", userId);
        try {
            return namedParameterJdbcTemplate.query(sql, parameters, new User());
        } catch (Exception e){
            throw new PMException(e.getMessage());
        }
    }

    @Override
    public List<User> getAllUsersWithPagination(int limit, int offset) {
        String sql = "SELECT * FROM User WHERE isActive=true ORDER BY firstName LIMIT ? OFFSET ?";
        try {
            return jdbcTemplate.query(sql, new User(), limit, offset);
        } catch (Exception e){
            throw new PMException(e.getMessage());
        }
    }

    @Override
    public User getUserByUserId(String userId) {
        String sql = "SELECT * FROM User WHERE userId=? AND isActive=true";
        User user = null;
        try {
            user = jdbcTemplate.queryForObject(sql,new User(), userId);
        } catch (EmptyResultDataAccessException e){
        }
        return user;
    }

    @Override
    public User getUserWithFlag(String userId) {
        return null;
    }

    @Override
    public Object updateUserByUserId(String userId, UserUpdateDto userUpdateDto) {
        jdbcTemplate.update(connection -> {
            PreparedStatement preparedStatement = connection.prepareStatement("UPDATE User SET firstName=?, lastName=?, email=? WHERE userId=?");
            preparedStatement.setString(1, userUpdateDto.getFirstName());
            preparedStatement.setString(2, userUpdateDto.getLastName());
            preparedStatement.setString(3, userUpdateDto.getEmail());
            preparedStatement.setString(4, userId);

            return preparedStatement;
        });

        return  userUpdateDto;
    }

    @Override
    public void updateProfilePicture(String userId, String profilePictureUrl) {
        jdbcTemplate.update(connection -> {
            PreparedStatement preparedStatement = connection.prepareStatement("UPDATE User SET profileImage=? WHERE userId=?");
            preparedStatement.setString(1, profilePictureUrl);
            preparedStatement.setString(2, userId);

            return preparedStatement;
        });
    }

    @Override
    public List<User> getAllProjectUsers(String projectId) {
        String sql = "SELECT u.* FROM User AS u LEFT JOIN Project_User as pu ON pu.assigneeId = u.userId WHERE pu.projectId=? AND isBlocked=false";
        List<User> userList = jdbcTemplate.query(sql, new User(), projectId);
        return userList;
    }

    @Override
    public List<UserProjectDto> getUsersProjectDetails(String projectId) {
        String sql = "SELECT * FROM Project_User as pu " +
                "LEFT JOIN User as u ON pu.assigneeId = u.userId " +
                "LEFT JOIN ProjectRole as pr ON pu.assigneeProjectRole = pr.projectRoleId " +
                "WHERE pu.projectId = ? AND pu.isBlocked=false";
        List<UserProjectDto> userProjectDtoList = jdbcTemplate.query(sql ,new UserProjectDto(), projectId);
        return userProjectDtoList;
    }

    @Override
    public List<UserTaskGroupDto> getUsersTaskGroupDetails(String taskGroupId) {
        String sql = "SELECT * FROM TaskGroup_Member AS TGM INNER JOIN User AS U ON TGM.taskGroupMemberId=U.userId WHERE TGM.taskGroupId=?";
        List<UserTaskGroupDto> taskGroupDtoList = jdbcTemplate.query(sql, new UserTaskGroupDto(), taskGroupId);
        return taskGroupDtoList;
    }

    @Override
    public Object getAllBlockedProjectUsers(String projectId) {
        String sql = "SELECT u.* FROM User AS u LEFT JOIN Project_User" +
                " as pu ON pu.assigneeId = u.userId WHERE pu.projectId=?" +
                " AND isBlocked=true";
        return null;
    }

    @Override
    public void addSlackIdToUser(String userId, String slackId) {
        jdbcTemplate.update(connection -> {
            PreparedStatement preparedStatement = connection.prepareStatement("UPDATE User SET userSlackId=?, notification=? WHERE userId=?");
            preparedStatement.setString(1, slackId);
            preparedStatement.setBoolean(2, true);
            preparedStatement.setString(3, userId);

            return preparedStatement;
        });
    }

    @Override
    public void updateNotificationStatus(String userId, SlackNotificationDto slackNotificationDto) {
        jdbcTemplate.update(connection -> {
            PreparedStatement preparedStatement = connection.prepareStatement("UPDATE User SET notification=? WHERE userId=?");
            preparedStatement.setBoolean(1, slackNotificationDto.getNotificationStatus());
            preparedStatement.setString(2, userId);

            return preparedStatement;
    });
    }

    @Override
    public void changeUserUpdateStatus(String userId, boolean status) {
        jdbcTemplate.update(connection -> {
            PreparedStatement preparedStatement = connection.prepareStatement("UPDATE User SET isActive=? WHERE userId=?");
            preparedStatement.setBoolean(1, status);
            preparedStatement.setString(2, userId);

            return preparedStatement;
        });
    }


    //REMOVE

    @Override
    public void updateUserName(String userId, String userName) {
        jdbcTemplate.update(connection -> {
            PreparedStatement preparedStatement = connection.prepareStatement("UPDATE User SET username=? WHERE userId=?");
            preparedStatement.setString(1, userName);
            preparedStatement.setString(2, userId);

            return preparedStatement;
        });

    }

}
