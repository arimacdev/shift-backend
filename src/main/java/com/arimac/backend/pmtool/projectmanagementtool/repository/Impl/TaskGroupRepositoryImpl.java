package com.arimac.backend.pmtool.projectmanagementtool.repository.Impl;

import com.arimac.backend.pmtool.projectmanagementtool.model.TaskGroup;
import com.arimac.backend.pmtool.projectmanagementtool.model.TaskGroup_Member;
import com.arimac.backend.pmtool.projectmanagementtool.repository.TaskGroupRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.sql.PreparedStatement;

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
            PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO TaskGroup (taskGroupId, taskGroupName, taskGroupCreatedAt) VALUES (?,?,?)");
            preparedStatement.setString(1, taskGroup.getTaskGroupId());
            preparedStatement.setString(2, taskGroup.getTaskGroupName());
            preparedStatement.setTimestamp(3, taskGroup.getTaskGroupCreatedAt());

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
}
