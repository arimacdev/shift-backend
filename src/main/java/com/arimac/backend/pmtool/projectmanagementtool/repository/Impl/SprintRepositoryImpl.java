package com.arimac.backend.pmtool.projectmanagementtool.repository.Impl;

import com.arimac.backend.pmtool.projectmanagementtool.dtos.Sprint.SprintUpdateDto;
import com.arimac.backend.pmtool.projectmanagementtool.model.Sprint;
import com.arimac.backend.pmtool.projectmanagementtool.repository.SprintRepository;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.sql.PreparedStatement;
import java.util.List;

@Service
public class SprintRepositoryImpl implements SprintRepository {

    private final JdbcTemplate jdbcTemplate;

    public SprintRepositoryImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void createSprint(Sprint sprint) {
        jdbcTemplate.update(connection -> {
            PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO Sprint (sprintId, projectId, sprintName, sprintCreatedBy, sprintCreatedAt, isDeleted, sprintDescription) VALUES (?,?,?,?,?,?,?)");
            preparedStatement.setString(1, sprint.getSprintId());
            preparedStatement.setString(2, sprint.getProjectId());
            preparedStatement.setString(3, sprint.getSprintName());
            preparedStatement.setString(4, sprint.getSprintCreatedBy());
            preparedStatement.setTimestamp(5, sprint.getSprintCreatedAt());
            preparedStatement.setBoolean(6, sprint.getIsDeleted());
            preparedStatement.setString(7, sprint.getSprintDescription());

            return preparedStatement;
        });
    }

    @Override
    public List<Sprint> getAllSprints(String projectId) {
        String sql = "SELECT * FROM Sprint WHERE projectId=? AND isDeleted=false";
        List<Sprint> sprints = jdbcTemplate.query(sql, new Sprint(), projectId);
        return sprints;
    }

    @Override
    public Sprint getSprintById(String sprintId) {
        String sql = "SELECT * FROM Sprint WHERE sprintId=? AND isDeleted=false";
        try {
            return jdbcTemplate.queryForObject(sql, new Sprint(), sprintId);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    @Override
    public void updateSprint(String sprintId, SprintUpdateDto sprintUpdateDto) {
        jdbcTemplate.update(connection -> {
            PreparedStatement preparedStatement = connection.prepareStatement("UPDATE Sprint SET sprintName=? ,sprintDescription=? WHERE sprintId=?");
            preparedStatement.setString(1, sprintUpdateDto.getSprintName());
            preparedStatement.setString(2, sprintUpdateDto.getSprintDescription());
            preparedStatement.setString(3, sprintId);

            return preparedStatement;
        });
    }
}
