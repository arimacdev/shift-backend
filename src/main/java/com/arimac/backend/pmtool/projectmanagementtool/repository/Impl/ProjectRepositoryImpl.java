package com.arimac.backend.pmtool.projectmanagementtool.repository.Impl;

import com.arimac.backend.pmtool.projectmanagementtool.dtos.ProjectUserResponseDto;
import com.arimac.backend.pmtool.projectmanagementtool.model.Project;
import com.arimac.backend.pmtool.projectmanagementtool.model.Project_User;
import com.arimac.backend.pmtool.projectmanagementtool.repository.ProjectRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;

import java.sql.PreparedStatement;
import java.util.List;

@Service
public class ProjectRepositoryImpl implements ProjectRepository {

    private static final Logger logger = LoggerFactory.getLogger(ProjectRepositoryImpl.class);


    private final JdbcTemplate jdbcTemplate;

    public ProjectRepositoryImpl(JdbcTemplate jdbcTemplate) {
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
            preparedStatement.setString(6, project.getProjectStatus().toString());

            return preparedStatement;
        });
        return project;
    }

    @Override
    public ProjectUserResponseDto getProjectByIdAndUserId(String projectId, String userId) {
        String sql = "SELECT * FROM Project_User AS pu LEFT JOIN project AS p ON pu.projectId=p.projectId WHERE pu.assigneeId=? AND pu.projectId=? AND p.isDeleted=false AND pu.isBlocked=false";
        ProjectUserResponseDto project;
        try {
            project =  jdbcTemplate.queryForObject(sql, this.query, userId, projectId);
        } catch (EmptyResultDataAccessException e){
            logger.info("Error {}", e.getLocalizedMessage());
            return null;
        }
        return project;
    }

    @Override
    public List<ProjectUserResponseDto> getAllProjects() {
        String sql = "SELECT * FROM Project_User AS pu LEFT JOIN project AS p ON pu.projectId=p.projectId AND p.isDeleted=false AND pu.isBlocked=false";
        List<ProjectUserResponseDto> projects =  jdbcTemplate.query(sql, this.query);
        return  projects;
    }

    @Override
    public List<ProjectUserResponseDto> getAllProjectsByUser(String userId) {
        String sql = "SELECT * FROM Project_User AS pu LEFT JOIN project AS p ON pu.projectId=p.projectId WHERE pu.assigneeId=? AND p.isDeleted=false AND pu.isBlocked=false";
        List<ProjectUserResponseDto> projects =  jdbcTemplate.query(sql, this.query, userId);
        return projects;
    }

    @Override
    public void updateProject(Project project, String projectId) {
        jdbcTemplate.update(connection -> {
            PreparedStatement preparedStatement = connection.prepareStatement("UPDATE project SET projectName=?, clientId=?,  projectStartDate=?, projectEndDate=?, projectStatus=? WHERE projectId=?");
            preparedStatement.setString(1, project.getProjectName());
            preparedStatement.setString(2, project.getClientId());
            preparedStatement.setTimestamp(3, project.getProjectStartDate());
            preparedStatement.setTimestamp(4, project.getProjectEndDate());
            preparedStatement.setString(5, project.getProjectStatus().toString());
            preparedStatement.setString(6, projectId);

            return preparedStatement;
        });
    }

    @Override
    public void assignUserToProject(String projectId, Project_User project_user) {
       jdbcTemplate.update(connection -> {
           PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO Project_User(projectId, assigneeId, assignedAt, assigneeJobRole, assigneeProjectRole, isBlocked) values (?,?,?,?,?,?)");
           preparedStatement.setString(1, project_user.getProjectId());
           preparedStatement.setString(2, project_user.getAssigneeId());
           preparedStatement.setTimestamp(3, project_user.getAssignedAt());
           preparedStatement.setString(4, project_user.getAssigneeJobRole());
           preparedStatement.setInt(5, project_user.getAssigneeProjectRole());
           preparedStatement.setBoolean(6, project_user.getIsBlocked());

           return preparedStatement;
       });

    }

    @Override
    public void updateAssigneeProjectRole(Project_User project_user) {
        jdbcTemplate.update(connection -> {
            PreparedStatement preparedStatement = connection.prepareStatement("UPDATE Project_User SET projectId=?, assigneeId=?, assignedAt=?, assigneeJobRole=?, assigneeProjectRole=? WHERE projectId=? AND assigneeId=?");
            preparedStatement.setString(1, project_user.getProjectId());
            preparedStatement.setString(2, project_user.getAssigneeId());
            preparedStatement.setTimestamp(3, project_user.getAssignedAt());
            preparedStatement.setString(4, project_user.getAssigneeJobRole());
            preparedStatement.setInt(5, project_user.getAssigneeProjectRole());
            preparedStatement.setString(6, project_user.getProjectId());
            preparedStatement.setString(7, project_user.getAssigneeId());

            return preparedStatement;
        });
    }

    public void removeProjectAssignee(String projectId, String assigneeId){
        String sql = "DELETE from Project_User WHERE projectId=? AND assigneeId=?";
        jdbcTemplate.update(sql, projectId, assigneeId);
    }

    @Override
    public void flagProject(String projectId) {
        String sql = "UPDATE project SET isDeleted=true WHERE projectId=?";
        jdbcTemplate.update(sql,projectId);
    }

    @Override
    public void unFlagProject(String projectId) {
        String sql = "UPDATE project SET isDeleted=false WHERE projectId=?";
        jdbcTemplate.update(sql, projectId);
    }

    @Override
    public void blockOrUnBlockProjectUser(String userId, String projectId, boolean status) {
        String sql = "update Project_User SET isBlocked=? WHERE projectId=? AND assigneeId=?";
        jdbcTemplate.update(sql, status, projectId, userId);
    }

    private RowMapper<ProjectUserResponseDto> query = (resultSet, i) -> {
        ProjectUserResponseDto projectUserResponseDto = new ProjectUserResponseDto();
        projectUserResponseDto.setProjectId(resultSet.getString("projectId"));
        projectUserResponseDto.setProjectName(resultSet.getString("projectName"));
        projectUserResponseDto.setClientId(resultSet.getString("clientId"));
        projectUserResponseDto.setProjectStartDate(resultSet.getTimestamp("projectStartDate"));
        projectUserResponseDto.setProjectEndDate(resultSet.getTimestamp("projectEndDate"));
        projectUserResponseDto.setProjectStatus(resultSet.getString("projectStatus"));
        projectUserResponseDto.setIsDeleted(resultSet.getBoolean("isDeleted"));
        projectUserResponseDto.setAssignedAt(resultSet.getTimestamp("assignedAt"));
        projectUserResponseDto.setAssigneeId(resultSet.getString("assigneeId"));
        projectUserResponseDto.setAssigneeJobRole(resultSet.getString("assigneeJobRole"));
        projectUserResponseDto.setAssigneeProjectRole(resultSet.getInt("assigneeProjectRole"));
        projectUserResponseDto.setBlockedStatus(resultSet.getBoolean("isBlocked"));
        return projectUserResponseDto;
    };


}
