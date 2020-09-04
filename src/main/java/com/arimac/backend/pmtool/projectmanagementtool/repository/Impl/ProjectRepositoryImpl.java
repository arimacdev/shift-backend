package com.arimac.backend.pmtool.projectmanagementtool.repository.Impl;

import com.arimac.backend.pmtool.projectmanagementtool.dtos.Analytics.Project.ProjectSummaryDto;
import com.arimac.backend.pmtool.projectmanagementtool.dtos.Analytics.ProjectStatusCountDto;
import com.arimac.backend.pmtool.projectmanagementtool.dtos.Project.ProjectUserResponseDto;
import com.arimac.backend.pmtool.projectmanagementtool.enumz.WeightTypeEnum;
import com.arimac.backend.pmtool.projectmanagementtool.exception.PMException;
import com.arimac.backend.pmtool.projectmanagementtool.model.Project;
import com.arimac.backend.pmtool.projectmanagementtool.model.Project_User;
import com.arimac.backend.pmtool.projectmanagementtool.repository.ProjectRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
public class ProjectRepositoryImpl implements ProjectRepository {
    private static final String ALL = "all";

    private static final Logger logger = LoggerFactory.getLogger(ProjectRepositoryImpl.class);
    private final JdbcTemplate jdbcTemplate;
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;


