package com.arimac.backend.pmtool.projectmanagementtool.Service;

import com.arimac.backend.pmtool.projectmanagementtool.dtos.Folder.FolderDto;
import com.arimac.backend.pmtool.projectmanagementtool.dtos.Folder.MoveFolderDto;

public interface FolderService {
    Object createFolder(String projectId, String userId, FolderDto folderDto);
    Object getMainFolders(String userId ,String projectId);
    Object getFilesFoldersOfFolder(String userId, String projectId, String folderId);
    Object updateFolder(String userId, String projectId, String folderId, FolderDto folderDto);
    Object deleteFolder(String userId, String projectId, String folderId);
    Object moveFileToFolder(String userId, String projectId, MoveFolderDto moveFolderDto);
}
