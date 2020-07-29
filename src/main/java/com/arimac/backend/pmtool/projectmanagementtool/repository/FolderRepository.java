package com.arimac.backend.pmtool.projectmanagementtool.repository;

import com.arimac.backend.pmtool.projectmanagementtool.model.Folder;

public interface FolderRepository {
    void createFolder(Folder folder);
    Folder getFolderById(String folderId);
}
