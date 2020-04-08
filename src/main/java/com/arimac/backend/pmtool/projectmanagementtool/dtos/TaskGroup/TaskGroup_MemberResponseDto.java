package com.arimac.backend.pmtool.projectmanagementtool.dtos.TaskGroup;

import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

public class TaskGroup_MemberResponseDto implements RowMapper<TaskGroup_MemberResponseDto> {
    private String taskGroupId;
    private String taskGroupName;
    private Timestamp taskGroupCreatedAt;
    private String taskGroupMemberId;
    private int TaskGroupRole;
    private boolean isBlocked;
    private Timestamp memberAssignedAt;

    public TaskGroup_MemberResponseDto() {
    }

    public TaskGroup_MemberResponseDto(String taskGroupId, String taskGroupName, Timestamp taskGroupCreatedAt, String taskGroupMemberId, int taskGroupRole, boolean isBlocked, Timestamp memberAssignedAt) {
        this.taskGroupId = taskGroupId;
        this.taskGroupName = taskGroupName;
        this.taskGroupCreatedAt = taskGroupCreatedAt;
        this.taskGroupMemberId = taskGroupMemberId;
        TaskGroupRole = taskGroupRole;
        this.isBlocked = isBlocked;
        this.memberAssignedAt = memberAssignedAt;
    }

    public String getTaskGroupId() {
        return taskGroupId;
    }

    public void setTaskGroupId(String taskGroupId) {
        this.taskGroupId = taskGroupId;
    }

    public String getTaskGroupName() {
        return taskGroupName;
    }

    public void setTaskGroupName(String taskGroupName) {
        this.taskGroupName = taskGroupName;
    }

    public Timestamp getTaskGroupCreatedAt() {
        return taskGroupCreatedAt;
    }

    public void setTaskGroupCreatedAt(Timestamp taskGroupCreatedAt) {
        this.taskGroupCreatedAt = taskGroupCreatedAt;
    }

    public String getTaskGroupMemberId() {
        return taskGroupMemberId;
    }

    public void setTaskGroupMemberId(String taskGroupMemberId) {
        this.taskGroupMemberId = taskGroupMemberId;
    }

    public int getTaskGroupRole() {
        return TaskGroupRole;
    }

    public void setTaskGroupRole(int taskGroupRole) {
        TaskGroupRole = taskGroupRole;
    }

    public boolean isBlocked() {
        return isBlocked;
    }

    public void setBlocked(boolean blocked) {
        isBlocked = blocked;
    }

    public Timestamp getMemberAssignedAt() {
        return memberAssignedAt;
    }

    public void setMemberAssignedAt(Timestamp memberAssignedAt) {
        this.memberAssignedAt = memberAssignedAt;
    }

    @Override
    public TaskGroup_MemberResponseDto mapRow(ResultSet resultSet, int i) throws SQLException {
        return new TaskGroup_MemberResponseDto(
                resultSet.getString("taskGroupId"),
                resultSet.getString("taskGroupName"),
                resultSet.getTimestamp("taskGroupCreatedAt"),
                resultSet.getString("taskGroupMemberId"),
                resultSet.getInt("TaskGroupRole"),
                resultSet.getBoolean("isBlocked"),
                resultSet.getTimestamp("memberAssignedAt")
        );
    }
}
