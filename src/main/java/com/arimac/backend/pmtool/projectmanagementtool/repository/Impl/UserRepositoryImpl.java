package com.arimac.backend.pmtool.projectmanagementtool.repository.Impl;

import com.arimac.backend.pmtool.projectmanagementtool.dtos.Analytics.User.UserActivityDto;
import com.arimac.backend.pmtool.projectmanagementtool.dtos.Analytics.User.UserDetailedAnalysis;
import com.arimac.backend.pmtool.projectmanagementtool.dtos.Analytics.User.UserNumberDto;
import com.arimac.backend.pmtool.projectmanagementtool.dtos.Project_UserDto;
import com.arimac.backend.pmtool.projectmanagementtool.dtos.SlackNotificationDto;
import com.arimac.backend.pmtool.projectmanagementtool.dtos.TaskGroup.UserTaskGroupDto;
import com.arimac.backend.pmtool.projectmanagementtool.dtos.UserProjectDto;
import com.arimac.backend.pmtool.projectmanagementtool.dtos.UserUpdateDto;
import com.arimac.backend.pmtool.projectmanagementtool.enumz.AnalyticsEnum.ChartCriteriaEnum;
import com.arimac.backend.pmtool.projectmanagementtool.enumz.AnalyticsEnum.UserDetailsEnum;
import com.arimac.backend.pmtool.projectmanagementtool.enumz.FilterOrderEnum;
import com.arimac.backend.pmtool.projectmanagementtool.exception.PMException;
import com.arimac.backend.pmtool.projectmanagementtool.model.User;
import com.arimac.backend.pmtool.projectmanagementtool.repository.UserRepository;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

@Service
public class UserRepositoryImpl implements UserRepository {

    private final JdbcTemplate jdbcTemplate;
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    private static final String ALL = "all";


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
        String sql = "SELECT * FROM User WHERE isActive=true";
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
    public User getUserByIdpUserId(String idpUserId) {
        String sql = "SELECT * FROM User WHERE idpUserId=?";
        try {
            return jdbcTemplate.queryForObject(sql, new User(), idpUserId);
        } catch (EmptyResultDataAccessException e){
            return null;
        } catch (Exception e){
            throw new PMException(e.getMessage());
        }
    }

