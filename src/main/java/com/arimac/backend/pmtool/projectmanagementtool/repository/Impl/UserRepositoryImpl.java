package com.arimac.backend.pmtool.projectmanagementtool.repository.Impl;

import com.arimac.backend.pmtool.projectmanagementtool.dtos.UserProjectDto;
import com.arimac.backend.pmtool.projectmanagementtool.dtos.UserUpdateDto;
import com.arimac.backend.pmtool.projectmanagementtool.model.User;
import com.arimac.backend.pmtool.projectmanagementtool.repository.UserRepository;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.sql.PreparedStatement;
import java.util.List;

@Service
public class UserRepositoryImpl implements UserRepository {

    private final JdbcTemplate jdbcTemplate;

    public UserRepositoryImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Object createUser(User user) {
        jdbcTemplate.update(connection -> {
            PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO User(userId, firstName, lastName, email, idpUserId) values (?,?,?,?,?)");
            preparedStatement.setString(1, user.getUserId());
            preparedStatement.setString(2, user.getFirstName());
            preparedStatement.setString(3, user.getLastName());
            preparedStatement.setString(4, user.getEmail());
            preparedStatement.setString(5, user.getIdpUserId());

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
    public User getUserByUserId(String userId) {
        String sql = "SELECT * FROM User WHERE userId=?";
        User user = null;
        try {
            user = jdbcTemplate.queryForObject(sql,new User(), userId);
        } catch (EmptyResultDataAccessException e){
        }
        return user;
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
        String sql = "SELECT * FROM Project_User as pu LEFT JOIN User as u ON pu.assigneeId = u.userId LEFT JOIN ProjectRole as pr ON pu.assigneeProjectRole = pr.projectRoleId WHERE pu.projectId = ?";
        List<UserProjectDto> userProjectDtoList = jdbcTemplate.query(sql ,new UserProjectDto(), projectId);
        return userProjectDtoList;
    }

    @Override
    public Object getAllBlockedProjectUsers(String projectId) {
        String sql = "SELECT u.* FROM User AS u LEFT JOIN Project_User as pu ON pu.assigneeId = u.userId WHERE pu.projectId=? AND isBlocked=true";
        
        return null;
    }

    @Override
    public void addSlackIdToUser(String userId, String slackId) {
        jdbcTemplate.update(connection -> {
            PreparedStatement preparedStatement = connection.prepareStatement("UPDATE User SET userSlackId=? WHERE userId=?");
            preparedStatement.setString(1, slackId);
            preparedStatement.setString(2, userId);

            return preparedStatement;
        });
    }

}
