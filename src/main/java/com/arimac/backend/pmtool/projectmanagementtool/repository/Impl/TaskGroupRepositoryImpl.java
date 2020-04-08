package com.arimac.backend.pmtool.projectmanagementtool.repository.Impl;

import com.arimac.backend.pmtool.projectmanagementtool.dtos.TaskGroup.TaskGroupUpdateDto;
import com.arimac.backend.pmtool.projectmanagementtool.dtos.TaskGroup.TaskGroup_MemberResponseDto;
import com.arimac.backend.pmtool.projectmanagementtool.model.TaskGroup;
import com.arimac.backend.pmtool.projectmanagementtool.model.TaskGroup_Member;
import com.arimac.backend.pmtool.projectmanagementtool.repository.TaskGroupRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.sql.PreparedStatement;
import java.util.List;

@Service
public class TaskGroupRepositoryImpl implements TaskGroupRepository {
    private static final Logger logger = LoggerFactory.getLogger(TaskGroupRepositoryImpl.class);

    private final JdbcTemplate jdbcTemplate;

    public TaskGroupRepositoryImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Object createTaskGroup(TaskGroup taskGroup) {
        jdbcTemplate.update(connection -> {
            PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO TaskGroup (taskGroupId, taskGroupName, taskGroupCreatedAt, isDeleted) VALUES (?,?,?,?)");
            preparedStatement.setString(1, taskGroup.getTaskGroupId());
            preparedStatement.setString(2, taskGroup.getTaskGroupName());
            preparedStatement.setTimestamp(3, taskGroup.getTaskGroupCreatedAt());
            preparedStatement.setBoolean(4, false);

            return preparedStatement;
        });
        return taskGroup;
    }

    @Override
    public Object assignMemberToTaskGroup(TaskGroup_Member assignment) {
        jdbcTemplate.update(connection -> {
            PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO TaskGroup_Member (taskGroupId, taskGroupMemberId, TaskGroupRole, isBlocked, memberAssignedAt) VALUES (?,?,?,?,?)");
            preparedStatement.setString(1, assignment.getTaskGroupId());
            preparedStatement.setString(2, assignment.getTaskGroupMemberId());
            preparedStatement.setInt(3, assignment.getTaskGroupRole());
            preparedStatement.setBoolean(4, assignment.isBlocked());
            preparedStatement.setTimestamp(5, assignment.getMemberAssignedAt());

            return preparedStatement;
        });
        return assignment;
    }

    @Override
    public TaskGroup_Member getTaskGroupMemberByTaskGroup(String userId, String taskGroupId) {
        String sql = "SELECT * FROM TaskGroup_Member WHERE taskGroupMemberId=? AND taskGroupId=? AND isDeleted=false AND isBlocked=false";
        TaskGroup_Member member;
        try {
            member = jdbcTemplate.queryForObject(sql, new TaskGroup_Member(), userId, taskGroupId);
        } catch (EmptyResultDataAccessException e){
            return null;
        }
        return member;
    }

    @Override
    public Object getAllTaskGroupsByUser(String userId) {
        String sql = "SELECT * FROM TaskGroup_Member WHERE taskGroupMemberId=?";
        return jdbcTemplate.query(sql, new TaskGroup_Member(), userId);
    }

    @Override
    public List<TaskGroup_MemberResponseDto> getAllTaskGroupsWithGroup(String userId) {
        String sql = "SELECT * FROM TaskGroup_Member AS TGM " +
                "INNER JOIN TaskGroup AS TG ON TGM.taskGroupId = TG.taskGroupId " +
                "WHERE TGM.taskGroupMemberId=?";
        return jdbcTemplate.query(sql, new TaskGroup_MemberResponseDto(), userId);
    }

    @Override
    public void updateTaskGroup(String taskGroupId, TaskGroupUpdateDto taskGroupUpdateDto) {
        String sql = "UPDATE TaskGroup SET taskGroupName=? WHERE taskgroupId=?";
        jdbcTemplate.update(sql,taskGroupUpdateDto.getTaskGroupName(), taskGroupId);
    }

    @Override
    public void flagTaskGroup(String taskGroupId) {
        String sql = "UPDATE TaskGroup SET isDeleted=? WHERE taskgroupId=?";
        jdbcTemplate.update(sql, true, taskGroupId);
    }
}
