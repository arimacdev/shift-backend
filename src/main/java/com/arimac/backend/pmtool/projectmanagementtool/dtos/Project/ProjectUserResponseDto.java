package com.arimac.backend.pmtool.projectmanagementtool.dtos.Project;

import com.arimac.backend.pmtool.projectmanagementtool.enumz.WeightTypeEnum;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

public class ProjectUserResponseDto implements RowMapper<ProjectUserResponseDto> {
    private String projectId;
    private String clientId;
    private String assigneeId;
    private Timestamp assignedAt;
    private String assigneeJobRole;
    private int assigneeProjectRole;
    private String projectName;
    private String projectStatus;
    private Timestamp projectStartDate;
    private Timestamp projectEndDate;
    private boolean isDeleted;
    private boolean blockedStatus;
    private String projectAlias;
    private WeightTypeEnum weightMeasure;
    private boolean isPinned;
    private boolean isSupportEnabled;
    private boolean isSupportAdded;

    public ProjectUserResponseDto() {
    }


    public ProjectUserResponseDto(String projectId, String clientId, String assigneeId, Timestamp assignedAt, String assigneeJobRole, int assigneeProjectRole, String projectName, String projectStatus, Timestamp projectStartDate, Timestamp projectEndDate, boolean isDeleted, boolean blockedStatus, String projectAlias, WeightTypeEnum weightMeasure, boolean isStarred, boolean isSupportEnabled, boolean isSupportAdded) {
        this.projectId = projectId;
        this.clientId = clientId;
        this.assigneeId = assigneeId;
        this.assignedAt = assignedAt;
        this.assigneeJobRole = assigneeJobRole;
        this.assigneeProjectRole = assigneeProjectRole;
        this.projectName = projectName;
        this.projectStatus = projectStatus;
        this.projectStartDate = projectStartDate;
        this.projectEndDate = projectEndDate;
        this.isDeleted = isDeleted;
        this.blockedStatus = blockedStatus;
        this.projectAlias = projectAlias;
        this.weightMeasure = weightMeasure;
        this.isPinned = isStarred;
        this.isSupportEnabled = isSupportEnabled;
        this.isSupportAdded = isSupportAdded;
    }

    public String getAssigneeJobRole() {
        return assigneeJobRole;
    }

    public void setAssigneeJobRole(String assigneeJobRole) {
        this.assigneeJobRole = assigneeJobRole;
    }

    public int getAssigneeProjectRole() {
        return assigneeProjectRole;
    }

    public void setAssigneeProjectRole(int assigneeProjectRole) {
        this.assigneeProjectRole = assigneeProjectRole;
    }

    public Timestamp getProjectStartDate() {
        return projectStartDate;
    }

    public void setProjectStartDate(Timestamp projectStartDate) {
        this.projectStartDate = projectStartDate;
    }

    public Timestamp getProjectEndDate() {
        return projectEndDate;
    }

    public void setProjectEndDate(Timestamp projectEndDate) {
        this.projectEndDate = projectEndDate;
    }

    public String getAssigneeId() {
        return assigneeId;
    }

    public void setAssigneeId(String assigneeId) {
        this.assigneeId = assigneeId;
    }

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    public Timestamp getAssignedAt() {
        return assignedAt;
    }

    public void setAssignedAt(Timestamp assignedAt) {
        this.assignedAt = assignedAt;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getProjectStatus() {
        return projectStatus;
    }

    public void setProjectStatus(String projectStatus) {
        this.projectStatus = projectStatus;
    }

    public boolean getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(boolean deleted) {
        isDeleted = deleted;
    }

    public boolean getBlockedStatus() {
        return blockedStatus;
    }

    public void setBlockedStatus(boolean blockedStatus) {
        this.blockedStatus = blockedStatus;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getProjectAlias() {
        return projectAlias;
    }

    public void setProjectAlias(String projectAlias) {
        this.projectAlias = projectAlias;
    }

    public WeightTypeEnum getWeightMeasure() {
        return weightMeasure;
    }

    public void setWeightMeasure(WeightTypeEnum weightMeasure) {
        this.weightMeasure = weightMeasure;
    }

    private WeightTypeEnum getWeightMeasureOf(int weightId){
        return WeightTypeEnum.get(weightId);
    }

    public boolean getIsStarred() {
        return isPinned;
    }

    public void setIsStarred(boolean starred) {
        isPinned = starred;
    }

    public boolean getIsSupportEnabled() {
        return isSupportEnabled;
    }

    public void setIsSupportEnabled(boolean supportEnabled) {
        isSupportEnabled = supportEnabled;
    }

    public boolean getIsSupportAdded() {
        return isSupportAdded;
    }

    public void setIsSupportAdded(boolean supportAdded) {
        isSupportAdded = supportAdded;
    }

    @Override
    public ProjectUserResponseDto mapRow(ResultSet resultSet, int i) throws SQLException {
        return new ProjectUserResponseDto(
                resultSet.getString("projectId"),
                resultSet.getString("clientId"),
                resultSet.getString("assigneeId"),
                resultSet.getTimestamp("assignedAt"),
                resultSet.getString("assigneeJobRole"),
                resultSet.getInt("assigneeProjectRole"),
                resultSet.getString("projectName"),
                resultSet.getString("projectStatus"),
                resultSet.getTimestamp("projectStartDate"),
                resultSet.getTimestamp("projectEndDate"),
                resultSet.getBoolean("isDeleted"),
                resultSet.getBoolean("blockedStatus"),
                resultSet.getString("projectAlias"),
                getWeightMeasureOf(resultSet.getInt("weightMeasure")),
                resultSet.getBoolean("isPinned"),
                resultSet.getBoolean("isSupportEnabled"),
                resultSet.getBoolean("isSupportAdded"));
    }

    @Override
    public String toString() {
        return "ProjectUserResponseDto{" +
                "projectId='" + projectId + '\'' +
                ", clientId='" + clientId + '\'' +
                ", assigneeId='" + assigneeId + '\'' +
                ", assignedAt=" + assignedAt +
                ", assigneeJobRole='" + assigneeJobRole + '\'' +
                ", assigneeProjectRole=" + assigneeProjectRole +
                ", projectName='" + projectName + '\'' +
                ", projectStatus='" + projectStatus + '\'' +
                ", projectStartDate=" + projectStartDate +
                ", projectEndDate=" + projectEndDate +
                ", isDeleted=" + isDeleted +
                ", blockedStatus=" + blockedStatus +
                ", projectAlias='" + projectAlias + '\'' +
                ", weightMeasure=" + weightMeasure +
                '}';
    }
}
