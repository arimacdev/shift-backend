package com.arimac.backend.pmtool.projectmanagementtool.repository.Impl;

import com.arimac.backend.pmtool.projectmanagementtool.Service.Impl.TaskServiceImpl;
import com.arimac.backend.pmtool.projectmanagementtool.dtos.*;
import com.arimac.backend.pmtool.projectmanagementtool.dtos.Filteration.WorkloadFilteration;
import com.arimac.backend.pmtool.projectmanagementtool.dtos.Sprint.TaskSprintUpdateDto;
import com.arimac.backend.pmtool.projectmanagementtool.dtos.Task.TaskParentChildUpdateDto;
import com.arimac.backend.pmtool.projectmanagementtool.enumz.FilterTypeEnum;
import com.arimac.backend.pmtool.projectmanagementtool.exception.PMException;
import com.arimac.backend.pmtool.projectmanagementtool.model.Task;
import com.arimac.backend.pmtool.projectmanagementtool.repository.TaskRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;

import java.sql.PreparedStatement;
import java.util.List;

@Service
public class TaskRepositoryImpl implements TaskRepository {

    private final JdbcTemplate jdbcTemplate;
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public TaskRepositoryImpl(JdbcTemplate jdbcTemplate, NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    private static final String ALL= "all";

    private static final Logger logger = LoggerFactory.getLogger(TaskServiceImpl.class);


    @Override
    public Task addTaskToProject(Task task) {
        jdbcTemplate.update(connection -> {
            PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO Task (taskId, projectId, taskName, taskInitiator, taskAssignee, taskNote, taskStatus, taskCreatedAt, taskDueDateAt, taskReminderAt, isDeleted, sprintId, issueType, parentId, isParent, secondaryTaskId) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
            preparedStatement.setString(1, task.getTaskId());
            preparedStatement.setString(2, task.getProjectId());
            preparedStatement.setString(3, task.getTaskName());
            preparedStatement.setString(4, task.getTaskInitiator());
            preparedStatement.setString(5, task.getTaskAssignee());
            preparedStatement.setString(6, task.getTaskNote());
            preparedStatement.setString(7, task.getTaskStatus().toString());
            preparedStatement.setTimestamp(8, task.getTaskCreatedAt());
           // preparedStatement.setDate(8, new java.sql.Date(task.getTaskCreatedAt().getTime()));
            preparedStatement.setTimestamp(9, task.getTaskDueDateAt());
            preparedStatement.setTimestamp(10, task.getTaskReminderAt());
            preparedStatement.setBoolean(11, task.getIsDeleted());
            preparedStatement.setString(12, task.getSprintId());
            preparedStatement.setString(13, task.getIssueType().toString());
            preparedStatement.setString(14, task.getParentId());
            preparedStatement.setBoolean(15, task.getIsParent());
            preparedStatement.setString(16, task.getSecondaryTaskId());

            return preparedStatement;
        });
        return task;
    }

    @Override
    public List<Task> getAllProjectTasksByUser(String projectId) {
        String sql = "SELECT * FROM Task WHERE projectId=? AND isDeleted=false";
        List<Task> taskList = jdbcTemplate.query(sql, new Task(), projectId);
        return  taskList;
    }

    @Override
    public List<TaskUserResponseDto> getAllProjectTasksWithProfile(String projectId) {
        String sql = "SELECT * FROM Task as t " +
                "LEFT JOIN User AS u ON t.taskAssignee=u.userId " +
                "WHERE t.projectId=? AND t.isDeleted=false";
        List<TaskUserResponseDto> taskList = jdbcTemplate.query(sql, new TaskUserResponseDto(), projectId);
        return  taskList;
    }

    @Override
    public List<TaskUserResponseDto> getAllParentTasksWithProfile(String projectId, int limit, int offset) {
        String sql = "SELECT * FROM Task as t LEFT JOIN User AS u ON t.taskAssignee=u.userId WHERE t.projectId=? AND t.isDeleted=false AND t.isParent=true" +
                " ORDER BY FIELD(taskStatus, 'closed') ASC, taskCreatedAt DESC LIMIT ? OFFSET ?";
        List<TaskUserResponseDto> taskList = jdbcTemplate.query(sql, new TaskUserResponseDto(), projectId, limit, offset);
        return  taskList;
    }

    @Override
    public List<TaskUserResponseDto> getAllChildrenOfParentTaskList(List<String> parentIds) {
        String sql = "SELECT * FROM Task as T INNER JOIN User AS U ON T.taskAssignee=U.userId WHERE parentId IN (:ids) AND isDeleted=false";
        MapSqlParameterSource parameters = new MapSqlParameterSource();
        parameters.addValue("ids", parentIds);
        List<TaskUserResponseDto> taskList = namedParameterJdbcTemplate.query(sql, parameters, new TaskUserResponseDto());
        return taskList;
    }

    @Override public List<TaskUserResponseDto> getAllChildTasksWithProfile(String projectId) {
        String sql = "SELECT * FROM Task as t " +
                "LEFT JOIN User AS u ON t.taskAssignee=u.userId " +
                "WHERE t.projectId=? AND t.isDeleted=false AND t.isParent=false";
        List<TaskUserResponseDto> taskList = jdbcTemplate.query(sql, new TaskUserResponseDto(), projectId);
        return  taskList;
    }

    @Override
    public List<Task> getAllUserAssignedTasks(String userId, String projectId) {
       String sql = "SELECT * FROM Task WHERE projectId=? AND taskAssignee=? AND isDeleted=false";
       List<Task> taskList = jdbcTemplate.query(sql, new Task(), projectId, userId);
       return taskList;
    }

    @Override
    public List<TaskUserResponseDto> getAllUserAssignedTasksWithProfile(String userId, String projectId) {
        String sql = "SELECT * FROM Task as t " +
                "LEFT JOIN User AS u ON t.taskAssignee=u.userId " +
                "WHERE t.projectId=? AND t.taskAssignee=? AND t.isDeleted=false";
        List<TaskUserResponseDto> taskList = jdbcTemplate.query(sql, new TaskUserResponseDto(), projectId, userId);
        return  taskList;
    }

    @Override
    public Task getProjectTask(String taskId) {
        String sql = "SELECT * FROM Task WHERE taskId=? AND isDeleted=false";
        Task task;
        try {
            task = jdbcTemplate.queryForObject(sql, new Task(), taskId);
        } catch (EmptyResultDataAccessException e){
            return null;
        }
        return task;
    }

    @Override
    public int getProjectTaskCount(String projectId) {
        String sql = "SELECT COUNT(*) FROM Task WHERE projectId=? AND isDeleted=false";
        try {
            return jdbcTemplate.queryForObject(sql, new Object[] {projectId} , Integer.class);
        } catch (Exception e){
            throw new PMException(e.getMessage());
        }
    }

    @Override
    public Task getProjectTasksWithFlag(String taskId) {
        String sql = "SELECT * FROM Task WHERE taskId=?";
        Task task;
        try {
            task = jdbcTemplate.queryForObject(sql, new Task(), taskId);
        } catch (EmptyResultDataAccessException e){
            return null;
        }
        return task;
    }

    @Override
    public Task getTaskByProjectIdTaskId(String projectId, String taskId) {
        String sql = "SELECT * FROM Task WHERE taskId=? AND projectId=? AND isDeleted=false";
        Task task;
        try {
            task = jdbcTemplate.queryForObject(sql, new Task(), taskId, projectId);
        } catch (EmptyResultDataAccessException e){
            return null;
        }
        return task;
    }

    @Override
    public Task getProjectTaskWithDeleted(String taskId) {
        String sql = "SELECT * FROM Task WHERE taskId=?";
        Task task;
        try {
            task = jdbcTemplate.queryForObject(sql, new Task(), taskId);
        } catch (EmptyResultDataAccessException e){
            return null;
        }
        return task;
    }

    @Override
    public Object updateProjectTask(String taskId, TaskUpdateDto taskUpdateDto) {
        jdbcTemplate.update(connection -> {
            PreparedStatement preparedStatement = connection.prepareStatement("UPDATE Task SET taskName=?, taskAssignee=?, taskNote=?, taskStatus=?, taskDueDateAt=?, taskReminderAt=?, issueType=? WHERE taskId=?");
            preparedStatement.setString(1, taskUpdateDto.getTaskName());
            preparedStatement.setString(2, taskUpdateDto.getTaskAssignee());
            preparedStatement.setString(3, taskUpdateDto.getTaskNotes());
            preparedStatement.setString(4, taskUpdateDto.getTaskStatus().toString());
            preparedStatement.setTimestamp(5, taskUpdateDto.getTaskDueDate());
            preparedStatement.setTimestamp(6, taskUpdateDto.getTaskRemindOnDate());
            preparedStatement.setString(7, taskUpdateDto.getIssueType().toString());
            preparedStatement.setString(8, taskId);

            return preparedStatement;
        });
        return taskUpdateDto;
    }

    @Override
    public void flagProjectTask(String taskId) {
        String sql = "UPDATE Task SET isDeleted=true WHERE taskId=?";
        jdbcTemplate.update(sql, taskId);
    }

    @Override
    public void flagProjectBoundTasks(String projectId) {
        String sql = "UPDATE Task SET isDeleted=true WHERE projectId=?";
        jdbcTemplate.update(sql, projectId);
    }

    @Override
    public List<WorkLoadTaskStatusDto> getAllUsersWithTaskCompletion(List<String> assignees, String from, String to) {
        StringBuilder userQuery = new StringBuilder();
        if (assignees.isEmpty())
            throw  new PMException("Assignee List Empty", HttpStatus.BAD_REQUEST);
        boolean allUsers = false;
        for (int u = 0; u < assignees.size(); u++){
            if (assignees.get(0).toLowerCase().equals("all")) {
                allUsers = true;
                break;
            }
            String user1 = "\"" + assignees.get(u)+ "\"";
            userQuery.append(user1);
            if (u != assignees.size() - 1)
            userQuery.append(",");
        }
        String query;
        List<WorkLoadTaskStatusDto> workLoadList;
        if (allUsers){
            query = "SELECT * FROM User AS u " +
                    "LEFT JOIN Task AS t on u.userId = t.taskAssignee " +
                    "LEFT JOIN project AS p ON t.projectId = p.project " +
                    "WHERE (t.isDeleted = false OR t.isDeleted IS NULL ) " +
                    "AND (p.isDeleted=false OR p.isDeleted IS NULL)";
        } else {
            query = "SELECT * FROM User AS u " +
                    "LEFT JOIN Task AS t on u.userId = t.taskAssignee " +
                    "LEFT JOIN project AS p ON t.projectId = p.project " +
                    "WHERE (t.isDeleted = false OR t.isDeleted IS NULL ) " +
                    "AND (p.isDeleted=false OR p.isDeleted IS NULL)" +
                    "AND userId IN (" + userQuery.toString() + ")";
        }
        workLoadList = jdbcTemplate.query(query, new WorkLoadTaskStatusDto());
        logger.info("query {}", query);
        return workLoadList;
    }

    @Override
    public List<WorkLoadProjectDto> getAllUserAssignedTaskWithCompletion(String userId, String from, String to) {
        String sql;
        if (from.equals(ALL) || to.equals(ALL)) {
             sql = "SELECT * FROM Project_User AS pu\n" +
                    "        LEFT JOIN Task AS t ON (t.projectId = pu.projectId)\n" +
                    "        INNER JOIN project p on pu.projectId = p.project\n" +
                    "WHERE (pu.assigneeId=?) AND (p.isDeleted=false) AND (t.isDeleted = false OR t.isDeleted IS NULL)";
            return jdbcTemplate.query(sql, new WorkLoadProjectDto(), userId);
        } else {
            sql = "SELECT * FROM Task AS t\n" +
                    "INNER JOIN project p on projectId = p.project\n" +
                    "WHERE (t.taskAssignee=?) AND (p.isDeleted=false) AND (t.isDeleted = false OR t.isDeleted IS NULL )" +
                    "AND (t.taskDueDateAt BETWEEN ? AND ?)";
            return jdbcTemplate.query(sql, new WorkLoadProjectDto(), userId, from, to);
        }
    }


    @Override
    public List<WorkloadFilteration> taskFilteration(String incomingQuery, String orderQuery) {
        String baseQuery = "SELECT * FROM Task INNER JOIN project ON projectId = project LEFT JOIN User on taskAssignee = userId WHERE ";
        String conditionQuery = " AND (project.isDeleted=false) AND (Task.isDeleted = false OR Task.isDeleted IS NULL)";
        String orderBy = "ORDER BY ";
        String completeQuery;
        if (orderQuery == null || orderQuery.isEmpty())
            completeQuery = baseQuery + incomingQuery + conditionQuery;
        else
            completeQuery = baseQuery + incomingQuery + conditionQuery + orderBy + orderQuery;
        logger.info("Final Query : {}", completeQuery);
        try{
            return jdbcTemplate.query(completeQuery, new WorkloadFilteration());
        } catch (Exception e){
            throw new PMException(e.getMessage());
        }
    }

    @Override
    public void updateProjectTaskSprint(String taskId, TaskSprintUpdateDto taskSprintUpdateDto) {
        jdbcTemplate.update(connection -> {
            PreparedStatement preparedStatement = connection.prepareStatement("UPDATE Task SET sprintId=? WHERE taskId=?");
            preparedStatement.setString(1, taskSprintUpdateDto.getNewSprint());
            preparedStatement.setString(2, taskId);

            return preparedStatement;
        });
    }

    @Override
    public void updateProjectTaskParent(String taskId, TaskParentChildUpdateDto taskParentChildUpdateDto) {
        jdbcTemplate.update(connection -> {
            PreparedStatement preparedStatement = connection.prepareStatement("UPDATE Task SET parentId=? WHERE taskId=?");
            preparedStatement.setString(1, taskParentChildUpdateDto.getNewParent());
            preparedStatement.setString(2, taskId);

            return preparedStatement;
        });
    }

    @Override
    public void transitionFromParentToChild(String taskId, TaskParentChildUpdateDto taskParentChildUpdateDto) {
        jdbcTemplate.update(connection -> {
            PreparedStatement preparedStatement = connection.prepareStatement("UPDATE Task SET parentId=?, isParent=? WHERE taskId=?");
            preparedStatement.setString(1, taskParentChildUpdateDto.getNewParent());
            preparedStatement.setBoolean(2, false);
            preparedStatement.setString(3, taskId);

            return preparedStatement;
        });
    }

    @Override
    public void addParentToParentTask(String taskId, TaskParentChildUpdateDto taskParentChildUpdateDto) {
        jdbcTemplate.update(connection -> {
            PreparedStatement preparedStatement = connection.prepareStatement("UPDATE Task SET parentId=?, isParent=? WHERE taskId=?");
            preparedStatement.setString(1, taskParentChildUpdateDto.getNewParent());
            preparedStatement.setBoolean(2, true);
            preparedStatement.setString(3, taskId);

            return preparedStatement;
        });
    }

    @Override
    public List<TaskUserResponseDto> getAllChildrenOfParentTaskWithProfile(String taskId) {
        String sql = "SELECT * FROM Task AS T INNER JOIN User AS U ON T.taskAssignee=U.userId WHERE T.parentId=? AND T.isDeleted=false";
        List<TaskUserResponseDto> children = jdbcTemplate.query(sql, new TaskUserResponseDto(), taskId);
        return children;
    }

    @Override
    public List<Task> getAllChildrenOfParentTask(String taskId) {
        String sql = "SELECT * FROM Task WHERE parentId=? AND isDeleted=false";
        List<Task> taskList = jdbcTemplate.query(sql, new Task(), taskId);
        return taskList;
    }

    @Override
    public boolean checkChildTasksOfAParentTask(String taskId) {
        String sql = "SELECT * FROM Task WHERE parentId=? AND isDeleted=false";
        List<Task> children = jdbcTemplate.query(sql, new Task(), taskId);
        if (children.isEmpty())
            return false;
        else
            return true;
    }

    @Override
    public List<TaskUserDto> filterTasks(String projectId, FilterTypeEnum filterType, String from, String to, String assignee, String issueType) {
        String sql;
        switch (filterType){
            case issueType:
                sql = "SELECT * FROM Task AS T INNER JOIN User AS U ON U.userId=T.taskAssignee  WHERE projectId=? AND isDeleted=false AND issueType=?" +
                " ORDER BY FIELD(taskStatus, 'closed') ASC,  taskCreatedAt DESC";
                return jdbcTemplate.query(sql, new TaskUserDto(),projectId, issueType);
            case dueDate:
                sql = "SELECT * FROM Task AS T INNER JOIN User AS U ON U.userId=T.taskAssignee WHERE projectId=? AND  isDeleted=false AND (taskDueDateAt BETWEEN ? AND ?)"+
                        " ORDER BY FIELD(taskStatus, 'closed') ASC,  taskCreatedAt DESC";
                logger.info("sql {}", sql);
                return jdbcTemplate.query(sql, new TaskUserDto(), projectId, from, to);
            case assignee:
                sql = "SELECT * FROM Task AS T INNER JOIN User AS U ON U.userId=T.taskAssignee WHERE projectId=? AND taskAssignee=? AND isDeleted=false" +
                        " ORDER BY FIELD(taskStatus, 'closed') ASC,  taskCreatedAt DESC";
                logger.info("sql {}", sql);
                return jdbcTemplate.query(sql, new TaskUserDto(), projectId, assignee);
        }
        return null;
    }

    @Override
    public void updateProjectAlias(String taskId, String alias) {
       jdbcTemplate.update(connection -> {
           PreparedStatement preparedStatement  = connection.prepareStatement("UPDATE Task SET secondaryTaskId=? WHERE taskId=?");
           preparedStatement.setString(1, alias);
           preparedStatement.setString(2,taskId);

           return preparedStatement;
       });
    }


}
