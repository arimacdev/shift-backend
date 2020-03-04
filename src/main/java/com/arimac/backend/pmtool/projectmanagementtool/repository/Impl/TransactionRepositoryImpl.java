package com.arimac.backend.pmtool.projectmanagementtool.repository.Impl;

import com.arimac.backend.pmtool.projectmanagementtool.dtos.ProjectUserResponseDto;
import com.arimac.backend.pmtool.projectmanagementtool.model.Project;
import com.arimac.backend.pmtool.projectmanagementtool.model.Project_User;
import com.arimac.backend.pmtool.projectmanagementtool.repository.TransactionRepository;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;

import java.sql.PreparedStatement;
import java.util.List;

@Service
public class TransactionRepositoryImpl implements TransactionRepository {

    private final JdbcTemplate jdbcTemplate;

    public TransactionRepositoryImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Project createProject(Project project) {
        jdbcTemplate.update(connection -> {
            PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO project(projectId, projectName, clientId, projectStartDate, projectEndDate, projectStatus) values (?,?,?,?,?,?)");
            preparedStatement.setString(1, project.getProjectId());
            preparedStatement.setString(2, project.getProjectName());
            preparedStatement.setString(3, project.getClientId());
            preparedStatement.setTimestamp(4, project.getProjectStartDate());
            preparedStatement.setTimestamp(5, project.getProjectEndDate());
            preparedStatement.setString(6, project.getProjectStatus());

            return preparedStatement;
        });
        return project;
    }

    @Override
    public List<ProjectUserResponseDto> getAllProjects() {
        String sql = "SELECT * FROM Project_User AS pu LEFT JOIN project AS p ON pu.projectId=p.projectId";
        List<ProjectUserResponseDto> projects =  jdbcTemplate.query(sql, this.query);
        return  projects;
    }

    @Override
    public List<ProjectUserResponseDto> getAllProjectsByUser(String userId) {
        String sql = "SELECT * FROM Project_User AS pu LEFT JOIN project AS p ON pu.projectId=p.projectId WHERE pu.userId=?";
        List<ProjectUserResponseDto> projects =  jdbcTemplate.query(sql, this.query, userId);
        return projects;
    }

    private RowMapper query = (resultSet, i) -> {
        ProjectUserResponseDto projectUserResponseDto = new ProjectUserResponseDto();
        projectUserResponseDto.setProjectId(resultSet.getString("projectId"));
        projectUserResponseDto.setProjectName(resultSet.getString("projectName"));
        projectUserResponseDto.setAssignedAt(resultSet.getTimestamp("assignedAt"));
        projectUserResponseDto.setUserId(resultSet.getString("userId"));
        projectUserResponseDto.setProjectStatus(resultSet.getString("projectStatus"));

        return projectUserResponseDto;
    };


}
