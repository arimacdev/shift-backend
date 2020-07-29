package com.arimac.backend.pmtool.projectmanagementtool.Service;

import com.arimac.backend.pmtool.projectmanagementtool.dtos.Folder.FolderAddDto;

public interface FolderService {
    Object createFolder(String projectId, String userId, FolderAddDto folderAddDto);
    Object getMainFolders(String userId ,String projectId);
    Object getFilesFoldersOfFolder(String userId, String projectId, String folderId);
}
