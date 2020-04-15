package com.arimac.backend.pmtool.projectmanagementtool.repository.Impl;

import com.arimac.backend.pmtool.projectmanagementtool.model.ProjectFile;
import com.arimac.backend.pmtool.projectmanagementtool.repository.ProjectFileRepository;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.sql.PreparedStatement;
import java.sql.Timestamp;

@Service
public class ProjectFileRepositoryImpl implements ProjectFileRepository {
    private final JdbcTemplate jdbcTemplate;

    public ProjectFileRepositoryImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void uploadProjectFile(ProjectFile projectFile) {
            jdbcTemplate.update(connection -> {
                PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO ProjectFile(projectFileId, projectId, projectFileName, projectFileUrl, projectFileAddedBy, projectFileAddedOn, isDeleted) VALUES (?,?,?,?,?,?,?)");
                preparedStatement.setString(1, projectFile.getProjectFileId());
                preparedStatement.setString(2, projectFile.getProjectId());
                preparedStatement.setString(3, projectFile.getProjectFileName());
                preparedStatement.setString(4, projectFile.getProjectFileUrl());
                preparedStatement.setString(5, projectFile.getProjectFileAddedBy());
                preparedStatement.setTimestamp(6, projectFile.getProjectFileAddedOn());
                preparedStatement.setBoolean(7, projectFile.getIsDeleted());

                return preparedStatement;
            });

    }
}
