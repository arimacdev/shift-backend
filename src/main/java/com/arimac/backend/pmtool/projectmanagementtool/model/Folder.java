package com.arimac.backend.pmtool.projectmanagementtool.model;

import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

public class Folder implements RowMapper<Folder> {
    private String folderId;
    private String projectId;
    private String folderName;
    private String folderCreator;
    private Timestamp folderCreatedAt;
    private String parentFolder;
    private boolean isDeleted;

    public Folder() {
    }

    public Folder(String folderId, String projectId, String folderName, String folderCreator, Timestamp folderCreatedAt, String parentFolder, boolean isDeleted) {
        this.folderId = folderId;
        this.projectId = projectId;
        this.folderName = folderName;
        this.folderCreator = folderCreator;
        this.folderCreatedAt = folderCreatedAt;
        this.parentFolder = parentFolder;
        this.isDeleted = isDeleted;
    }

    public String getFolderId() {
        return folderId;
    }

    public void setFolderId(String folderId) {
        this.folderId = folderId;
    }

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    public String getFolderName() {
        return folderName;
    }

    public void setFolderName(String folderName) {
        this.folderName = folderName;
    }

    public String getFolderCreator() {
        return folderCreator;
    }

    public void setFolderCreator(String folderCreator) {
        this.folderCreator = folderCreator;
    }

    public Timestamp getFolderCreatedAt() {
        return folderCreatedAt;
    }

    public void setFolderCreatedAt(Timestamp folderCreatedAt) {
        this.folderCreatedAt = folderCreatedAt;
    }

    public String getParentFolder() {
        return parentFolder;
    }

    public void setParentFolder(String parentFolder) {
        this.parentFolder = parentFolder;
    }

    public boolean getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(boolean deleted) {
        isDeleted = deleted;
    }

    @Override
    public Folder mapRow(ResultSet resultSet, int i) throws SQLException {
        return new Folder(
                resultSet.getString("folderId"),
                resultSet.getString("projectId"),
                resultSet.getString("folderName"),
                resultSet.getString("folderCreator"),
                resultSet.getTimestamp("folderCreatedAt"),
                resultSet.getString("parentFolder"),
                resultSet.getBoolean("isDeleted")
        );
    }
}
