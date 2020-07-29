package com.arimac.backend.pmtool.projectmanagementtool.dtos.Folder;

import com.arimac.backend.pmtool.projectmanagementtool.model.Folder;
import com.arimac.backend.pmtool.projectmanagementtool.model.ProjectFile;

import java.util.List;

public class FolderFileList {
    private List<Folder> folders;
    private List<ProjectFile> files;

    public List<Folder> getFolders() {
        return folders;
    }

    public void setFolders(List<Folder> folders) {
        this.folders = folders;
    }

    public List<ProjectFile> getFiles() {
        return files;
    }

    public void setFiles(List<ProjectFile> files) {
        this.files = files;
    }
}
