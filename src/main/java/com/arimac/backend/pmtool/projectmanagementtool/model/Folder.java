package com.arimac.backend.pmtool.projectmanagementtool.model;

import com.arimac.backend.pmtool.projectmanagementtool.enumz.Folder.FolderTypeEnum;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

public class Folder implements RowMapper<Folder> {
    private String folderId;
    private String projectId;
    private String taskId;
    private String folderName;
    private String folderCreator;
    private Timestamp folderCreatedAt;
    private String parentFolder;
    private boolean isDeleted;
    private FolderTypeEnum folderType;

    public Folder() {
    }

    public Folder(String folderId, String projectId, String taskId, String folderName, String folderCreator, Timestamp folderCreatedAt, String parentFolder, boolean isDeleted, FolderTypeEnum folderType) {
        this.folderId = folderId;
        this.projectId = projectId;
        this.taskId = taskId;
        this.folderName = folderName;
        this.folderCreator = folderCreator;
        this.folderCreatedAt = folderCreatedAt;
        this.parentFolder = parentFolder;
        this.isDeleted = isDeleted;
        this.folderType = folderType;
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
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


    public FolderTypeEnum getFolderType() {
        return folderType;
    }

    public void setFolderType(FolderTypeEnum folderType) {
        this.folderType = folderType;
    }

    @Override
    public Folder mapRow(ResultSet resultSet, int i) throws SQLException {
        return new Folder(
                resultSet.getString("folderId"),
                resultSet.getString("projectId"),
                resultSet.getString("taskId"),
                resultSet.getString("folderName"),
                resultSet.getString("folderCreator"),
                resultSet.getTimestamp("folderCreatedAt"),
                resultSet.getString("parentFolder"),
                resultSet.getBoolean("isDeleted"),
                FolderTypeEnum.valueOf(resultSet.getString("folderType"))
        );
    }
}