    public ProjectRepositoryImpl(JdbcTemplate jdbcTemplate, NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    @Override
    public Project createProject(Project project) {
        jdbcTemplate.update(connection -> {
            PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO project(project, projectName, projectAlias, clientId, projectStartDate, projectEndDate, projectStatus, isDeleted, issueCount, weightMeasure) values (?,?,?,?,?,?,?,?,?,?)");
            preparedStatement.setString(1, project.getProjectId());
            preparedStatement.setString(2, project.getProjectName());
            preparedStatement.setString(3, project.getProjectAlias());
            preparedStatement.setString(4, project.getClientId());
            preparedStatement.setTimestamp(5,  new java.sql.Timestamp(project.getProjectStartDate().getTime()));
            preparedStatement.setTimestamp(6, new java.sql.Timestamp(project.getProjectEndDate().getTime()));
            preparedStatement.setString(7, project.getProjectStatus().toString());
            preparedStatement.setBoolean(8, project.getIsDeleted());
            preparedStatement.setInt(9, project.getIssueCount());
            preparedStatement.setInt(10, project.getWeightMeasure());

            return preparedStatement;
        });
        return project;
    }


    @Override
    public Project getProjectById(String projectId) {
        String sql = "SELECT * FROM project WHERE project=?";
        try {
            return jdbcTemplate.queryForObject(sql, new Project(), projectId);
        } catch (EmptyResultDataAccessException e){
            return null;
        }
    }

    @Override
    public Project findProjectByName(String name) {
        String sql = "SELECT * FROM project WHERE projectName=?";
        try {
            return jdbcTemplate.queryForObject(sql, new Project(), name);
        } catch (EmptyResultDataAccessException e){
            return null;
        }
    }

    @Override
    public List<String> getProjectTaskIds(String projectId) {
        String sql = "SELECT taskId from Task where projectId=?";
        return jdbcTemplate.queryForList(sql, new Object[] {projectId}, String.class);
    }

    @Override
    public ProjectUserResponseDto getProjectByIdAndUserId(String projectId, String userId) {
        String sql = "SELECT * FROM Project_User AS pu " +
                "LEFT JOIN project AS p ON pu.projectId=p.project" +
                " WHERE pu.assigneeId=? AND pu.projectId=? AND p.isDeleted=false AND pu.isBlocked=false";
        ProjectUserResponseDto project;
        try {
            project =  jdbcTemplate.queryForObject(sql, this.query , userId, projectId);
        } catch (EmptyResultDataAccessException e){
            logger.info("Error {}", e.getLocalizedMessage());
            return null;
        }
        return project;
    }

    @Override
    public Project_User getProjectUser(String projectId, String userId) {
        String sql = "SELECT * FROM Project_User WHERE assigneeId=? AND projectId=? AND isBlocked=false";
        Project_User project_user;
        try {
            project_user =  jdbcTemplate.queryForObject(sql, new Project_User(), userId, projectId);
        } catch (EmptyResultDataAccessException e){
            logger.info("Error {}", e.getLocalizedMessage());
            return null;
        }
        return project_user;
    }

    @Override
    public Project_User getProjectUserWithBlockedStatus(String projectId, String userId) {
        String sql = "SELECT * FROM Project_User WHERE assigneeId=? AND projectId=?";
        Project_User project_user;
        try {
            project_user =  jdbcTemplate.queryForObject(sql, new Project_User(), userId, projectId);
        } catch (EmptyResultDataAccessException e){
            logger.info("Error {}", e.getLocalizedMessage());
            return null;
        }
        return project_user;
    }

    @Override
    public List<ProjectUserResponseDto> getAllUserAssignedProjects(String userId) {
        String sql = "SELECT * FROM Project_User AS pu INNER JOIN project AS p ON pu.projectId=p.project WHERE pu.assigneeId=? AND p.isDeleted=false AND pu.isBlocked=false";
        List<ProjectUserResponseDto> projects =  jdbcTemplate.query(sql, this.query, userId);
        return projects;
    }

    @Override
    public void updateProject(Project project, String projectId) {
        logger.info("step 03 {}", project);
        jdbcTemplate.update(connection -> {
            PreparedStatement preparedStatement = connection.prepareStatement("UPDATE project SET projectName=?, clientId=?,  projectStartDate=?, projectEndDate=?, projectStatus=?, projectAlias=? WHERE project=?");
            preparedStatement.setString(1, project.getProjectName());
            preparedStatement.setString(2, project.getClientId());
            preparedStatement.setTimestamp(3,  new java.sql.Timestamp(project.getProjectStartDate().getTime()));
            preparedStatement.setTimestamp(4, new java.sql.Timestamp(project.getProjectEndDate().getTime()));
            preparedStatement.setString(5, project.getProjectStatus().toString());
            preparedStatement.setString(6, project.getProjectAlias());
            preparedStatement.setString(7, projectId);

            return preparedStatement;
        });
    }

    @Override
    public void assignUserToProject(String projectId, Project_User project_user) {
        try {
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
        } catch (Exception e){
            logger.info("Assign Error {} {}", projectId, e.getMessage());
        }

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
        String sql = "UPDATE project SET isDeleted=true WHERE project=?";
        jdbcTemplate.update(sql,projectId);
    }

    @Override
    public void unFlagProject(String projectId) {
        String sql = "UPDATE project SET isDeleted=false WHERE project=?";
        jdbcTemplate.update(sql, projectId);
    }

    @Override
    public void blockOrUnBlockProjectUser(String userId, String projectId, boolean status) {
        String sql = "UPDATE Project_User SET isBlocked=? WHERE projectId=? AND assigneeId=?";
        jdbcTemplate.update(sql, status, projectId, userId);
    }

    @Override
    public void blockOrUnblockUserFromAllRelatedProjects(boolean status, String userId) {
        String sql = "UPDATE Project_User SET isBlocked=? WHERE assigneeId=?";
        jdbcTemplate.update(sql, status, userId);
    }

    @Override
    public void updateIssueCount(String projectId, int issueId) {
        jdbcTemplate.update(connection -> {
            PreparedStatement preparedStatement = connection.prepareStatement("UPDATE project SET issueCount=? WHERE project=?");
            preparedStatement.setInt(1,issueId);
            preparedStatement.setString(2, projectId);

            return preparedStatement;
        });
    }

    @Override
    public boolean checkProjectAlias(String alias) {
        String sql = "SELECT EXISTS (SELECT * FROM project WHERE projectAlias=? LIMIT 1)";
        try {
            return jdbcTemplate.queryForObject(sql, Boolean.class, alias);
        } catch (Exception e){
            throw new PMException(e.getMessage());
        }
    }

    @Override
    public void updateProjectWeight(String projectId, WeightTypeEnum weightTypeEnum) {
        String sql = "UPDATE project SET weightMeasure=? WHERE project=?";
        try {
            jdbcTemplate.update(sql, weightTypeEnum.getWeightId(), projectId);
        } catch (Exception e){
            throw new PMException(e.getMessage());
        }
    }

    private WeightTypeEnum getWeightMeasure(int weightId){
        return WeightTypeEnum.get(weightId);
    }

    private RowMapper<ProjectUserResponseDto> query = (resultSet, i) -> {
        ProjectUserResponseDto projectUserResponseDto = new ProjectUserResponseDto();
        projectUserResponseDto.setProjectId(resultSet.getString("project"));
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
        projectUserResponseDto.setProjectAlias(resultSet.getString("projectAlias"));
        projectUserResponseDto.setWeightMeasure(getWeightMeasure(resultSet.getInt("weightMeasure")));
        return projectUserResponseDto;
    };


    @Override
    public List<Project> getAllProjects() {
        String sql = "SELECT * FROM project WHERE isDeleted=false";
        try {
            return jdbcTemplate.query(sql, new Project());
        } catch (Exception e){
            throw  new PMException(e.getMessage());
        }
    }

    @Override
    public int getActiveProjectCount(String from, String to) {
        String sql;
        try {
        if (from.equals(ALL) && to.equals(ALL)) {
            sql = "SELECT COUNT(*) FROM project WHERE isDeleted=false";
            return jdbcTemplate.queryForObject(sql, Integer.class);
        }
        else {
            sql = "SELECT COUNT(*) FROM project WHERE isDeleted=false AND projectStartDate BETWEEN ? AND ?";
            return jdbcTemplate.queryForObject(sql, new Object[]{from,to}, Integer.class);
        }
        } catch (Exception e){
            throw new PMException(e.getMessage());
        }
    }

     @Override
    public List<ProjectStatusCountDto> getActiveProjectCountByStatus(String from, String to) {
        String sql;
        try {
            if (from.equals(ALL) && to.equals(ALL)) {
                sql = "SELECT projectStatus, COUNT(*) as projectCount FROM project WHERE isDeleted=false GROUP BY projectStatus ORDER BY projectCount DESC";
                return jdbcTemplate.query(sql, new ProjectStatusCountDto());
            }
            else {
                sql = "SELECT projectStatus, COUNT(*) as projectCount FROM project WHERE isDeleted=false AND projectStartDate BETWEEN ? AND ? GROUP BY projectStatus ORDER BY projectCount DESC";
                return jdbcTemplate.query(sql, new ProjectStatusCountDto(), from, to);
            }
        } catch (Exception e){
            throw new PMException(e.getMessage());
        }
    }

    @Override
    public List<ProjectSummaryDto> getProjectSummary(String from, String to, Set<String> status, String key) {
        String sql;
        String betweenQuery = "";
        String statusQuery = "";
        String keyQuery = "";
        MapSqlParameterSource parameters = new MapSqlParameterSource();
        if (!from.equals(ALL) && !to.equals(ALL)) {
            betweenQuery = "AND projectStartDate BETWEEN :fromDate AND :toDate ";
            parameters.addValue("fromDate", from);
            parameters.addValue("toDate", to);
        }
        if (!status.contains(ALL)){
            statusQuery = "AND projectStatus IN (:statusList) ";
            parameters.addValue("statusList", status);
        }
        if (!key.equals(ALL) || this.findProjectByName(ALL) != null){
            keyQuery = "AND projectName LIKE :projectName ";
            parameters.addValue("projectName", "%" + key + "%");
        }
        sql = "SELECT COUNT(taskId) AS taskCount, projectName, COUNT(case when taskStatus = 'closed' then 1 end) AS closed " +
                "FROM project AS P LEFT JOIN Task T on P.project = T.projectId " +
                "WHERE (T.isDeleted = false OR P.isDeleted = false) " +
                betweenQuery +
                statusQuery +
                keyQuery +
                "GROUP BY projectName " +
                "ORDER BY taskCount DESC";

        return namedParameterJdbcTemplate.query(sql, parameters, new ProjectSummaryDto());


    }


}
