package com.arimac.backend.pmtool.projectmanagementtool.dtos.Analytics.Project;

import com.arimac.backend.pmtool.projectmanagementtool.enumz.ProjectStatusEnum;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ProjectStatusCountDto implements RowMapper<ProjectStatusCountDto> {
    private ProjectStatusEnum projectStatus;
    private int  projectCount;


    public ProjectStatusCountDto() {
        this.projectCount = 0; ///////////////check here
    }

    public ProjectStatusCountDto(ProjectStatusEnum projectStatus, int projectCount) {
        this.projectStatus = projectStatus;
        this.projectCount = projectCount;
    }

    public ProjectStatusEnum getProjectStatus() {
        return projectStatus;
    }

    public void setProjectStatus(ProjectStatusEnum projectStatus) {
        this.projectStatus = projectStatus;
    }

    public int getProjectCount() {
        return projectCount;
    }

    public void setProjectCount(int projectCount) {
        this.projectCount = projectCount;
    }

    @Override
    public ProjectStatusCountDto mapRow(ResultSet resultSet, int i) throws SQLException {
        return new ProjectStatusCountDto(
                ProjectStatusEnum.valueOf(resultSet.getString("projectStatus")),
                resultSet.getInt("projectCount")
        );
    }
}
