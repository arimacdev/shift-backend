package com.arimac.backend.pmtool.projectmanagementtool.repository.Impl;

import com.arimac.backend.pmtool.projectmanagementtool.exception.PMException;
import com.arimac.backend.pmtool.projectmanagementtool.model.TaskRelationship;
import com.arimac.backend.pmtool.projectmanagementtool.repository.TaskRelationshipRepository;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.sql.PreparedStatement;
import java.util.List;

@Service
public class TaskRelationshipRepositoryImpl implements TaskRelationshipRepository {

    private final JdbcTemplate jdbcTemplate;

    public TaskRelationshipRepositoryImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void createTaskRelationship(TaskRelationship taskRelationship) {
        try {
            jdbcTemplate.update(connection -> {
                PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO TaskRelationship (projectId,fromLink, toLink) VALUES (?,?,?)");
                preparedStatement.setString(1, taskRelationship.getProjectId());
                preparedStatement.setString(2, taskRelationship.getFromLink());
                preparedStatement.setString(3, taskRelationship.getToLink());
                return preparedStatement;
            });
        } catch (Exception e){
            throw new PMException(e.getMessage());
        }
    }

    @Override
    public List<TaskRelationship> getTaskRelationship(String taskId) {
        String sql = "SELECT * FROM TaskRelationship WHERE fromLink=? OR toLink=?";
        try {
            return jdbcTemplate.query(sql, new TaskRelationship(), taskId, taskId);
        } catch (Exception e){
            throw  new PMException(e.getMessage());
        }
    }
}
