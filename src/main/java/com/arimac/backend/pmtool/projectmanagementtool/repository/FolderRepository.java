package com.arimac.backend.pmtool.projectmanagementtool.repository;

import com.arimac.backend.pmtool.projectmanagementtool.dtos.Folder.FolderDto;
import com.arimac.backend.pmtool.projectmanagementtool.model.Folder;

import java.util.List;

public interface FolderRepository {
    void createFolder(Folder folder);
    Folder getFolderById(String folderId);
    Folder getFolderByTaskId(String taskId);
    List<Folder> getMainFolders(String projectId);
    List<Folder> getSubFoldersOfFolder(String folderId);
    void updateFolder(FolderDto folderDto, String folderId);
}
