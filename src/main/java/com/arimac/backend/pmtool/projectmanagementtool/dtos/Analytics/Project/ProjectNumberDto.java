package com.arimac.backend.pmtool.projectmanagementtool.dtos.Analytics.Project;

import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ProjectNumberDto implements RowMapper<ProjectNumberDto> {
    private int totalProjects;
    private int activeProjects;

    public ProjectNumberDto() {
    }

    public int getTotalProjects() {
        return totalProjects;
    }

    public void setTotalProjects(int totalProjects) {
        this.totalProjects = totalProjects;
    }

    public int getActiveProjects() {
        return activeProjects;
    }

    public void setActiveProjects(int activeProjects) {
        this.activeProjects = activeProjects;
    }

    public ProjectNumberDto(int totalProjects, int activeProjects) {
        this.totalProjects = totalProjects;
        this.activeProjects = activeProjects;
    }

    @Override
    public ProjectNumberDto mapRow(ResultSet resultSet, int i) throws SQLException {
        return new ProjectNumberDto(
                resultSet.getInt("totalProjects"),
                resultSet.getInt("activeProjects")
        );
    }
}
