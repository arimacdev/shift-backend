package com.arimac.backend.pmtool.projectmanagementtool.dtos.Analytics.Project;

import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ProjectSummaryDto implements RowMapper<ProjectSummaryDto> {
    private String projectName;
    private int totalTasks;
    private int completedTaskCount;

    public ProjectSummaryDto() {
    }

    public ProjectSummaryDto(String projectName, int totalTasks, int completedTaskCount) {
        this.projectName = projectName;
        this.totalTasks = totalTasks;
        this.completedTaskCount = completedTaskCount;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public int getTotalTasks() {
        return totalTasks;
    }

    public void setTotalTasks(int totalTasks) {
        this.totalTasks = totalTasks;
    }

    public int getCompletedTaskCount() {
        return completedTaskCount;
    }

    public void setCompletedTaskCount(int completedTaskCount) {
        this.completedTaskCount = completedTaskCount;
    }

    @Override
    public ProjectSummaryDto mapRow(ResultSet resultSet, int i) throws SQLException {
        return new ProjectSummaryDto(
                resultSet.getString("projectName"),
                resultSet.getInt("taskCount"),
                resultSet.getInt("closed")
        );
    }
}
