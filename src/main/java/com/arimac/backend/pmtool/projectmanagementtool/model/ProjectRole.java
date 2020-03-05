package com.arimac.backend.pmtool.projectmanagementtool.model;


import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ProjectRole  implements RowMapper<ProjectRole> {
    private int projectRoleId;
    private String projectRoleName;

    public ProjectRole() {
    }

    public ProjectRole(int projectRoleId, String projectRoleName) {
        this.projectRoleId = projectRoleId;
        this.projectRoleName = projectRoleName;
    }

    public int getProjectRoleId() {
        return projectRoleId;
    }

    public void setProjectRoleId(int projectRoleId) {
        this.projectRoleId = projectRoleId;
    }

    public String getProjectRoleName() {
        return projectRoleName;
    }

    public void setProjectRoleName(String projectRoleName) {
        this.projectRoleName = projectRoleName;
    }

    @Override
    public ProjectRole mapRow(ResultSet resultSet, int i) throws SQLException {
        return new ProjectRole(
                resultSet.getInt("projectRoleId"),
                resultSet.getString("projectRoleName")
        );
    }
}
