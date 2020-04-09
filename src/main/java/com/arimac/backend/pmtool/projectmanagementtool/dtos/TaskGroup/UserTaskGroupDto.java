package com.arimac.backend.pmtool.projectmanagementtool.dtos.TaskGroup;

import com.arimac.backend.pmtool.projectmanagementtool.dtos.UserProjectDto;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class UserTaskGroupDto implements RowMapper<UserTaskGroupDto> {
    private String taskGroupId;
    private String assigneeId;
    private String assigneeFirstName;
    private String assigneeLastName;
    private String assigneeProfileImage;
    private int taskGroupRole;

    public UserTaskGroupDto() {
    }

    public UserTaskGroupDto(String taskGroupId, String assigneeId, String assigneeFirstName, String assigneeLastName, String assigneeProfileImage, int taskGroupRole) {
        this.taskGroupId = taskGroupId;
        this.assigneeId = assigneeId;
        this.assigneeFirstName = assigneeFirstName;
        this.assigneeLastName = assigneeLastName;
        this.assigneeProfileImage = assigneeProfileImage;
        this.taskGroupRole = taskGroupRole;
    }

    public String getTaskGroupId() {
        return taskGroupId;
    }

    public void setTaskGroupId(String taskGroupId) {
        this.taskGroupId = taskGroupId;
    }

    public String getAssigneeId() {
        return assigneeId;
    }

    public void setAssigneeId(String assigneeId) {
        this.assigneeId = assigneeId;
    }

    public String getAssigneeFirstName() {
        return assigneeFirstName;
    }

    public void setAssigneeFirstName(String assigneeFirstName) {
        this.assigneeFirstName = assigneeFirstName;
    }

    public String getAssigneeLastName() {
        return assigneeLastName;
    }

    public void setAssigneeLastName(String assigneeLastName) {
        this.assigneeLastName = assigneeLastName;
    }

    public String getAssigneeProfileImage() {
        return assigneeProfileImage;
    }

    public void setAssigneeProfileImage(String assigneeProfileImage) {
        this.assigneeProfileImage = assigneeProfileImage;
    }

    public int getTaskGroupRole() {
        return taskGroupRole;
    }

    public void setTaskGroupRole(int taskGroupRole) {
        this.taskGroupRole = taskGroupRole;
    }

    @Override
    public UserTaskGroupDto mapRow(ResultSet resultSet, int i) throws SQLException {
        return new UserTaskGroupDto(
                resultSet.getString("taskGroupId"),
                resultSet.getString("taskGroupMemberId"),
                resultSet.getString("firstName"),
                resultSet.getString("lastName"),
                resultSet.getString("profileImage"),
                resultSet.getInt("taskGroupRole")
        );
    }


}
