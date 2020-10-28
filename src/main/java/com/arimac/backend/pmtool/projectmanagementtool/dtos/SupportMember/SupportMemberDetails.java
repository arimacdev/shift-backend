package com.arimac.backend.pmtool.projectmanagementtool.dtos.SupportMember;

import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

public class SupportMemberDetails implements RowMapper<SupportMemberDetails> {
    private String projectId;
    private String assigneeId;
    private Timestamp assignedAt;
    private String assignedBy;
    private boolean isEnabled;

    private String firstName;
    private String lastName;
    private String profileImage;

    public SupportMemberDetails() {
    }

    public SupportMemberDetails(String projectId, String assigneeId, Timestamp assignedAt, String assignedBy, boolean isEnabled, String firstName, String lastName, String profileImage) {
        this.projectId = projectId;
        this.assigneeId = assigneeId;
        this.assignedAt = assignedAt;
        this.assignedBy = assignedBy;
        this.isEnabled = isEnabled;
        this.firstName = firstName;
        this.lastName = lastName;
        this.profileImage = profileImage;
    }

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    public String getAssigneeId() {
        return assigneeId;
    }

    public void setAssigneeId(String assigneeId) {
        this.assigneeId = assigneeId;
    }

    public Timestamp getAssignedAt() {
        return assignedAt;
    }

    public void setAssignedAt(Timestamp assignedAt) {
        this.assignedAt = assignedAt;
    }

    public String getAssignedBy() {
        return assignedBy;
    }

    public void setAssignedBy(String assignedBy) {
        this.assignedBy = assignedBy;
    }

    public boolean isEnabled() {
        return isEnabled;
    }

    public void setEnabled(boolean enabled) {
        isEnabled = enabled;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }

    @Override
    public SupportMemberDetails mapRow(ResultSet resultSet, int i) throws SQLException {
        return new SupportMemberDetails(
                resultSet.getString("projectId"),
                resultSet.getString("assigneeId"),
                resultSet.getTimestamp("assignedAt"),
                resultSet.getString("assignedBy"),
                resultSet.getBoolean("isEnabled"),
                resultSet.getString("firstName"),
                resultSet.getString("lastName"),
                resultSet.getString("profileImage")
        );
    }
}
