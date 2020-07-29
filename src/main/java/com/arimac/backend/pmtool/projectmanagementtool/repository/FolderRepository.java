package com.arimac.backend.pmtool.projectmanagementtool.repository;

import com.arimac.backend.pmtool.projectmanagementtool.model.Folder;

import java.util.List;

public interface FolderRepository {
    void createFolder(Folder folder);
    Folder getFolderById(String folderId);
    List<Folder> getMainFolders(String projectId);
}
