package com.arimac.backend.pmtool.projectmanagementtool.Service;

import com.arimac.backend.pmtool.projectmanagementtool.dtos.Folder.FolderDto;

public interface FolderService {
    Object createFolder(String projectId, String userId, FolderDto folderDto);
    Object getMainFolders(String userId ,String projectId);
    Object getFilesFoldersOfFolder(String userId, String projectId, String folderId);
    Object updateFolder(String userId, String projectId, String folderId, FolderDto folderDto);
}