    @Override
    public User getUserWithFlag(String userId) {
        String sql = "SELECT * FROM User WHERE userId=? AND isActive=false";
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
    public List<Project_UserDto> getAllProjectUsers(String projectId) {
        String sql = "SELECT u.*, pu.isBlocked FROM User AS u LEFT JOIN Project_User as pu ON pu.assigneeId = u.userId WHERE pu.projectId=? AND isBlocked=false";
        return jdbcTemplate.query(sql, new Project_UserDto(), projectId);
    }

    @Override
    public List<UserProjectDto> getUsersProjectDetails(String projectId) {
        String sql = "SELECT * FROM Project_User as pu " +
                "LEFT JOIN User as u ON pu.assigneeId = u.userId " +
                "LEFT JOIN ProjectRole as pr ON pu.assigneeProjectRole = pr.projectRoleId " +
                "WHERE pu.projectId = ? AND u.isActive=true";
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

    @Override
    public UserNumberDto getActiveUserCount(String from, String to) {
        String sql = "SELECT COUNT(*) AS totalUsers, COUNT(case when userSlackId IS NOT NULL then 1 end) AS slackActivated FROM User WHERE isActive=true";
        try {
            return jdbcTemplate.queryForObject(sql, new UserNumberDto());
        } catch (Exception e){
            throw new PMException(e.getMessage());
        }
    }

    @Override
    public List<UserDetailedAnalysis> getDetailedUserDetails(UserDetailsEnum orderBy, FilterOrderEnum orderType, int startIndex, int limit, Set<String> userList) {
        String baseQuery = "SELECT userId, firstName , lastName, profileImage,idpUserId," +
                "(SELECT COUNT(Project_User.projectId) FROM Project_User INNER JOIN project ON project.project = Project_User.projectId  WHERE project.isDeleted = false AND Project_User.assigneeId = User.userId AND Project_User.isBlocked = false) AS projectCount," +
                "(SELECT COUNT(DISTINCT(Task.projectId)) FROM Task INNER JOIN project ON project.project = Task.projectId WHERE project.isDeleted=false AND Task.taskAssignee = User.userId AND Task.isDeleted =false) as activeProjectCount," +
                "(SELECT COUNT(taskGroupId) FROM TaskGroup_Member WHERE TaskGroup_Member.taskGroupMemberId =  User.userId AND TaskGroup_Member.isDeleted = false) as taskGroupCount," +
                "(SELECT COUNT(taskId) FROM Task WHERE Task.taskAssignee = User.userId AND Task.isDeleted = false) as assignedTasks," +
                "(SELECT COUNT(taskId) FROM TaskGroupTask WHERE TaskGroupTask.taskAssignee = User.userId AND TaskGroupTask.isDeleted = false) as taskGroupTaskCount," +
                "(SELECT COUNT(taskId) FROM PersonalTask WHERE PersonalTask.taskAssignee = User.userId AND PersonalTask.isDeleted =false) as personalTaskCount" +
                " FROM User WHERE User.isActive = true";
        MapSqlParameterSource parameters = new MapSqlParameterSource();
        parameters.addValue("limit", limit);
        parameters.addValue("offset", startIndex);
        if (userList.contains(ALL)){
            return namedParameterJdbcTemplate.query(baseQuery + " ORDER BY " + orderBy.toString() + " " + orderType.toString() + " LIMIT :limit OFFSET :offset", parameters, new UserDetailedAnalysis());
        } else {
            parameters.addValue("userIds", userList);
            return namedParameterJdbcTemplate.query(baseQuery + " AND userId IN (:userIds)" + " ORDER BY " + orderBy.toString() + " " + orderType.toString() + " LIMIT :limit OFFSET :offset", parameters, new UserDetailedAnalysis());
        }

    }

    @Override
    public HashMap<String,UserActivityDto> getUserActivity(String from, String to, ChartCriteriaEnum criteria) {
        String sql;
        String dateFormat;
        if (criteria.equals(ChartCriteriaEnum.DAY))
            dateFormat = "DATE_FORMAT(actionTimestamp,'%Y-%m-%d') ";
        else if (criteria.equals(ChartCriteriaEnum.MONTH))
            dateFormat = "DATE_FORMAT(actionTimestamp,'%Y-%m') ";
        else
            dateFormat = "DATE_FORMAT(actionTimestamp,'%Y') ";
        if (from.equals(ALL) && to.equals(ALL)) {
            sql= "SELECT " + dateFormat +  "AS date," +
                    "COUNT(DISTINCT(actor)) as totalActiveMemberCount," +
                    "COUNT(DISTINCT actor, case when updatedvalue = 'closed' then 1 end) as totalTaskCompletionMemberCount" +
                    " FROM ActivityLog WHERE isDeleted=false " +
                    "GROUP BY " + dateFormat;
            //return jdbcTemplate.query(sql, new UserActivityDto());
            return jdbcTemplate.query(sql, (ResultSet rs) -> {
                HashMap<String,UserActivityDto> dateCountMap = new HashMap<>();
                while (rs.next()) {
                    dateCountMap.put(rs.getString("date"), new UserActivityDto( rs.getString("date"), rs.getInt("totalActiveMemberCount"), rs.getInt("totalTaskCompletionMemberCount")));
                }
                return dateCountMap;
            });
        } else {
            sql= "SELECT " + dateFormat +  "AS date," +
                    "COUNT(DISTINCT(actor)) as totalActiveMemberCount," +
                    "COUNT(DISTINCT actor, case when updatedvalue = 'closed' then 1 end) as totalTaskCompletionMemberCount" +
                    " FROM ActivityLog WHERE isDeleted=false AND" + " actionTimestamp BETWEEN ? AND ? " +
                    "GROUP BY " + dateFormat;
//            return jdbcTemplate.query(sql, new UserActivityDto(), from, to);
            return jdbcTemplate.query(sql, new Object[] { from, to }, (ResultSet rs) -> {
                HashMap<String,UserActivityDto> dateCountMap = new HashMap<>();
                while (rs.next()) {
                    dateCountMap.put(rs.getString("date"), new UserActivityDto( rs.getString("date"), rs.getInt("totalActiveMemberCount"), rs.getInt("totalTaskCompletionMemberCount")));
                }
                return dateCountMap;
            });
        }

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
