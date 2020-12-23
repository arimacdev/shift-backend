package com.arimac.backend.pmtool.projectmanagementtool.repository.Impl;

import com.arimac.backend.pmtool.projectmanagementtool.dtos.Analytics.Project.ProjectDetailAnalysis;
import com.arimac.backend.pmtool.projectmanagementtool.dtos.Analytics.Project.ProjectNumberDto;
import com.arimac.backend.pmtool.projectmanagementtool.dtos.Analytics.Project.ProjectStatusCountDto;
import com.arimac.backend.pmtool.projectmanagementtool.dtos.Analytics.Project.ProjectSummaryDto;
import com.arimac.backend.pmtool.projectmanagementtool.dtos.Internal.Support.ProjectDetails;
import com.arimac.backend.pmtool.projectmanagementtool.dtos.Project.ProjectDto;
import com.arimac.backend.pmtool.projectmanagementtool.dtos.Project.ProjectKeys;
import com.arimac.backend.pmtool.projectmanagementtool.dtos.Project.ProjectPinUnPin;
import com.arimac.backend.pmtool.projectmanagementtool.dtos.Project.ProjectUserResponseDto;
import com.arimac.backend.pmtool.projectmanagementtool.dtos.ServiceDesk.SupportProjectResponse;
import com.arimac.backend.pmtool.projectmanagementtool.dtos.User.UserDto;
import com.arimac.backend.pmtool.projectmanagementtool.enumz.AnalyticsEnum.ProjectDetailsEnum;
import com.arimac.backend.pmtool.projectmanagementtool.enumz.AnalyticsEnum.ProjectSummaryTypeEnum;
import com.arimac.backend.pmtool.projectmanagementtool.enumz.FilterOrderEnum;
import com.arimac.backend.pmtool.projectmanagementtool.enumz.ProjectStatusEnum;
import com.arimac.backend.pmtool.projectmanagementtool.enumz.WeightTypeEnum;
import com.arimac.backend.pmtool.projectmanagementtool.exception.PMException;
import com.arimac.backend.pmtool.projectmanagementtool.model.Project;
import com.arimac.backend.pmtool.projectmanagementtool.model.Project_Keys;
import com.arimac.backend.pmtool.projectmanagementtool.model.Project_User;
import com.arimac.backend.pmtool.projectmanagementtool.model.User;
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
import java.util.*;

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
            PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO project(project, projectName, projectAlias, clientId, projectStartDate, projectEndDate, projectStatus, isDeleted, issueCount, weightMeasure, isSupportEnabled, isSupportAdded) values (?,?,?,?,?,?,?,?,?,?,?,?)");
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
            preparedStatement.setBoolean(11,false);
            preparedStatement.setBoolean(12,false);

            return preparedStatement;
        });
        return project;
    }


    @Override
    public Project getProjectById(String projectId) {
        String sql = "SELECT * FROM project WHERE project=? AND isDeleted=false";
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
        String sql = "SELECT taskId from Task where projectId=? AND isDeleted=false";
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
    public void pinUnpinProjects(ProjectPinUnPin projectPinUnPin) {
        String sql = "UPDATE Project_User SET isPinned=? WHERE assigneeId=? AND projectId=? AND isBlocked=false";
        try {
            jdbcTemplate.update(sql, projectPinUnPin.getIsPin(), projectPinUnPin.getUser(), projectPinUnPin.getProject());
        } catch (Exception e){
            throw new PMException(e.getCause());
        }
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
            PreparedStatement preparedStatement = connection.prepareStatement("UPDATE project SET projectName=?, clientId=?,  projectStartDate=?, projectEndDate=?, projectStatus=?, projectAlias=?, isSupportEnabled=? WHERE project=?");
            preparedStatement.setString(1, project.getProjectName());
            preparedStatement.setString(2, project.getClientId());
            preparedStatement.setTimestamp(3,  new java.sql.Timestamp(project.getProjectStartDate().getTime()));
            preparedStatement.setTimestamp(4, new java.sql.Timestamp(project.getProjectEndDate().getTime()));
            preparedStatement.setString(5, project.getProjectStatus().toString());
            preparedStatement.setString(6, project.getProjectAlias());
            preparedStatement.setString(8, projectId);
            preparedStatement.setBoolean(7, project.getIsSupportEnabled());

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
        projectUserResponseDto.setIsStarred(resultSet.getBoolean("isPinned"));
        projectUserResponseDto.setProjectAlias(resultSet.getString("projectAlias"));
        projectUserResponseDto.setWeightMeasure(getWeightMeasure(resultSet.getInt("weightMeasure")));
        projectUserResponseDto.setIsSupportAdded(resultSet.getBoolean("isSupportAdded"));
        projectUserResponseDto.setIsSupportEnabled(resultSet.getBoolean("isSupportEnabled"));
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
        if (from.equals(ALL) && to.equals(ALL)) {
            sql = "SELECT COUNT(*) FROM project WHERE isDeleted=false";
            return jdbcTemplate.queryForObject(sql, Integer.class);
        } else {
            sql = "SELECT COUNT(*) FROM project WHERE isDeleted=false AND projectStartDate BETWEEN ? AND ?";
            return jdbcTemplate.queryForObject(sql, new Object[]{from, to}, Integer.class);

        }
    }


    @Override
    public ProjectNumberDto getProjectNumbers(String from, String to) {
         String sql = "SELECT COUNT(*) as totalProjects," +
                "COUNT(case when projectStatus  in (:statusList) then 1 end) as activeProjects" +
                " FROM project WHERE isDeleted=false";
        try {
            MapSqlParameterSource parameters = new MapSqlParameterSource();
            Set<String> statusL = new HashSet<>();
            statusL.add(ProjectStatusEnum.ongoing.toString());
            statusL.add(ProjectStatusEnum.support.toString());
            statusL.add(ProjectStatusEnum.finished.toString());
            parameters.addValue("statusList", statusL);
            if (from.equals(ALL) && to.equals(ALL)) {
            return namedParameterJdbcTemplate.queryForObject(sql, parameters, new ProjectNumberDto());
        }
        else {
            parameters.addValue("fromDate", from);
            parameters.addValue("toDate", to);
            return namedParameterJdbcTemplate.queryForObject(sql + " AND projectStartDate BETWEEN :fromDate AND :toDate", parameters, new ProjectNumberDto());
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
    public List<ProjectSummaryDto> getProjectSummary(String from, String to, Set<String> status, Set<String> projectList, ProjectSummaryTypeEnum orderBy, FilterOrderEnum orderType,int startIndex, int limit) {
        String sql;
        String betweenQuery = "";
        String statusQuery = "";
        String keyQuery = "";
        String orderByQuery = "";
        if (orderBy.equals(ProjectSummaryTypeEnum.completed))
            orderByQuery = "closed";
        else if (orderBy.equals(ProjectSummaryTypeEnum.total))
            orderByQuery = "taskCount";
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
        if (!projectList.contains(ALL)){
            keyQuery = "AND project IN (:projectList)";
            parameters.addValue("projectList", projectList);
        }
        sql = "SELECT COUNT(taskId) AS taskCount, projectName, projectStatus, COUNT(case when taskStatus = 'closed' then 1 end) AS closed " +
                "FROM project AS P LEFT JOIN Task T on P.project = T.projectId " +
                "WHERE (T.isDeleted = false OR P.isDeleted = false) " +
                betweenQuery +
                statusQuery +
                keyQuery +
                "GROUP BY projectName, projectStatus " +
                "ORDER BY " + orderByQuery + " " + orderType.toString() +
                " LIMIT :limit OFFSET :offset";
        parameters.addValue("limit", limit);
        parameters.addValue("offset", startIndex);
        return namedParameterJdbcTemplate.query(sql, parameters, new ProjectSummaryDto());
    }

    @Override
    public LinkedHashMap<String, ProjectDetailAnalysis> getDetailedProjectDetails(String from, String to, ProjectDetailsEnum orderBy, FilterOrderEnum orderType, int startIndex, int limit) {
        String resultsOrderingQuery  = "";
        switch (orderBy){
            case taskcount:
                resultsOrderingQuery = "(SELECT * FROM project WHERE isDeleted = false) as limitProject INNER JOIN (SELECT projectId FROM Task WHERE isDeleted=false GROUP BY projectId ORDER BY COUNT(*) DESC LIMIT ? OFFSET ?) as P ON P.projectId = limitProject.project";
                break;
            case timeTaken:
                resultsOrderingQuery = "(SELECT * FROM project WHERE isDeleted=false) as limitProject INNER JOIN (SELECT project FROM project WHERE isDeleted=false GROUP BY project ORDER BY DATEDIFF(CURDATE(), projectStartDate) DESC LIMIT ? OFFSET ?) as P ON P.project = limitProject.project";
                break;
            case memberCount:
                resultsOrderingQuery = "(SELECT * FROM project WHERE isDeleted=false) as limitProject INNER JOIN (SELECT projectId FROM Project_User WHERE isBlocked = false GROUP BY projectId ORDER BY COUNT(*) DESC LIMIT ? OFFSET ?) as P ON P.projectId = limitProject.project";
                break;
            default:
                resultsOrderingQuery = "(SELECT * FROM project WHERE isDeleted = false) as limitProject INNER JOIN (SELECT project FROM project WHERE isDeleted=false ORDER BY " + orderBy.toString() + " " + orderType.toString() + " LIMIT ? OFFSET ?) as P ON P.project = limitProject.project";

        }
        String baseQuery ="SELECT limitProject.project, limitProject.projectName, limitProject.projectStartDate, limitProject.projectStatus, userId, firstName, lastName, profileImage," +
                " (SELECT COUNT(case when taskStatus = 'closed' then 1 end ) FROM Task WHERE Task.projectId = limitProject.project AND taskCreatedAt BETWEEN ? AND ?) as closedCount," +
                "(SELECT COUNT(*) FROM Task WHERE Task.projectId = limitProject.project";
        String timeFilter = " AND taskCreatedAt BETWEEN ? AND ?";
        String latterQuery = ") as taskcount," +
                "(SELECT COUNT(*) FROM Project_User WHERE Project_User.projectId = limitProject.project AND Project_User.isBlocked = false) as memberCount," +
                "DATEDIFF(CURDATE(), projectStartDate) as timeTaken " +
                "FROM " +
                resultsOrderingQuery +
                " LEFT JOIN Project_User AS PU ON PU.projectId=limitProject.project " +
                "LEFT JOIN User AS U ON U.userId = PU.assigneeId" +
                " WHERE PU.assigneeProjectRole = 1 AND U.isActive = true";
        if (!from.equals(ALL) && !to.equals(ALL)) {
            return jdbcTemplate.query(baseQuery + timeFilter + latterQuery, new Object[] { from, to, from, to, limit, startIndex }, (ResultSet rs) -> {
                LinkedHashMap<String, ProjectDetailAnalysis> dateCountMap = new LinkedHashMap<>();
                while (rs.next()) {
                    ProjectDetailAnalysis projectDetailAnalysis = new ProjectDetailAnalysis();
                    if (!dateCountMap.containsKey(rs.getString("project"))) {
                        projectDetailAnalysis.setProjectId(rs.getString("project"));
                        projectDetailAnalysis.setProjectName(rs.getString("projectName"));
                        projectDetailAnalysis.setProjectStartDate(rs.getTimestamp("projectStartDate"));
                        projectDetailAnalysis.setProjectStatus(ProjectStatusEnum.valueOf(rs.getString("projectStatus")));
                        projectDetailAnalysis.setTaskCount(rs.getInt("taskCount"));
                        projectDetailAnalysis.setMemberCount(rs.getInt("memberCount"));
                        projectDetailAnalysis.setClosedCount(rs.getInt("closedCount"));
                        projectDetailAnalysis.setTimeTaken(rs.getInt("timeTaken"));
                        List<User> owner = new ArrayList<>();
                        owner.add(new User(rs.getString("userId"), rs.getString("firstName"), rs.getString("lastName"), rs.getString("profileImage")));
                        projectDetailAnalysis.setOwners(owner);
                        dateCountMap.put(rs.getString("project"), projectDetailAnalysis);
                    } else {
                        dateCountMap.get(rs.getString("project")).getOwners().add(new User(rs.getString("userId"), rs.getString("firstName"), rs.getString("lastName"), rs.getString("profileImage")));
                    }
                }
                return dateCountMap;
            });
        } else {

            return jdbcTemplate.query(baseQuery + latterQuery, new Object[]{from, to, limit, startIndex}, (ResultSet rs) -> {
                LinkedHashMap<String, ProjectDetailAnalysis> dateCountMap = new LinkedHashMap<>();
                while (rs.next()) {
                    ProjectDetailAnalysis projectDetailAnalysis = new ProjectDetailAnalysis();
                    if (!dateCountMap.containsKey(rs.getString("project"))) {
                        projectDetailAnalysis.setProjectId(rs.getString("project"));
                        projectDetailAnalysis.setProjectName(rs.getString("projectName"));
                        projectDetailAnalysis.setProjectStartDate(rs.getTimestamp("projectStartDate"));
                        projectDetailAnalysis.setProjectStatus(ProjectStatusEnum.valueOf(rs.getString("projectStatus")));
                        projectDetailAnalysis.setTaskCount(rs.getInt("taskCount"));
                        projectDetailAnalysis.setMemberCount(rs.getInt("memberCount"));
                        projectDetailAnalysis.setTimeTaken(rs.getInt("timeTaken"));
                        List<User> owner = new ArrayList<>();
                        owner.add(new User(rs.getString("userId"), rs.getString("firstName"), rs.getString("lastName"), rs.getString("profileImage")));
                        projectDetailAnalysis.setOwners(owner);
                        dateCountMap.put(rs.getString("project"), projectDetailAnalysis);
                    } else {
                        dateCountMap.get(rs.getString("project")).getOwners().add(new User(rs.getString("userId"), rs.getString("firstName"), rs.getString("lastName"), rs.getString("profileImage")));
                    }
                }
                return dateCountMap;
            });
        }
    }

    @Override
    public void addProjectKeys(Project_Keys project_keys) {
        try {
            jdbcTemplate.update(connection -> {
                PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO Project_Keys(projectId, domain, projectKey, isValid) VALUES(?,?,?,?)");
                preparedStatement.setString(1,project_keys.getProjectId());
                preparedStatement.setString(2, project_keys.getDomain());
                preparedStatement.setString(3, project_keys.getProjectKey());
                preparedStatement.setBoolean(4, project_keys.getIsValid());
                return preparedStatement;
            });
        } catch (Exception e){
            throw new PMException(e.getMessage());
        }
    }

    @Override
    public Project_Keys getProjectKey(String projectKey) {
        String sql = "SELECT * FROM Project_Keys WHERE projectKey=? AND isValid=true";

        try {
            return jdbcTemplate.queryForObject(sql, new Project_Keys(), projectKey);
        }
        catch (EmptyResultDataAccessException e){
            return null;
        }
        catch (Exception e){
            throw  new PMException(e.getMessage());
        }
    }

    @Override
    public Project_Keys getProjectKeyByDomain(String projectKey, String domain) {
        String sql = "SELECT * FROM Project_Keys WHERE projectKey=? AND domain=? AND isValid=true";
        try {
            return jdbcTemplate.queryForObject(sql, new Project_Keys(), projectKey, domain);
        } catch (EmptyResultDataAccessException e){
            return null;
        } catch (Exception e){
            throw new PMException(e.getMessage());
        }
    }

    @Override
    public void updateProjectKeys(ProjectKeys project_keys) {
        jdbcTemplate.update(connection -> {
            PreparedStatement preparedStatement = connection.prepareStatement("UPDATE Project_Keys SET domain=?, isValid=? WHERE projectKey=?");
            preparedStatement.setString(1,project_keys.getDomain());
            preparedStatement.setBoolean(2, project_keys.getValid());
            preparedStatement.setString(3, project_keys.getProjectKey());

            return preparedStatement;
        });
    }

    @Override
    public List<Project_Keys> getProjectKeys(String projectId) {
        String sql = "SELECT * FROM Project_Keys as PK INNER JOIN project as P ON P.project = PK.projectId WHERE projectId=?";
        try {
            return jdbcTemplate.query(sql, new Project_Keys(), projectId);
        } catch (Exception e){
            throw new PMException(e.getMessage());
        }
    }

    @Override
    public void addOrRemoveProjectSupport(String projectId, boolean status) {
        String sql = "UPDATE project SET isSupportAdded=?, isSupportEnabled=? WHERE project=?";
        try {
            jdbcTemplate.update(sql, status, status,projectId);
        } catch (Exception e){
            throw new PMException(e.getMessage());
        }
    }

    @Override
    public void addDefaultAssignee(String projectId, String defaultAssignee) {
        String sql = "UPDATE project SET defaultAssignee=? WHERE project=?";
        try {
            jdbcTemplate.update(sql,defaultAssignee, projectId);
        } catch (Exception e){
            throw new PMException(e.getMessage());
        }
    }

    @Override
    public List<SupportProjectResponse> getSupportProjects() {
        String sql = "SELECT * FROM project WHERE isSupportAdded=true AND isDeleted=false";
        try {
            return jdbcTemplate.query(sql, new SupportProjectResponse());
        } catch (Exception e) {
            throw new PMException(e.getMessage());
        }
    }

    @Override
    public HashMap<String, ProjectDetails> getProjectMapByIds(List<String> projectIds) {
        String sql = "SELECT project,projectName,projectAlias FROM project WHERE project IN (:projectIds) AND isDeleted=false AND isSupportEnabled=true";
        MapSqlParameterSource parameters = new MapSqlParameterSource();
        parameters.addValue("projectIds", projectIds);
        HashMap<String, ProjectDetails> projectMap = new HashMap<>();
        try {
            return namedParameterJdbcTemplate.query(sql, parameters, (ResultSet rs) -> {
                while (rs.next()) {
                    if (!projectMap.containsKey(rs.getString("project"))) {
                        ProjectDetails project = new ProjectDetails();
                        project.setProjectName(rs.getString("projectName"));
                        project.setProjectAlias(rs.getString("projectAlias"));
                        projectMap.put(rs.getString("project"), project);
                    }
                }
                return projectMap;
            });
        } catch (Exception e) {
            return null;
        }
    }

}
