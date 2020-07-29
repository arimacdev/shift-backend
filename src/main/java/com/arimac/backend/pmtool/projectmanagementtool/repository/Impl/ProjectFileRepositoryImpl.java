package com.arimac.backend.pmtool.projectmanagementtool.repository.Impl;

import com.arimac.backend.pmtool.projectmanagementtool.dtos.ProjectFileResponseDto;
import com.arimac.backend.pmtool.projectmanagementtool.exception.PMException;
import com.arimac.backend.pmtool.projectmanagementtool.model.ProjectFile;
import com.arimac.backend.pmtool.projectmanagementtool.repository.ProjectFileRepository;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.sql.PreparedStatement;
import java.sql.Timestamp;
import java.util.List;

@Service
public class ProjectFileRepositoryImpl implements ProjectFileRepository {
    private final JdbcTemplate jdbcTemplate;

    public ProjectFileRepositoryImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void uploadProjectFile(ProjectFile projectFile) {
            jdbcTemplate.update(connection -> {
                PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO ProjectFile(projectFileId, projectId, projectFileName, projectFileUrl, projectFileSize, projectFileAddedBy, projectFileAddedOn, isDeleted) VALUES (?,?,?,?,?,?,?,?)");
                preparedStatement.setString(1, projectFile.getProjectFileId());
                preparedStatement.setString(2, projectFile.getProjectId());
                preparedStatement.setString(3, projectFile.getProjectFileName());
                preparedStatement.setString(4, projectFile.getProjectFileUrl());
                preparedStatement.setInt(5, projectFile.getProjectFileSize());
                preparedStatement.setString(6, projectFile.getProjectFileAddedBy());
                preparedStatement.setTimestamp(7, projectFile.getProjectFileAddedOn());
                preparedStatement.setBoolean(8, projectFile.getIsDeleted());

                return preparedStatement;
            });

    }

    @Override
    public List<ProjectFileResponseDto> getAllProjectFiles(String projectId) {
        String sql = "SELECT * FROM ProjectFile as PF INNER JOIN User U ON PF.projectFileAddedBy = U.userId WHERE PF.projectId=? AND PF.isDeleted=false";
        List<ProjectFileResponseDto> projectFiles = jdbcTemplate.query(sql, new ProjectFileResponseDto(), projectId);
        return projectFiles;
    }

    @Override
    public void flagProjectFile(String projectFileId) {
        String sql = "UPDATE ProjectFile SET isDeleted=true WHERE projectFileId=?";
        jdbcTemplate.update(sql, projectFileId);
    }

    @Override
    public ProjectFile getProjectFile(String projectFile) {
        String sql = "SELECT * FROM ProjectFile WHERE projectFileId=? AND isDeleted=false";
        try {
            return jdbcTemplate.queryForObject(sql, new ProjectFile(), projectFile);
        } catch (EmptyResultDataAccessException e){
            return null;
        }

    }

    @Override
    public ProjectFile getProjectFileWithFlag(String projectFile) {
        String sql = "SELECT * FROM ProjectFile WHERE projectFileId=?";
        try {
            return jdbcTemplate.queryForObject(sql, new ProjectFile(), projectFile);
        } catch (EmptyResultDataAccessException e){
            return null;
        }
    }

    @Override
    public List<ProjectFile> getMainProjectFiles(String projectId) {
        String sql = "SELECT * FROM ProjectFile WHERE projectId=? AND projectFolder IS NULL AND isDeleted=false";
        try {
            return jdbcTemplate.query(sql, new ProjectFile(), projectId);
        } catch (Exception e){
            throw  new PMException(e.getMessage());
        }
    }

    @Override
    public List<ProjectFile> getFolderProjectFiles(String folderId) {
        String sql = "SELECT * FROM ProjectFile WHERE projectFolder=?";
        try {
            return jdbcTemplate.query(sql, new ProjectFile(), folderId);
        } catch (Exception e){
            throw new PMException(e.getMessage());
        }
    }
}
