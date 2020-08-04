package com.arimac.backend.pmtool.projectmanagementtool.repository.Impl;

import com.arimac.backend.pmtool.projectmanagementtool.dtos.Files.TaskFileUserProfileDto;
import com.arimac.backend.pmtool.projectmanagementtool.dtos.Folder.MoveFolderDto;
import com.arimac.backend.pmtool.projectmanagementtool.exception.PMException;
import com.arimac.backend.pmtool.projectmanagementtool.model.TaskFile;
import com.arimac.backend.pmtool.projectmanagementtool.repository.TaskFileRepository;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;

import java.sql.PreparedStatement;
import java.util.List;

@Service
public class TaskFileRepositoryImpl implements TaskFileRepository {
    private final JdbcTemplate jdbcTemplate;
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public TaskFileRepositoryImpl(JdbcTemplate jdbcTemplate, NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    @Override
    public Object uploadTaskFile(TaskFile taskFile) {
        jdbcTemplate.update(connection -> {
            PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO TaskFile (taskFileId, taskId, taskFileName, taskFileUrl, taskFileCreator, taskFileDate, isDeleted, taskFileSize, taskFolder) VALUES(?,?,?,?,?,?,?,?,?)");
            preparedStatement.setString(1, taskFile.getTaskFileId());
            preparedStatement.setString(2, taskFile.getTaskId());
            preparedStatement.setString(3, taskFile.getTaskFileName());
            preparedStatement.setString(4, taskFile.getTaskFileUrl());
            preparedStatement.setString(5, taskFile.getTaskFileCreator());
            preparedStatement.setTimestamp(6, taskFile.getTaskFileDate());
            preparedStatement.setBoolean(7, false);
            preparedStatement.setInt(8, taskFile.getTaskFileSize());
            preparedStatement.setString(9, taskFile.getTaskFolder());

            return preparedStatement;
        });
        return taskFile;
    }

    @Override
    public TaskFile getTaskFileById(String fileId) {
        String sql = "SELECT * FROM TaskFile Where taskFileId=? AND isDeleted=false";
        try {
            return jdbcTemplate.queryForObject(sql, new TaskFile(), fileId);
        }catch (EmptyResultDataAccessException e){
            return null;
        }
    }

    @Override
    public TaskFile getTaskFileWithFlag(String fileId) {
        String sql = "SELECT * FROM TaskFile Where taskFileId=?";
        try {
            return jdbcTemplate.queryForObject(sql, new TaskFile(), fileId);
        }catch (EmptyResultDataAccessException e){
            return null;
        }
    }

    @Override
    public List<TaskFile> getAllTaskFiles(String taskId) {
        String sql = "SELECT * FROM TaskFile WHERE taskId=? AND isDeleted=false";
        List<TaskFile> taskFileList = jdbcTemplate.query(sql, new TaskFile(), taskId);
        return taskFileList;
    }

    @Override
    public List<TaskFileUserProfileDto> getTaskFilesWithUserProfile(String taskId) {
        String sql = "SELECT * FROM TaskFile as TF INNER JOIN User AS U ON TF.taskFileCreator = U.userId WHERE TF.taskId=? AND TF.isDeleted=false";
        List<TaskFileUserProfileDto> taskFileUserProfileList = jdbcTemplate.query(sql, new TaskFileUserProfileDto(), taskId);
        return  taskFileUserProfileList;
    }

    @Override
    public void flagTaskFile(String taskFileId) {
        String sql = "UPDATE TaskFile SET isDeleted=? WHERE taskFileId=?";
        jdbcTemplate.update(sql,true, taskFileId);
    }

    @Override
    public List<TaskFile> getFolderTaskFiles(String folderId) {
        String sql = "SELECT * FROM TaskFile WHERE taskFolder=? AND isDeleted=false";
        try {
             return jdbcTemplate.query(sql, new TaskFile(), folderId);
        } catch (Exception e){
            throw new PMException(e.getMessage());
        }
    }

    @Override
    public void flagFolderTaskFiles(String folderId) {
        String sql = "UPDATE TaskFile SET isDeleted=true WHERE taskFolder=?";
        try {
            jdbcTemplate.update(sql, folderId);
        } catch (Exception e){
            throw new PMException(e.getMessage());
        }
    }

    @Override
    public void updateTaskFolder(MoveFolderDto moveFolderDto) {
        String sql = "UPDATE TaskFile SET taskFolder=? WHERE taskFileId=?";
        try {
            jdbcTemplate.update(sql, moveFolderDto.getNewParentFolder(), moveFolderDto.getFileId());
        } catch (Exception e){
            throw new PMException(e.getMessage());
        }
    }

    @Override
    public List<TaskFile> filterFilesByName(String name, List<String> taskIds) {
        String sql = "SELECT * FROM TaskFile WHERE taskId IN (:taskIds) AND taskFileName LIKE :filterName AND isDeleted=false";
        MapSqlParameterSource parameters = new MapSqlParameterSource();
        parameters.addValue("taskIds", taskIds);
        parameters.addValue("filterName", name + "%");
        try {
            return namedParameterJdbcTemplate.query(sql, parameters, new TaskFile());
        } catch (Exception e){
            throw new PMException(e.getMessage());
        }
    }

}
