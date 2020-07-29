package com.arimac.backend.pmtool.projectmanagementtool.dtos.Folder;

import com.arimac.backend.pmtool.projectmanagementtool.model.Folder;
import com.arimac.backend.pmtool.projectmanagementtool.model.ProjectFile;

import java.util.List;

public class MainFolderFileList {
    private List<Folder> mainFolders;
    private List<ProjectFile> mainFiles;

    public List<Folder> getMainFolders() {
        return mainFolders;
    }

    public void setMainFolders(List<Folder> mainFolders) {
        this.mainFolders = mainFolders;
    }

    public List<ProjectFile> getMainFiles() {
        return mainFiles;
    }

    public void setMainFiles(List<ProjectFile> mainFiles) {
        this.mainFiles = mainFiles;
    }
}
