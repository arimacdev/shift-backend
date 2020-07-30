package com.arimac.backend.pmtool.projectmanagementtool.repository.Impl;

import com.arimac.backend.pmtool.projectmanagementtool.exception.PMException;
import com.arimac.backend.pmtool.projectmanagementtool.model.Folder;
import com.arimac.backend.pmtool.projectmanagementtool.repository.FolderRepository;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.sql.PreparedStatement;
import java.util.List;

@Service
public class FolderRepositoryImpl implements FolderRepository {
    private final JdbcTemplate jdbcTemplate;

    public FolderRepositoryImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void createFolder(Folder folder) {
        try {
            jdbcTemplate.update(connection -> {
                PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO Folder(folderId, projectId, folderName, folderCreator, folderCreatedAt, parentFolder, isDeleted, taskId, folderType) VALUES (?,?,?,?,?,?,?,?,?)");
                preparedStatement.setString(1, folder.getFolderId());
                preparedStatement.setString(2, folder.getProjectId());
                preparedStatement.setString(3, folder.getFolderName());
                preparedStatement.setString(4, folder.getFolderCreator());
                preparedStatement.setTimestamp(5, folder.getFolderCreatedAt());
                preparedStatement.setString(6, folder.getParentFolder());
                preparedStatement.setBoolean(7, folder.getIsDeleted());
                preparedStatement.setString(8, folder.getTaskId());
                preparedStatement.setString(9, folder.getFolderType().toString());

                return preparedStatement;
            });
        } catch (Exception e){
            throw new PMException(e.getMessage());
        }
    }

    @Override
    public Folder getFolderById(String folderId) {
        String sql = "SELECT * FROM Folder WHERE folderId=? AND isDeleted=false";
        try {
           return jdbcTemplate.queryForObject(sql, new Folder(), folderId);
        } catch (EmptyResultDataAccessException e){
            return null;
        } catch (Exception e){
            throw  new PMException(e.getMessage());
        }
    }

    @Override
    public Folder getFolderByTaskId(String taskId) {
        String sql = "SELECT * FROM Folder WHERE taskId=? AND isDeleted=false";
        try {
            return jdbcTemplate.queryForObject(sql, new Folder(), taskId);
        } catch (EmptyResultDataAccessException e){
            return null;
        } catch (Exception e){
            throw new PMException(e.getMessage());
        }
    }

    @Override
    public List<Folder> getMainFolders(String projectId) {
        String sql = "SELECT * FROM Folder WHERE projectId=? AND parentFolder IS NULL AND isDeleted= false";
        try {
            return jdbcTemplate.query(sql, new Folder(), projectId);
        } catch (Exception e){
            throw new PMException(e.getMessage());
        }

    }

    @Override
    public List<Folder> getSubFoldersOfFolder(String folderId) {
        String sql = "SELECT * FROM Folder WHERE parentFolder=? AND isDeleted=false";
        try {
            return jdbcTemplate.query(sql, new Folder(), folderId);
        } catch (Exception e){
            throw new PMException(e.getMessage());
        }
    }
}
