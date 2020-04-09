package com.arimac.backend.pmtool.projectmanagementtool.model;

import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

public class TaskGroup_Member implements RowMapper<TaskGroup_Member> {
    private String taskGroupId;
    private String taskGroupMemberId;
    private int TaskGroupRole;
    private boolean isBlocked;
    private Timestamp memberAssignedAt;

    public TaskGroup_Member() {
    }

    public TaskGroup_Member(String taskGroupId, String taskGroupMemberId, int taskGroupRole, boolean isBlocked, Timestamp memberAssignedAt) {
        this.taskGroupId = taskGroupId;
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

    public String getTaskGroupMemberId() {
        return taskGroupMemberId;
    }

    public void setTaskGroupMemberId(String taskGroupMemberId) {
        this.taskGroupMemberId = taskGroupMemberId;
    }

    public Timestamp getMemberAssignedAt() {
        return memberAssignedAt;
    }

    public void setMemberAssignedAt(Timestamp memberAssignedAt) {
        this.memberAssignedAt = memberAssignedAt;
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

    @Override
    public TaskGroup_Member mapRow(ResultSet resultSet, int i) throws SQLException {
        return new TaskGroup_Member(
                resultSet.getString("taskGroupId"),
                resultSet.getString("taskGroupMemberId"),
                resultSet.getInt("TaskGroupRole"),
                resultSet.getBoolean("isBlocked"),
                resultSet.getTimestamp("memberAssignedAt")
        );
    }
}
